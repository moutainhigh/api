package com.zhsj.api.service;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.wft.Signature;


@Service
public class GoagoService {
	
    private static final Logger logger = LoggerFactory.getLogger(MinshengService.class);
    
	public void aa(){
		Map<String,String> postmap = new HashMap<String, String>();
		postmap.put("method", "gogo.bill.incrementv.query");
		postmap.put("timestamp", DateUtil.getCurrentTime());
		postmap.put("messageFormat", "json");
		postmap.put("appKey", "360537866757285");
		postmap.put("v", "1.0");
		postmap.put("signMethod", "MD5");
		postmap.put("timeType", "1");
		postmap.put("startCreate", "20171010005959");
		postmap.put("endCreate", "20171010235959");
		postmap.put("shopId", "");
		JSONArray array = new JSONArray();
		array.add("1B3RFCP57GMS8G001GKNO3G1M91IF407");
		postmap.put("shopEntityId",array.toJSONString());
		postmap.put("pageNo", "1");
		postmap.put("pageSize", "30");
		
		postmap.put("sign", getSign("1BLA79TOIUH4UO0034GMGCB3L6NVHCRI", postmap, postmap.get("signMethod")));
		String aa = HttpClient.sendGet("http://api.test.goago.cn/oapi/rest", postmap);
		System.out.println(aa);
		JSONObject jsonObject = JSONObject.parseObject(aa);
		String rescode = jsonObject.getString("rescode");
		if(!"OPEN_SUCCESS".equals(rescode)){
			System.err.println(jsonObject.getString("resmsg"));
			return;
		}
		String hasNext = jsonObject.getString("hasNext");
		JSONArray infos = jsonObject.getJSONArray("billInfoList");
		for(){
			
		}
	}
	
	

	    /**
	     * 签名算法
	     * @param appSecret  签名密钥
	     * @param parms      请求参数
	     * @param signMethod 签名方法
	     * @return 签名
	     */
	    public static String getSign(String appSecret, Map<String, String> params, String signMethod)
	    { // 第一步：检查参数是否已经排序
	        String[] keys = params.keySet().toArray(new String[0]);
	        Arrays.sort(keys);
	        // 第二步：把所有参数名和参数值串在一起
	        StringBuilder signStr = new StringBuilder();
	        for (String key : keys)
	        {
	            String value = params.get(key);
	            if (value != null && !"".equals(value))
	            {
	                signStr.append(key).append("=").append(value).append("&");
	            }
	        }
	        // 第三步：拼接签名密钥
	        signStr.append("key=").append(appSecret);
	        logger.info("服务端的签名之前的字符串:" + signStr.toString());
	        // 第四步：签名
	        if ("MD5".equals(signMethod))
	        {
	            return getMD5(signStr.toString());
	        }
	        else
	        {
	            return getSHA(signStr.toString());
	        }
	    }

	    /**
	     * SHA加密
	     * @param str 待加密数据
	     * @return 加密后数据
	     */
	    public static String getSHA(String str)
	    {
	        return encrypt(str, "SHA");
	    }

	    /**
	     * MD5加密
	     * @param str 待加密数据
	     * @return 加密后数据
	     */
	    public static String getMD5(String str)
	    {
	        return encrypt(str, "MD5");
	    }

	    /**
	     * 加密方法
	     * @param str 待加密数据
	     * @param algorithm 加密类型 MD5，SHA
	     * @return 加密后数据
	     */
	    private static String encrypt(String str, String algorithm)
	    {
	        try
	        {
	            MessageDigest md = MessageDigest.getInstance(algorithm);
	            md.update(str.getBytes("UTF-8"));
	            byte[] digest = md.digest();
	            StringBuffer hexstr = new StringBuffer();
	            String shaHex = "";
	            for (int i = 0; i < digest.length; i++)
	            {
	                shaHex = Integer.toHexString(digest[i] & 0xFF);
	                if (shaHex.length() < 2)
	                {
	                    hexstr.append(0);
	                }
	                hexstr.append(shaHex);
	            }
	            return hexstr.toString().toUpperCase();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	           return "";
	        }
	    }
    
	public static void main(String[] args) {
		new GoagoService().aa();
	}
}
