package com.zhsj.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.refund.PinganRefundBean;
import com.zhsj.api.bean.refund.PinganRefundResultBean;
import com.zhsj.api.bean.refund.PinganRefundSearchBean;
import com.zhsj.api.bean.refund.PinganRefundSearchResult;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.wft.HttpsRequest;
import com.zhsj.api.util.wft.RandomStringGenerator;
import com.zhsj.api.util.wft.Signature;

@Service
public class PinganService {
	
    private static final Logger logger = LoggerFactory.getLogger(MinshengService.class);
    
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;

	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#PinganService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			PinganRefundBean reqData = new PinganRefundBean();
			reqData.setService("unified.trade.refund");
			reqData.setMch_id(storePayInfo.getMchId());
			reqData.setOut_trade_no(orderBean.getOrderId());
			reqData.setOut_refund_no("re"+orderBean.getOrderId());
			reqData.setTotal_fee(String.valueOf((int)Arith.mul(orderBean.getActualChargeAmount(), 100)));
			reqData.setRefund_fee(String.valueOf((int)Arith.mul(price, 100)));
			reqData.setOp_user_id(String.valueOf(userId));
			reqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(10));
			reqData.setSign(Signature.getSign(reqData.toMap(),storePayInfo.getField1()));
			String postDataXML = XMLBeanUtils.mapToXml(reqData.toMap());
			logger.info("#PinganService.payWeChat# postDataXml={}",postDataXML);
			String resultString = new HttpsRequest().sendPost("https://pay.swiftpass.cn/pay/gateway", postDataXML, "", "");
			logger.info("#PinganService.payWeChat# resultString={}",resultString);
			PinganRefundResultBean resData = PinganRefundResultBean.getPinganRefundResultBean(resultString);
			if(!"0".equals(resData.getStatus())){
				result = resData.getMessage();
			}else {
				if(!"0".equals(resData.getResult_code())){
					result = resData.getErr_code();
				}else{
					result = "SUCCESS";
				}
			}
		}catch (Exception e) {
			result = "系统导常";
			logger.error("#PinganService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId,e);
		}
		return result;
	}
	
	public String searchRefund(OrderBean orderBean){
		logger.info("#PinganService.refundMoney# orderBean={}",orderBean);
		String result = "FAIL";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			PinganRefundSearchBean reqData = new PinganRefundSearchBean();
			reqData.setService("unified.trade.refundquery");
			reqData.setMch_id(storePayInfo.getMchId());
			reqData.setOut_trade_no(orderBean.getOrderId());
			reqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(10));
			reqData.setSign(Signature.getSign(reqData.toMap(),storePayInfo.getField1()));
			String postDataXML = XMLBeanUtils.mapToXml(reqData.toMap());
			logger.info("#PinganService.payWeChat# postDataXml={}",postDataXML);
			String resultString = new HttpsRequest().sendPost("https://pay.swiftpass.cn/pay/gateway", postDataXML, "", "");
			logger.info("#PinganService.payWeChat# resultString={}",resultString);
			PinganRefundSearchResult resData = PinganRefundSearchResult.getPinganRefundSearchResult(resultString);
			if(!"0".equals(resData.getStatus())){
				result = resData.getMessage();
			}else {
				if(!"0".equals(resData.getResult_code())){
					result = resData.getErr_code();
				}else{
					result = resData.getRefund_status_0();
				}
			}			
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#PinganService.refundMoney# orderBean={}",orderBean,e);
		}
		return result;
	}
	
	public static void main(String[] args) {
		OrderBean orderBean = new OrderBean();
		orderBean.setActualChargeAmount(1);
		orderBean.setOrderId("10674590170503627987206");
		orderBean.setPayMethod("1");
//		new PinganService().refundMoney(orderBean);
		new PinganService().searchRefund(orderBean);
	}
}
