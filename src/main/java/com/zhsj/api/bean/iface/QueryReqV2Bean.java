package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class QueryReqV2Bean extends BaseBean{
	private String version;
	private String ins_cd;
	private String mchnt_cd;
	private String mchnt_order_no;
	private String order_type;
	private String transaction_id;
	private String wwt_order_no;
	private String random_str;
	private String sign;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIns_cd() {
		return ins_cd;
	}

	public void setIns_cd(String ins_cd) {
		this.ins_cd = ins_cd;
	}

	public String getMchnt_cd() {
		return mchnt_cd;
	}

	public void setMchnt_cd(String mchnt_cd) {
		this.mchnt_cd = mchnt_cd;
	}

	public String getMchnt_order_no() {
		return mchnt_order_no;
	}

	public void setMchnt_order_no(String mchnt_order_no) {
		this.mchnt_order_no = mchnt_order_no;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getWwt_order_no() {
		return wwt_order_no;
	}

	public void setWwt_order_no(String wwt_order_no) {
		this.wwt_order_no = wwt_order_no;
	}

	public String getRandom_str() {
		return random_str;
	}

	public void setRandom_str(String random_str) {
		this.random_str = random_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public boolean checkNull(){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if("transaction_id".equals(field.getName()) || "wwt_order_no".equals(field.getName()) 
                		|| "mchnt_order_no".equals(field.getName())){
                    continue;
                }
                
                if( obj instanceof String && StringUtils.isEmpty((String)obj)){
                	return false;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
	
	@Override
	public Map<String, Object> toMap() {
		Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
        	Object obj;
            try {
                obj = field.get(this);
                map.put(field.getName(), obj);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
	}
	
	
	 
	
}
