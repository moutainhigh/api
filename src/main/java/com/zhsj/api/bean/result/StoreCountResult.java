package com.zhsj.api.bean.result;

public class StoreCountResult {
	private double planMmount;
	private double actualAmount;
	private int dealCount;
	private int userCount;
	private int refundCount;
	public double getPlanMmount() {
		return planMmount;
	}
	public void setPlanMmount(double planMmount) {
		this.planMmount = planMmount;
	}
	public double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public int getDealCount() {
		return dealCount;
	}
	public void setDealCount(int dealCount) {
		this.dealCount = dealCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getRefundCount() {
		return refundCount;
	}
	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}
	@Override
	public String toString() {
		return "StoreCountResult [planMmount=" + planMmount + ", actualAmount="
				+ actualAmount + ", dealCount=" + dealCount + ", userCount="
				+ userCount + ", refundCount=" + refundCount + "]";
	}
	
	
}
