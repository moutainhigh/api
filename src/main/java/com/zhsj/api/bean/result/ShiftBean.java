package com.zhsj.api.bean.result;

public class ShiftBean {
	private String startTime;
	private String endTime;
	private double refundMoney; //退款
	private int refundNum;
	private double actualMoney;//实收
	private int actualNum;
	private double storeDisMoney;//商家优惠
	private int storeDisNum;
	private double orgDisMoney;//平台优惠
	private int orgDisNum;
	private double totalMoney;
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
	public int getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(int refundNum) {
		this.refundNum = refundNum;
	}
	public double getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(double actualMoney) {
		this.actualMoney = actualMoney;
	}
	public int getActualNum() {
		return actualNum;
	}
	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}
	public double getStoreDisMoney() {
		return storeDisMoney;
	}
	public void setStoreDisMoney(double storeDisMoney) {
		this.storeDisMoney = storeDisMoney;
	}
	public int getStoreDisNum() {
		return storeDisNum;
	}
	public void setStoreDisNum(int storeDisNum) {
		this.storeDisNum = storeDisNum;
	}
	public double getOrgDisMoney() {
		return orgDisMoney;
	}
	public void setOrgDisMoney(double orgDisMoney) {
		this.orgDisMoney = orgDisMoney;
	}
	public int getOrgDisNum() {
		return orgDisNum;
	}
	public void setOrgDisNum(int orgDisNum) {
		this.orgDisNum = orgDisNum;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	
}
