package com.zhsj.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.PinganOrgBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.dao.TBPinganOrgDao;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.UnicodeUtils;
import com.zhsj.api.util.npingan.TLinx2Util;
import com.zhsj.api.util.npingan.TLinxAESCoder;
import com.zhsj.api.util.npingan.TLinxSHA1;

@Service
public class NPinganService {
	
    private static final Logger logger = LoggerFactory.getLogger(NPinganService.class);
    
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;
    @Autowired
    private TBPinganOrgDao tbPinganOrgDao;

	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#NPinganService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			String privateKey = "";
			if(StringUtils.isNotEmpty(storePayInfo.getField6())){
				PinganOrgBean pinganOrgBean = tbPinganOrgDao.getById(Long.parseLong(storePayInfo.getField6()));
				if(pinganOrgBean != null){
					privateKey = pinganOrgBean.getPrivateKey();
				}
			}
			
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
			String resultData = this.postData(postmap,  storePayInfo.getField2(),privateKey, MtConfig.getProperty("N_PINGAN_URL", "")+"payrefund","RSA");
			logger.info(resultData);
			if("FAIL".equals(resultData)){
				return "FAIL";
			}
			
			Map<String, String> resMap = JSON.parseObject(resultData, Map.class);
			if(resMap.get("errcode") != null){
				return UnicodeUtils.unicode2String(resMap.get("msg"));
			}
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
        	return rspStr;
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
	
	 public String orderView(OrderBean orderBean){
    	logger.info("#NPinganService.orderView# orderBean={}",orderBean);
		try{
//			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
//			if(CollectionUtils.isEmpty(storePayInfos)){
//				return "支付类型错误";
//			}
//			StorePayInfo payInfo = storePayInfos.get(0);
			
			String openId = "50652a0b5e532810ed181a94f5368055";
			String openKey = "516333c24f7d6b65187809d7fe00c570";
//			https://mixpayuat4.orangebank.com.cn/mct1/
			
			StorePayInfo payInfo = new StorePayInfo();
//			payInfo.setField1("txafCXQt058248b3230c9081ff90ce80");
//			payInfo.setField2("aG0ck19g2HdthGRdSCfmiloOoGXoOzWZ");
			payInfo.setField1(openId);
			payInfo.setField2(openKey);
			
			// 固定参数
	        TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map
			postmap.put("open_id", payInfo.getField1());
			postmap.put("timestamp", new DateUtil().unixTime()+"");
			
			TreeMap<String, String> datamap = new TreeMap<String, String>();    // data参数的map
			datamap.put("out_no", orderBean.getOrderId());
//				datamap.put("ord_no", "9150235849840956336711807");
			String data = TLinxAESCoder.encrypt(JSON.toJSONString(datamap), payInfo.getField2());    // AES加密，并bin2hex
			postmap.put("data", data);
			
			String resultData = this.postData(postmap,  payInfo.getField2(),"", MtConfig.getProperty("N_PINGAN_URL", "")+"order/view","AES");
			logger.info(resultData);
			if("FAIL".equals(resultData)){
				return "FAIL";
			}
			JSONObject jsonObject = JSON.parseObject(resultData);
			
			if(jsonObject.getInteger("errcode") != null){
				return UnicodeUtils.unicode2String(jsonObject.getString("msg"));
			}
			JSONArray array = jsonObject.getJSONArray("related_order");
			if(array == null || array.size() <= 0){
				return "FAIL";
			}
			JSONObject json = array.getJSONObject(0);
			if(json.getInteger("status") != null && json.getInteger("status") == 1){
				return "SUCCESS";
			}
			
		}catch (Exception e) {
			logger.error("#NPinganService.orderView# orderBean={}",orderBean,e);
		}
		return "FAIL";
    }
	 
	public static void main(String[] args) {
		String openId = "50652a0b5e532810ed181a94f5368055";
		String openKey = "516333c24f7d6b65187809d7fe00c570";
//		https://mixpayuat4.orangebank.com.cn/mct1/
		
		StorePayInfo payInfo = new StorePayInfo();
//		payInfo.setField1("txafCXQt058248b3230c9081ff90ce80");
//		payInfo.setField2("aG0ck19g2HdthGRdSCfmiloOoGXoOzWZ");
		payInfo.setField1(openId);
		payInfo.setField2(openKey);
		payInfo.setField4("Weixin");
		payInfo.setPayMethod("1");
//		payInfo.setField4("AlipayCS");
//		payInfo.setPayMethod("2");
		payInfo.setStoreName("小林");
		OrderBean bean = new OrderBean();
		bean.setOrderId("10674560170828688861173");
		System.out.println(new NPinganService().orderView(bean));
	}
}
