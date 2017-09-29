package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PreCreateResBean extends BaseBean{
	private String ins_cd;
	private String mchnt_cd;
	private String random_str;
	private String sign;
	private String order_type;
	private String mchnt_order_no;
	private String wwt_order_no;
	private String sdk_appid;
	private String sdk_timestamp;
	private String sdk_noncestr;
	private String sdk_package;
	private String sdk_signtype;
	private String sdk_sign;
	private String trade_no;

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

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getMchnt_order_no() {
		return mchnt_order_no;
	}

	public void setMchnt_order_no(String mchnt_order_no) {
		this.mchnt_order_no = mchnt_order_no;
	}

	public String getWwt_order_no() {
		return wwt_order_no;
	}

	public void setWwt_order_no(String wwt_order_no) {
		this.wwt_order_no = wwt_order_no;
	}

	public String getSdk_appid() {
		return sdk_appid;
	}

	public void setSdk_appid(String sdk_appid) {
		this.sdk_appid = sdk_appid;
	}

	public String getSdk_timestamp() {
		return sdk_timestamp;
	}

	public void setSdk_timestamp(String sdk_timestamp) {
		this.sdk_timestamp = sdk_timestamp;
	}

	public String getSdk_noncestr() {
		return sdk_noncestr;
	}

	public void setSdk_noncestr(String sdk_noncestr) {
		this.sdk_noncestr = sdk_noncestr;
	}

	public String getSdk_package() {
		return sdk_package;
	}

	public void setSdk_package(String sdk_package) {
		this.sdk_package = sdk_package;
	}

	public String getSdk_signtype() {
		return sdk_signtype;
	}

	public void setSdk_signtype(String sdk_signtype) {
		this.sdk_signtype = sdk_signtype;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getSdk_sign() {
		return sdk_sign;
	}

	public void setSdk_sign(String sdk_sign) {
		this.sdk_sign = sdk_sign;
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
