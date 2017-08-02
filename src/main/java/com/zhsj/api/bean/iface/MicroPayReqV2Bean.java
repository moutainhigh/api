package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class MicroPayReqV2Bean extends BaseBean{
	private String version;
	private String ins_cd;
	private String mchnt_cd;
	private String term_id;
	private String mchnt_order_no;
	private String order_amt;
	private String auth_code;
	private String goods_detail;
	private String addn_inf;
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
	public String getTerm_id() {
		return term_id;
	}
	public void setTerm_id(String term_id) {
		this.term_id = term_id;
	}
	public String getMchnt_order_no() {
		return mchnt_order_no;
	}
	public void setMchnt_order_no(String mchnt_order_no) {
		this.mchnt_order_no = mchnt_order_no;
	}
	public String getOrder_amt() {
		return order_amt;
	}
	public void setOrder_amt(String order_amt) {
		this.order_amt = order_amt;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getGoods_detail() {
		return goods_detail;
	}
	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}
	public String getAddn_inf() {
		return addn_inf;
	}
	public void setAddn_inf(String addn_inf) {
		this.addn_inf = addn_inf;
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
                if("goods_detail".equals(field.getName()) || "addn_inf".equals(field.getName())){
                   continue;
                }
                obj = field.get(this);
                if(obj == null ){
                	return false;
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
                if("goods_detail".equals(field.getName()) || "addn_inf".equals(field.getName()) || "sign".equals(field.getName())){
                   continue;
                }
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
