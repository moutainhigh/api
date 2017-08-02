package com.zhsj.api.bean.iface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QueryResBean extends BaseBean{
	private String order_type;
	private String ins_cd;
	private String mchnt_cd;
	private String term_id;
	private String random_str;
	private String sign;
	private String trans_stat;
	private int order_amount;
	private int actual_amount;
	private String transaction_id;
	private String wwt_order_no;
	private String mcnnt_order_no;

	
	
	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
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

	public String getTrans_stat() {
		return trans_stat;
	}

	public void setTrans_stat(String trans_stat) {
		this.trans_stat = trans_stat;
	}

	public int getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(int order_amount) {
		this.order_amount = order_amount;
	}

	public int getActual_amount() {
		return actual_amount;
	}

	public void setActual_amount(int actual_amount) {
		this.actual_amount = actual_amount;
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

	public String getMcnnt_order_no() {
		return mcnnt_order_no;
	}

	public void setMcnnt_order_no(String mcnnt_order_no) {
		this.mcnnt_order_no = mcnnt_order_no;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
        	Object obj;
            try {
                obj = field.get(this);
                if("term_id".equals(field.getName())){
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

	@Override
	public boolean checkNull() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
