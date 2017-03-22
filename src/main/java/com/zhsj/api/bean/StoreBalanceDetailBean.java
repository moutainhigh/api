package com.zhsj.api.bean;

import java.math.BigDecimal;
/**
 * 营销账户的流水
 */
public class StoreBalanceDetailBean {

	private long id;
	private String storeNo;//商家编号
	private int type;//1收入2支出
	private BigDecimal price;//钱
	private String description;//业务描述
	private int paymentStatus;//支出状态1初始2成功3失败
	private String partnerTradeNo;//支出订单号
	private String paymentNo;//wx订单号
	private int paymentTime;//微信支付成功时间
	private int utime;
	private int ctime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}
	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public int getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(int paymentTime) {
		this.paymentTime = paymentTime;
	}
	public int getUtime() {
		return utime;
	}
	public void setUtime(int utime) {
		this.utime = utime;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	
	
	
	
	
}
