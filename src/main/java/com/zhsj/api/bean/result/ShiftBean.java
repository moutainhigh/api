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
	
	//微信订单
	private int displayWX;
	private double planMoneyWX;
	private double actualMoneyWX;
	private double orgDisMoneyWX;
	private double storeDisMoneyWX;
	private double refundWX;
	//支付宝订单
	private int displayAli;
	private double planMoneyAli;
	private double actualMoneyAli;
	private double orgDisMoneyAli;
	private double storeDisMoneyAli;
	private double refundAli;
	//银联卡订单
	private int displayUCard;
	private double planMoneyUCard;;
	private double actualMoneyUCard;;
	private double orgDisMoneyUCard;;
	private double storeDisMoneyUCard;;
	private double refundUCard;;
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
	public int getDisplayWX() {
		return displayWX;
	}
	public void setDisplayWX(int displayWX) {
		this.displayWX = displayWX;
	}
	public double getPlanMoneyWX() {
		return planMoneyWX;
	}
	public void setPlanMoneyWX(double planMoneyWX) {
		this.planMoneyWX = planMoneyWX;
	}
	public double getActualMoneyWX() {
		return actualMoneyWX;
	}
	public void setActualMoneyWX(double actualMoneyWX) {
		this.actualMoneyWX = actualMoneyWX;
	}
	public double getOrgDisMoneyWX() {
		return orgDisMoneyWX;
	}
	public void setOrgDisMoneyWX(double orgDisMoneyWX) {
		this.orgDisMoneyWX = orgDisMoneyWX;
	}
	public double getStoreDisMoneyWX() {
		return storeDisMoneyWX;
	}
	public void setStoreDisMoneyWX(double storeDisMoneyWX) {
		this.storeDisMoneyWX = storeDisMoneyWX;
	}
	public double getRefundWX() {
		return refundWX;
	}
	public void setRefundWX(double refundWX) {
		this.refundWX = refundWX;
	}
	public int getDisplayAli() {
		return displayAli;
	}
	public void setDisplayAli(int displayAli) {
		this.displayAli = displayAli;
	}
	public double getPlanMoneyAli() {
		return planMoneyAli;
	}
	public void setPlanMoneyAli(double planMoneyAli) {
		this.planMoneyAli = planMoneyAli;
	}
	public double getActualMoneyAli() {
		return actualMoneyAli;
	}
	public void setActualMoneyAli(double actualMoneyAli) {
		this.actualMoneyAli = actualMoneyAli;
	}
	public double getOrgDisMoneyAli() {
		return orgDisMoneyAli;
	}
	public void setOrgDisMoneyAli(double orgDisMoneyAli) {
		this.orgDisMoneyAli = orgDisMoneyAli;
	}
	public double getStoreDisMoneyAli() {
		return storeDisMoneyAli;
	}
	public void setStoreDisMoneyAli(double storeDisMoneyAli) {
		this.storeDisMoneyAli = storeDisMoneyAli;
	}
	public double getRefundAli() {
		return refundAli;
	}
	public void setRefundAli(double refundAli) {
		this.refundAli = refundAli;
	}
	public int getDisplayUCard() {
		return displayUCard;
	}
	public void setDisplayUCard(int displayUCard) {
		this.displayUCard = displayUCard;
	}
	public double getPlanMoneyUCard() {
		return planMoneyUCard;
	}
	public void setPlanMoneyUCard(double planMoneyUCard) {
		this.planMoneyUCard = planMoneyUCard;
	}
	public double getActualMoneyUCard() {
		return actualMoneyUCard;
	}
	public void setActualMoneyUCard(double actualMoneyUCard) {
		this.actualMoneyUCard = actualMoneyUCard;
	}
	public double getOrgDisMoneyUCard() {
		return orgDisMoneyUCard;
	}
	public void setOrgDisMoneyUCard(double orgDisMoneyUCard) {
		this.orgDisMoneyUCard = orgDisMoneyUCard;
	}
	public double getStoreDisMoneyUCard() {
		return storeDisMoneyUCard;
	}
	public void setStoreDisMoneyUCard(double storeDisMoneyUCard) {
		this.storeDisMoneyUCard = storeDisMoneyUCard;
	}
	public double getRefundUCard() {
		return refundUCard;
	}
	public void setRefundUCard(double refundUCard) {
		this.refundUCard = refundUCard;
	}
	
}
