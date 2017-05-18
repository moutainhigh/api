package com.zhsj.api.bean.refund;

import java.util.Map;

import com.zhsj.api.util.XMLBeanUtils;

public class WeChatRefundSearchResult {
	private String return_code;
	private String return_msg;
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String appid;
	private String mch_id;
	private String refund_status_0;
	private String refund_status_1;
	private String refund_status_2;
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
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
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
	public String getRefund_status_0() {
		return refund_status_0;
	}
	public void setRefund_status_0(String refund_status_0) {
		this.refund_status_0 = refund_status_0;
	}
	public String getRefund_status_1() {
		return refund_status_1;
	}
	public void setRefund_status_1(String refund_status_1) {
		this.refund_status_1 = refund_status_1;
	}
	public String getRefund_status_2() {
		return refund_status_2;
	}
	public void setRefund_status_2(String refund_status_2) {
		this.refund_status_2 = refund_status_2;
	}
	
	public static WeChatRefundSearchResult getWeChatRefundSearchResult(String resultString) throws Exception {
		WeChatRefundSearchResult resData = new WeChatRefundSearchResult();
    	Map<String, String> map = XMLBeanUtils.xmlToMap(resultString);
    	
    	resData.setReturn_code(map.get("return_code"));
    	resData.setReturn_msg(map.get("return_msg"));
        if("SUCCESS".equals(map.get("return_code"))){
        	resData.setResult_code(map.get("result_code"));
        	resData.setErr_code(map.get("err_code"));
        	resData.setErr_code_des(map.get("err_code_des"));
        	if("SUCCESS".equals(map.get("result_code"))){
        		resData.setRefund_status_0(map.get("refund_status_0"));
        		resData.setRefund_status_1(map.get("refund_status_1"));
        		resData.setRefund_status_2(map.get("refund_status_2"));
        	}
        	return resData;
        }else{
        	return resData;
        }
    }
}
