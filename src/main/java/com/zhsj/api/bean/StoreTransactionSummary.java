package com.zhsj.api.bean;


public class StoreTransactionSummary {
    
	private String storeName;
	private double actualSumAmount;
	private int orderCount;
	private String time;
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public double getActualSumAmount() {
		return actualSumAmount;
	}
	public void setActualSumAmount(double actualSumAmount) {
		this.actualSumAmount = actualSumAmount;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	
	
	
	
}
