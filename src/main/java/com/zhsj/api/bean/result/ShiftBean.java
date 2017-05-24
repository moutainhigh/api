package com.zhsj.api.bean.result;

public class ShiftBean {
	private String startTime;
	private String endTime;
	private double refundMoney; //退款
	private long refundNum;
	private double actualMoney;//实收
	private double storeDisMoney;//商家优惠
	private long storeDisNum;
	private double orgDisMoney;//平台优惠
	private long orgDisNum;
	private double totalMoney;
	private long totalNum;
	private String name;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public long getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(long refundNum) {
		this.refundNum = refundNum;
	}
	public double getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(double actualMoney) {
		this.actualMoney = actualMoney;
	}
	public double getStoreDisMoney() {
		return storeDisMoney;
	}
	public void setStoreDisMoney(double storeDisMoney) {
		this.storeDisMoney = storeDisMoney;
	}
	public long getStoreDisNum() {
		return storeDisNum;
	}
	public void setStoreDisNum(long storeDisNum) {
		this.storeDisNum = storeDisNum;
	}
	public double getOrgDisMoney() {
		return orgDisMoney;
	}
	public void setOrgDisMoney(double orgDisMoney) {
		this.orgDisMoney = orgDisMoney;
	}
	public long getOrgDisNum() {
		return orgDisNum;
	}
	public void setOrgDisNum(long orgDisNum) {
		this.orgDisNum = orgDisNum;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
