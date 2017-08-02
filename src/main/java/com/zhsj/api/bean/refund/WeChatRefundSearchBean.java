package com.zhsj.api.bean.refund;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhsj.api.util.MD5Util;

public class WeChatRefundSearchBean {
	private String appid;
	private String mch_id;
	private String sub_mch_id;
	private String nonce_str;
	private String sign;
	private String out_trade_no;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
	public String getSub_mch_id() {
		return sub_mch_id;
	}
	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}
	public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String obj;
            try {
                obj = (String)field.get(this);
                if(obj!=null && !"".equals(obj)){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
	
	public static String getSign(Map<String,String> map,String key){
		Logger logger = LoggerFactory.getLogger(WeChatRefundSearchBean.class);
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            list.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        System.out.println(result);
        logger.info("Sign Before MD5:" + result);
        result = (MD5Util.encode(result)).toUpperCase();
        logger.info("Sign Result:" + result);
        System.out.println(result);
        return result;
    }
}
