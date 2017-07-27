package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.AuthCache;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.annotations.JsonAdapter;
import com.mysql.fabric.xmlrpc.base.Data;
import com.vpiaotong.openapi.OpenApi;
import com.vpiaotong.openapi.util.Base64Util;
import com.vpiaotong.openapi.util.HttpUtils;
import com.vpiaotong.openapi.util.SecurityUtil;
import com.zhsj.api.bean.PWTSettings;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StoreItemBean;
import com.zhsj.api.bean.StoreSettingsBean;
import com.zhsj.api.dao.TBStoreItemDao;
import com.zhsj.api.dao.TBStoreSettingsDao;
import com.zhsj.api.dao.TBWPTSettingsDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;



@Service
public class VPiaotongService {
	Logger logger = LoggerFactory.getLogger(VPiaotongService.class);
	
	@Autowired
	TBStoreSettingsDao tbStoreSettingsDao;
	@Autowired
	StoreService storeService;
	@Autowired
	TBStoreItemDao tbStoreItemDao;
	@Autowired
	TBWPTSettingsDao tbWPTSettingsDao;
	
	public String getStoreQRCode(String storeNo,String orderNo,double totalPrice){
		logger.info("#VPiaotongService.VPiaotongService# storeNo={},orderNo={},totalPrice={}",storeNo,orderNo,totalPrice);
		String qr = "";
		try{
			StoreSettingsBean storeSettingsBean = tbStoreSettingsDao.getByStoreNo(storeNo);
			if(storeSettingsBean == null || storeSettingsBean.getEleInvoice() != 1){
				logger.info("#VPiaotongService.VPiaotongService# storeNo={},orderNo={},totalPrice={},msg={}",storeNo,orderNo,totalPrice,"不打印发票");
				return qr;
			}
			List<StoreItemBean> itemBeans = tbStoreItemDao.getItems(storeNo);
			if(CollectionUtils.isEmpty(itemBeans)){
				logger.info("#VPiaotongService.VPiaotongService# storeNo={},,orderNo={},totalPrice={},msg={}",storeNo,orderNo,totalPrice,"没有开票项目");
				return qr;
			}
			StoreBean storeBean = storeService.getStoreByNO(storeNo);
			List<PWTSettings> pwtList = tbWPTSettingsDao.getByEn(storeSettingsBean.getIsTest());
			if(CollectionUtils.isEmpty(pwtList)){
				return "";
			}
			return this.getQrCodeByItems(storeBean, itemBeans, totalPrice, orderNo,pwtList.get(0));
		}catch (Exception e) {
			logger.error("#VPiaotongService.VPiaotongService# storeNo={}",storeNo,e);
		}
		return qr;
	}
	
	public String getQrCodeByItems(StoreBean storeBean,List<StoreItemBean> itemList,double totalPrice,String orderNo,PWTSettings pwtSettings){
        logger.info("#VPiaotongService.getQrCodeByItems# storeBean={},totalPrice={},orderNo={}",storeBean.getStoreNo(),totalPrice,orderNo);
		try{
			//明细
	        List<Map> list = new ArrayList<>();
	        StoreItemBean bean = itemList.get(0);
        	Map<String, String> itemMap = new HashMap<>();
    		itemMap.put("itemName", bean.getItemName());
    		itemMap.put("taxRateValue",String.valueOf(Arith.round(bean.getTaxRate(), 2)));
    		itemMap.put("taxClassificationCode", bean.getTaxCode());
    		if(bean.getQuantity() >0 && bean.getUnitPrice()>0){
    			itemMap.put("unitPrice", String.valueOf(Arith.round(bean.getUnitPrice(),2)));
        		itemMap.put("quantity", String.valueOf(bean.getQuantity()));
        		double total = bean.getUnitPrice() * bean.getQuantity();
        		itemMap.put("invoiceItemAmount",String.valueOf(Arith.round(total,2)));
    		}else{
    			itemMap.put("invoiceItemAmount",String.valueOf(Arith.round(totalPrice,2)));
    		}
    		list.add(itemMap);
			
			//信息
			DateTime dateTime = DateTime.now();
	        Map<String, Object> content = new HashMap<>();
	        content.put("taxpayerNum", storeBean.getTaxpayerNum());
	        content.put("enterpriseName", storeBean.getEnterpriseName());
	        content.put("tradeNo", orderNo);
	        content.put("tradeTime",dateTime.toString("yyyy-MM-dd HH:mm:ss"));
	        content.put("invoiceAmount", Arith.round(totalPrice, 2));
	        content.put("allowInvoiceCount", 1);
	        content.put("smsFlag", false);
	        content.put("expireTime", dateTime.plusHours(storeBean.getExpireTime()).toString("yyyy-MM-dd HH:mm:ss"));
	        content.put("itemList", list);
	        String req = JSON.toJSONString(content);
	        
	        
	        
	        String platformPrefix = pwtSettings.getPrefix();
	        String platformCode = pwtSettings.getPlatformCode();
	        String password = pwtSettings.getPlatformPassword();
	        String privateKey = pwtSettings.getPrivateKey();
	        String buildRequest = new OpenApi(password, platformCode, platformPrefix, privateKey).buildRequest(req);
			String url = pwtSettings.getRequestUrl();
	        String response = HttpUtils.postJson(url, buildRequest);
	        Map<String, String> map = JSON.parseObject(response, Map.class);
	        if("0000".equals(map.get("code"))){
	        	String con = map.get("content");
	        	con = SecurityUtil.decrypt3DES(password, con);
	        	map = JSON.parseObject(con, Map.class);
	        	return Base64Util.decode2String(map.get("invoiceUrl"));
	        }
		}catch (Exception e) {
	        logger.error("#VPiaotongService.getQrCodeByItems# storeBean={},totalPrice={},orderNo={}",storeBean.getStoreNo(),totalPrice,orderNo,e);
		}
		return "";
	}
	
	public static void main(String[] args) throws Exception{
		StoreBean  storeBean = new StoreBean();
		storeBean.setTaxpayerNum("110101201702071");
		storeBean.setEnterpriseName("电子票测试新1");
		storeBean.setExpireTime(5);
		
		List<StoreItemBean> itemList = new ArrayList<>();
		StoreItemBean bean = new StoreItemBean();
		bean.setItemName("测试产品1");
		bean.setTaxCode("1010103990000000000");
		bean.setTaxRate(0.04);
		itemList.add(bean);
//		
//		String qr = new VPiaotongService().getQrCodeByItems(storeBean,itemList,110.98,DateUtil.getCurrentTimeHaveHR());
//		System.out.println(qr);
		
//		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//        keyPairGen.initialize(1024);
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        String publicKeyStr = RSAUtil.getKeyString(publicKey);
//        System.out.println("publicKeyString:" + publicKeyStr);
//        String privateKeyStr = RSAUtil.getKeyString(privateKey);
//        System.out.println("privateKeyString:" + privateKeyStr);
		
//		String str = "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";
//		System.out.println(SecurityUtil.decrypt3DES("a08Cv8jwi4baj8aZ4NjV9gf1", str));
	}
}
