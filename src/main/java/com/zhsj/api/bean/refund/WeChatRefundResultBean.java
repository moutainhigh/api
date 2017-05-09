package com.zhsj.api.bean.refund;

import java.util.Map;

import com.zhsj.api.util.XMLBeanUtils;

public class WeChatRefundResultBean {
	private String return_code;
	private String return_msg;
	
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String appid;
	private String mch_id;
	private String sign;
	private String transaction_id;
	private String out_trade_no;
	private String out_refund_no;
	private String refund_id;
	private String refund_fee;
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public static WeChatRefundResultBean getWeChatRefundResultBean(String resultString) throws Exception {
		WeChatRefundResultBean resData = new WeChatRefundResultBean();
    	Map<String, String> map = XMLBeanUtils.xmlToMap(resultString);
    	
    	resData.setReturn_code(map.get("return_code"));
    	resData.setReturn_msg(map.get("return_msg"));
        if("SUCCESS".equals(map.get("return_code"))){
        	resData.setResult_code(map.get("result_code"));
        	resData.setErr_code(map.get("err_code"));
        	resData.setErr_code_des(map.get("err_code_des"));
        	if("SUCCESS".equals(map.get("result_code"))){
        		resData.setTransaction_id(map.get("transaction_id"));
        		resData.setOut_trade_no(map.get("out_trade_no"));
        		resData.setRefund_id(map.get("refund_id"));
        		resData.setRefund_fee(map.get("refund_fee"));
        	}
        	return resData;
        }else{
        	return resData;
        }
    }
}
