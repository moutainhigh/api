/**
 * @Filename: TLinxUtil.java
 * @Author caiqf
 * @Date 016-4-12
 */
package com.zhsj.api.util.npingan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;

/**
 * @Class: TLinxUtil.java
 * @Description: TLinx宸ュ叿绫?
 * @Author caiqf
 * @Date 2016-4-12
 */
@SuppressWarnings("all")
public class TLinxUtil {
	// 排序
	public static String sort(Map paramMap) throws Exception {
		String sort = "";
		TLinxMapUtil signMap = new TLinxMapUtil();
		if (paramMap != null) {
			String key;
			for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
				key = (String) it.next();
				String value = ((paramMap.get(key) != null) && (!("".equals(paramMap.get(key).toString())))) ? paramMap.get(key).toString() : "";
				signMap.put(key, value);
			}
			signMap.sort();
			for (Iterator it = signMap.keySet().iterator(); it.hasNext();) {
				key = (String) it.next();
				sort = sort + key + "=" + signMap.get(key).toString() + "&";
			}
			if ((sort != null) && (!("".equals(sort)))) {
				sort = sort.substring(0, sort.length() - 1);
			}
		}
		return sort;
	}


	// 排序
	public static String sortjson(JSONObject jsonMap) throws Exception {
		Map<String ,String > paramMap= JSON.parseObject(jsonMap.toJSONString(), Map.class);
		String sort = "";
		TLinxMapUtil signMap = new TLinxMapUtil();
		if (paramMap != null) {
			String key1;
			for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
				key1 = (String) it.next();
				String value = ((paramMap.get(key1) != null) && (!(""
						.equals(paramMap.get(key1).toString())))) ? paramMap
						.get(key1).toString() : "";
				signMap.put(key1, value);
			}
			signMap.sort();
			for (Iterator it = signMap.keySet().iterator(); it.hasNext();) {
				key1 = (String) it.next();
				sort = sort + key1 + "=" + signMap.get(key1).toString() + "&";
			}
			if ((sort != null) && (!("".equals(sort)))) {
				sort = sort.substring(0, sort.length() - 1);
			}
		}
		return sort;
	}

	// AES加密
	public static String AESEncrypt(String jsonData, String aeskey)
			throws Exception {
		return TLinxAESCoder.encrypt(jsonData, aeskey);
	}

	// AES解密
	public static String AESDecrypt(String data, String aeskey)
			throws Exception {
		return TLinxAESCoder.decrypt(data, aeskey);
	}

}
