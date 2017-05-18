package com.zhsj.api.bean.refund;

import java.util.Map;

import com.zhsj.api.util.XMLBeanUtils;

public class PinganRefundResultBean {
	private String status;
	private String message;
	private String result_code;
	private String err_code;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String transaction_id;
	private String out_trade_no;
	private String refund_id;
	private String refund_channel;
	private String refund_fee;
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
	public String getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	
	
	public String getRefund_channel() {
		return refund_channel;
	}
	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	/**
     * 从RefunQueryResponseString里面解析出退款订单数据
     * @param refundQueryResponseString RefundQuery API返回的数据
     * @return 因为订单数据有可能是多个，所以返回一个列表
     */
    public static PinganRefundResultBean getPinganRefundResultBean(String pinganRefundResultString) throws Exception {
    	PinganRefundResultBean resData = new PinganRefundResultBean();
    	Map<String, String> map = XMLBeanUtils.xmlToMap(pinganRefundResultString);
    	
    	resData.setStatus(map.get("status"));
    	resData.setMessage(map.get("message"));
        if("0".equals(map.get("status"))){
        	resData.setResult_code(map.get("result_code"));
        	resData.setMch_id(map.get("mch_id"));
        	resData.setNonce_str(map.get("nonce_str"));
        	resData.setSign(map.get("sign"));
        	resData.setErr_code(map.get("err_code"));
        	if("0".equals(map.get("result_code"))){
        		resData.setTransaction_id(map.get("transaction_id"));
        		resData.setOut_trade_no(map.get("out_trade_no"));
        		resData.setRefund_id(map.get("refund_id"));
        		resData.setRefund_channel(map.get("refund_channel"));
        		resData.setRefund_fee(map.get("refund_fee"));
        	}
        	return resData;
        }else{
        	return resData;
        }
    }
	
	
}
