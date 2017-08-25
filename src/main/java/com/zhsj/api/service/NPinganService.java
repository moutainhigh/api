package com.zhsj.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.refund.PinganRefundBean;
import com.zhsj.api.bean.refund.PinganRefundResultBean;
import com.zhsj.api.bean.refund.PinganRefundSearchBean;
import com.zhsj.api.bean.refund.PinganRefundSearchResult;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.UnicodeUtils;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.npingan.TLinx2Util;
import com.zhsj.api.util.npingan.TLinxAESCoder;
import com.zhsj.api.util.npingan.TLinxSHA1;
import com.zhsj.api.util.wft.HttpsRequest;
import com.zhsj.api.util.wft.RandomStringGenerator;
import com.zhsj.api.util.wft.Signature;

@Service
public class NPinganService {
	
    private static final Logger logger = LoggerFactory.getLogger(NPinganService.class);
    
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;

	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#NPinganService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			// 固定参数
	        TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map
			postmap.put("open_id", storePayInfo.getField1());
			postmap.put("timestamp", new DateUtil().unixTime()+"");
			
			TreeMap<String, String> datamap = new TreeMap<String, String>();    // data参数的map
			datamap.put("out_no", orderBean.getOrderId());
			datamap.put("refund_out_no", "re"+orderBean.getOrderId());
			datamap.put("refund_amount", String.valueOf((int)Arith.mul(price, 100)));
			datamap.put("shop_pass", TLinxSHA1.SHA1("123456"));
			String data = TLinxAESCoder.encrypt(JSON.toJSONString(datamap), storePayInfo.getField2());    // AES加密，并bin2hex
			postmap.put("data", data);
			postmap.put("sign_type", "RSA");
			String resultData = this.postData(postmap,  storePayInfo.getField2(),MtConfig.getProperty("N_PINGAN_PRIVATE_KEY", ""), MtConfig.getProperty("N_PINGAN_URL", "")+"payrefund","RSA");
			logger.info(resultData);
			Map<String, String> resMap = JSON.parseObject(resultData, Map.class);
			if("1".equals(resMap.get("status"))){
				result = "SUCCESS";
			}else {
				result = UnicodeUtils.unicode2String(resMap.get("key"));
			}
		}catch (Exception e) {
			result = "系统导常";
			logger.error("#NPinganService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId,e);
		}
		return result;
	}
	
	
	public String postData( TreeMap<String, String> postmap,String openKey,String privateKey,String url,String type) throws Exception{
		logger.info("#NPinganService.postData# postmap={},url={}",postmap,url);
        /**
         * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
         */
		if("RSA".equals(type)){
			TLinx2Util.handleSignRSA(postmap,openKey,privateKey);
		}else{
	        TLinx2Util.handleSign(postmap,openKey);
		}
        /**
         * 3 请求、响应
         */
        String rspStr = TLinx2Util.handlePost(postmap, url);
        logger.info("#NPinganService.postData# rspStr={}",rspStr);

        /**
         * 4 验签  有data节点时才验签
         */
        JSONObject respObject = JSON.parseObject(rspStr);
        if(!"0".equals(respObject.getString("errcode"))){
        	return "FAIL";
        }
        
        Object dataStr = respObject.get("data");
        if (!rspStr.isEmpty() && ( dataStr != null )) {
            if (TLinx2Util.verifySign(respObject,openKey)) {    // 验签成功
                /**
                 * 5 AES解密，并hex2bin
                 */
                String respData = TLinxAESCoder.decrypt(dataStr.toString(), openKey);
                return respData;
            } else {
                logger.info("#NPinganService.postData# 验签失败");
                return "FAIL";
            }
        } else {
        	logger.info("#NPinganService.postData# 没有返回data数据");
            return "FAIL";
        }
	}
	
	
	public static void main(String[] args) {
		OrderBean orderBean = new OrderBean();
		orderBean.setActualChargeAmount(1);
		orderBean.setOrderId("10674590170503627987206");
		orderBean.setPayMethod("1");
//		new PinganService().refundMoney(orderBean);
	}
}
