package com.zhsj.api.service;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.WeChatInfoBean;
import com.zhsj.api.bean.refund.WeChatRefundBean;
import com.zhsj.api.bean.refund.WeChatRefundResultBean;
import com.zhsj.api.bean.refund.WeChatRefundSearchBean;
import com.zhsj.api.bean.refund.WeChatRefundSearchResult;
import com.zhsj.api.dao.TBWeChatInfoDao;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.wft.HttpsRequest;
import com.zhsj.api.util.wft.RandomStringGenerator;
import com.zhsj.api.util.wft.Signature;

@Service
public class WeChatService {
	private Logger logger = LoggerFactory.getLogger(WeChatService.class);
	
	@Autowired
	private TbStorePayInfoDao tbStorePayInfoDao;
	@Autowired
	private TBWeChatInfoDao tbWeChatInfoDao;
	
	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#WeChatService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId);
		String result = "Fail";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			String appId = StringUtils.isEmpty(storePayInfo.getSubAppid())?storePayInfo.getAppId():storePayInfo.getSubAppid();
			WeChatInfoBean weChatInfo = tbWeChatInfoDao.getByAppId(appId);
			
			WeChatRefundBean reqData = new WeChatRefundBean();
			reqData.setAppid(appId);
			reqData.setMch_id(weChatInfo.getMchId());
			reqData.setSub_mch_id(storePayInfo.getMchId());
			reqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(10));
			reqData.setOut_trade_no(orderBean.getOrderId());
			reqData.setOut_refund_no("re"+orderBean.getOrderId());
			reqData.setTotal_fee(String.valueOf((int)Arith.mul(orderBean.getActualChargeAmount(), 100)));
			reqData.setRefund_fee(String.valueOf((int)Arith.mul(price, 100)));
			reqData.setRefund_fee_type("CNY");
			reqData.setOp_user_id(String.valueOf(userId));
			reqData.setSign(Signature.getSign(reqData.toMap(),weChatInfo.getAppKey()));
			
			String postDataXML = XMLBeanUtils.mapToXml(reqData.toMap());
			logger.info("#WeChatService.refundMoney# postDataXml={}",postDataXML);
			
			String resultString = new HttpsRequest().sendPost("https://api.mch.weixin.qq.com/secapi/pay/refund", postDataXML,weChatInfo.getCertPath(),weChatInfo.getMchId());
			logger.info("#WeChatService.refundMoney# resultString={}",resultString);
			WeChatRefundResultBean resData = WeChatRefundResultBean.getWeChatRefundResultBean(resultString);
			if(!"SUCCESS".equals(resData.getReturn_code())){
				result = resData.getReturn_msg();
			}else {
				if(!"SUCCESS".equals(resData.getResult_code())){
					result = resData.getErr_code_des();
				}else{
					result = "SUCCESS";
				}
			}
			
		}catch (Exception e) {
			result = "系统错误";
			logger.error("#WeChatService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId,e);
		}
		return result;
	}
	
	public String searchRefund(OrderBean orderBean){
		logger.info("#WeChatService.searchRefund# orderBean={}",orderBean);
		String result = "Fail";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			String appId = StringUtils.isEmpty(storePayInfo.getSubAppid())?storePayInfo.getAppId():storePayInfo.getSubAppid();
			WeChatInfoBean weChatInfo = tbWeChatInfoDao.getByAppId(appId);
			
			WeChatRefundSearchBean reqData = new WeChatRefundSearchBean();
			reqData.setAppid(appId);
			reqData.setMch_id(weChatInfo.getMchId());
			reqData.setSub_mch_id(storePayInfo.getMchId());
			reqData.setNonce_str(RandomStringGenerator.getRandomStringByLength(10));
			reqData.setOut_trade_no(orderBean.getOrderId());
			reqData.setSign(reqData.getSign(reqData.toMap(),weChatInfo.getAppKey()));
			
			String postDataXML = XMLBeanUtils.mapToXml(reqData.toMap());
			logger.info("#WeChatService.searchRefund# postDataXml={}",postDataXML);
			String resultString = new HttpsRequest().sendPost("https://api.mch.weixin.qq.com/pay/refundquery", postDataXML,"","");
			logger.info("#WeChatService.searchRefund# resultString={}",resultString);
			WeChatRefundSearchResult resData = WeChatRefundSearchResult.getWeChatRefundSearchResult(resultString);
			if(!"SUCCESS".equals(resData.getReturn_code())){
				result = resData.getReturn_msg();
			}else {
				if(!"SUCCESS".equals(resData.getResult_code())){
					result = resData.getErr_code_des();
				}else{
					result = resData.getRefund_status_0();
				}
			}
		}catch (Exception e) {
			result = "系统错误";
			logger.error("#WeChatService.searchRefund# orderBean={}",orderBean,e);
		}
		return result;
	}
}
