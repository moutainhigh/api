package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.AliPayInfo;
import com.zhsj.api.dao.TBAliPayInfoDao;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.ali.AuthToken;
import com.zhsj.api.util.ali.SignUtils;

@Service
public class AliService {
    private static final Logger logger = LoggerFactory.getLogger(AliService.class);
    
    @Autowired
    private TBAliPayInfoDao tbAliPayInfoDao;
    
    public String getAliUserId(String appId,String authCode,String privateKey,String gateWay){
    	logger.info("#AliService.getAliUserId# appId={},authCode={},gateWay={}",appId,authCode,gateWay);
    	try{
    		AuthToken authToken = new AuthToken();
    		authToken.setApp_id(appId);
    		authToken.setMethod("alipay.system.oauth.token");
    		authToken.setFormat("JSON");
    		authToken.setCharset("utf-8");
    		authToken.setSign_type("RSA");
    		authToken.setTimestamp(DateUtil.getCurrentTimeHaveHR());
    		authToken.setVersion("1.0");
    		authToken.setGrant_type("authorization_code");
    		authToken.setCode(authCode);
    		authToken.setSign(new SignUtils().sign(getParamStr(authToken.toMap()), privateKey, "SHA1withRSA")); 
    		logger.info("#AliService.getAliUserId# map={}",authToken.toMap());
    		String resultString = HttpClient.sendGet(gateWay, authToken.toMap());
			logger.info("#AliService.getAliUserId# resultString={}",resultString);
			if(StringUtils.isEmpty(resultString)){
				return "";
			}
			JSONObject jsonObject = JSON.parseObject(resultString);
			jsonObject = (JSONObject) jsonObject.get("alipay_system_oauth_token_response");
			String userId = jsonObject.getString("user_id");
			return userId;
    	}catch (Exception e) {
    		logger.error("#AliService.getAliUserId# appId={},authCode={}",appId,authCode,e);
		}
    	return "";
    }
    
    private String getParamStr(Map<String,String> map) {
    	ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = "";
        if(sb.length() >0){
        	result = sb.subSequence(0, sb.length()-1).toString();
        }else {
			result = sb.toString();
		}
        return result;
	}
    
    public AliPayInfo getAliPayInfoByAppId(String appId){
    	return tbAliPayInfoDao.getByAppId(appId);
    }
    
    public static void main(String[] args) {
    	String gateWay = "https://openapi.alipay.com/gateway.do";
		String key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALgB9WmWXVFqr3UyhH96LRm7OkW+uONcl8pRYGTK0JSnRjdGdqV46fAZ2HmB9KOSUhjrnr0lPsSN/25qURRKd2KZ5toe2ZRx/0M4o9cN798lGhJS6at+5IXEpWHM/hHEAV2MJf3FrkH3FUnG5dOe43EQU3jEvu9jRFN71tApysQlAgMBAAECgYAt/t9Xm2yMM3zUcOJJ80Je0jxIvtn8i2KuAhV0XHYzEnUs0AJMbqMbj3pbNS7vzF2VOSCe6zZ7b3tQIGdQCSU6JKAlvzwEkn98hB6HM6c4CA2moeLL+nHlw2/XAGjtoVQlzGJ5cy8cvsASc1D2k26/v11larQrSHZoOwnue3JQQQJBAP4GdCpwLN7li5HScPWKuYO5Ap3iwP0nzFz90FbBqoNRITPCorlaTWqE01pZawssO3Np0mDwG713o2ZzG02ryL0CQQC5cCj0+lzgs+hp61GfB6v4fQ3W0kFnfGaPumhL81zPVZUBB4jXIAs10nuvKNXljHeOhKAbNjbuYDHrqcD2/KOJAkBbiXfS0xlYON9SbMLHuHWhQcnvmwZwMycxW0T3/lGzgEi2niyFHkVHXRXMOOyZ2haSQE/PHrbgT3xSLqhoFL8BAkAfpHroqpwUn75l7Iil+Von8bbd1EziysB5gBZx91n76x+c6Fs6UjZ3fa4rSm2NWPXBtPHZEtw2uuVp0DKkMqH5AkA71BZZnLVXsVFbv6dHdBkMmnLpUTY8V0dIaTdVNFJpkyUO3YUvbTkKVtvI+MJHcqGgVH8o7Duk9iHPCzTOnCTZ";
		System.out.print(new AliService().getAliUserId("2016090701864634", "995d8dc177e7459f8f0313f965baVX88",key,gateWay));
	}
    
}
