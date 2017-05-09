package com.zhsj.api.bean.refund;

import java.util.Map;

import com.zhsj.api.util.XMLBeanUtils;

public class PinganRefundSearchResult {
	private String status;
	private String message;
	private String result_code;
	private String mch_id;
	private String nonce_str;
	private String err_code;
	private String transaction_id;
	private String out_trade_no;
	private String refund_count;
	private String refund_fee_0;
	private String refund_time_0;
	private String refund_status_0;
	private String refund_fee_1;
	private String refund_time_1;
	private String refund_status_1;
	private String refund_fee_2;
	private String refund_time_2;
	private String refund_status_2;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
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
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
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
	public String getRefund_count() {
		return refund_count;
	}
	public void setRefund_count(String refund_count) {
		this.refund_count = refund_count;
	}
	public String getRefund_fee_0() {
		return refund_fee_0;
	}
	public void setRefund_fee_0(String refund_fee_0) {
		this.refund_fee_0 = refund_fee_0;
	}
	public String getRefund_time_0() {
		return refund_time_0;
	}
	public void setRefund_time_0(String refund_time_0) {
		this.refund_time_0 = refund_time_0;
	}
	public String getRefund_status_0() {
		return refund_status_0;
	}
	public void setRefund_status_0(String refund_status_0) {
		this.refund_status_0 = refund_status_0;
	}
	public String getRefund_fee_1() {
		return refund_fee_1;
	}
	public void setRefund_fee_1(String refund_fee_1) {
		this.refund_fee_1 = refund_fee_1;
	}
	public String getRefund_time_1() {
		return refund_time_1;
	}
	public void setRefund_time_1(String refund_time_1) {
		this.refund_time_1 = refund_time_1;
	}
	public String getRefund_status_1() {
		return refund_status_1;
	}
	public void setRefund_status_1(String refund_status_1) {
		this.refund_status_1 = refund_status_1;
	}
	public String getRefund_fee_2() {
		return refund_fee_2;
	}
	public void setRefund_fee_2(String refund_fee_2) {
		this.refund_fee_2 = refund_fee_2;
	}
	public String getRefund_time_2() {
		return refund_time_2;
	}
	public void setRefund_time_2(String refund_time_2) {
		this.refund_time_2 = refund_time_2;
	}
	public String getRefund_status_2() {
		return refund_status_2;
	}
	public void setRefund_status_2(String refund_status_2) {
		this.refund_status_2 = refund_status_2;
	}
	/**
     * 从RefunQueryResponseString里面解析出退款订单数据
     * @param refundQueryResponseString RefundQuery API返回的数据
     * @return 因为订单数据有可能是多个，所以返回一个列表
     */
    public static PinganRefundSearchResult getPinganRefundSearchResult(String resultString) throws Exception {
    	PinganRefundSearchResult resData = new PinganRefundSearchResult();
    	Map<String, String> map = XMLBeanUtils.xmlToMap(resultString);
    	resData.setStatus(map.get("status"));
    	resData.setMessage(map.get("message"));
        if("0".equals(map.get("status"))){
        	resData.setResult_code(map.get("result_code"));
        	resData.setMch_id(map.get("mch_id"));
        	resData.setNonce_str(map.get("nonce_str"));
        	resData.setErr_code(map.get("err_code"));
        	if("0".equals(map.get("result_code"))){
        		resData.setTransaction_id(map.get("transaction_id"));
        		resData.setOut_trade_no(map.get("out_trade_no"));
        		resData.setRefund_count(map.get("refund_count"));
        		resData.setRefund_fee_0(map.get("refund_fee_0"));
        		resData.setRefund_time_0(map.get("refund_time_0"));
        		resData.setRefund_status_0((map.get("refund_status_0")));
        		resData.setRefund_fee_1(map.get("refund_fee_1"));
        		resData.setRefund_time_1(map.get("refund_time_1"));
        		resData.setRefund_status_1((map.get("refund_status_1")));
        		resData.setRefund_fee_2(map.get("refund_fee_2"));
        		resData.setRefund_time_2(map.get("refund_time_2"));
        		resData.setRefund_status_2((map.get("refund_status_2")));
        	}
        	return resData;
        }else{
        	return resData;
        }
    }
	
}
