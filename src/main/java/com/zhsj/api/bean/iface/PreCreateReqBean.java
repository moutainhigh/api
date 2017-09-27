package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class PreCreateReqBean extends BaseBean{
	private String version;
	private String ins_cd;
	private String mchnt_cd;
	private String random_str;
	private String sign;
	private String order_amt;
	private String mchnt_order_no;
	private String order_type;
	private String goods_des;
	private String openid;
	private String appid;
	private String notify_url;
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
	public String getOrder_amt() {
		return order_amt;
	}
	public void setOrder_amt(String order_amt) {
		this.order_amt = order_amt;
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
	public String getGoods_des() {
		return goods_des;
	}
	public void setGoods_des(String goods_des) {
		this.goods_des = goods_des;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
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
	
	@Override
	public boolean checkNull() {
		if(StringUtils.isEmpty(ins_cd) || StringUtils.isEmpty(mchnt_cd) ){
			return false;
		}
		if(StringUtils.isEmpty(sign)){
			return false;
		}
		if(StringUtils.isEmpty(order_amt) || Double.parseDouble(order_amt) < 0.01){
			return false;
		}
		if(StringUtils.isEmpty(order_type) || StringUtils.isEmpty(openid)){
			return false;
		}
		if(StringUtils.isEmpty(notify_url)){
			return false;
		}
		return true;
	}

	


}
