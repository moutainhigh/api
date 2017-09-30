package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PreCreateResBean extends BaseBean{
	private String ins_cd;
	private String mchnt_cd;
	private String random_str;
	private String sign;
	private String pay_url;
	
//	
//	private String order_type;
//	private String mchnt_order_no;
//	private String wwt_order_no;
//	private String sdk_appid;
//	private String sdk_timestamp;
//	private String sdk_noncestr;
//	private String sdk_package;
//	private String sdk_signtype;
//	private String sdk_sign;
//	private String trade_no;

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

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
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

	@Override
	public boolean checkNull() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
