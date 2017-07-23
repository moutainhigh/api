package com.zhsj.api.bean.result;

public class ShiftNewBean {
	private String startTime;
	private String endTime;
	private String refundMoney; //退款
	private String refundNum;
	private String actualMoney;//实收
	private String storeDisMoney;//商家优惠
	private String storeDisNum;
	private String orgDisMoney;//平台优惠
	private String orgDisNum;
	private String totalMoney;
	private String totalNum;
	private String name;
	
	//微信订单
	private int displayWX;
	private String planMoneyWX;
	private String actualMoneyWX;
	private String orgDisMoneyWX;
	private String storeDisMoneyWX;
	private String refundWX;
	//支付宝订单
	private int displayAli;
	private String planMoneyAli;
	private String actualMoneyAli;
	private String orgDisMoneyAli;
	private String storeDisMoneyAli;
	private String refundAli;
	//银联卡订单
	private int displayUCard;
	private String planMoneyUCard;;
	private String actualMoneyUCard;;
	private String orgDisMoneyUCard;;
	private String storeDisMoneyUCard;;
	private String refundUCard;
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
	public String getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}
	public String getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(String actualMoney) {
		this.actualMoney = actualMoney;
	}
	public String getStoreDisMoney() {
		return storeDisMoney;
	}
	public void setStoreDisMoney(String storeDisMoney) {
		this.storeDisMoney = storeDisMoney;
	}
	public String getStoreDisNum() {
		return storeDisNum;
	}
	public void setStoreDisNum(String storeDisNum) {
		this.storeDisNum = storeDisNum;
	}
	public String getOrgDisMoney() {
		return orgDisMoney;
	}
	public void setOrgDisMoney(String orgDisMoney) {
		this.orgDisMoney = orgDisMoney;
	}
	public String getOrgDisNum() {
		return orgDisNum;
	}
	public void setOrgDisNum(String orgDisNum) {
		this.orgDisNum = orgDisNum;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
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
	public String getPlanMoneyWX() {
		return planMoneyWX;
	}
	public void setPlanMoneyWX(String planMoneyWX) {
		this.planMoneyWX = planMoneyWX;
	}
	public String getActualMoneyWX() {
		return actualMoneyWX;
	}
	public void setActualMoneyWX(String actualMoneyWX) {
		this.actualMoneyWX = actualMoneyWX;
	}
	public String getOrgDisMoneyWX() {
		return orgDisMoneyWX;
	}
	public void setOrgDisMoneyWX(String orgDisMoneyWX) {
		this.orgDisMoneyWX = orgDisMoneyWX;
	}
	public String getStoreDisMoneyWX() {
		return storeDisMoneyWX;
	}
	public void setStoreDisMoneyWX(String storeDisMoneyWX) {
		this.storeDisMoneyWX = storeDisMoneyWX;
	}
	public String getRefundWX() {
		return refundWX;
	}
	public void setRefundWX(String refundWX) {
		this.refundWX = refundWX;
	}
	public int getDisplayAli() {
		return displayAli;
	}
	public void setDisplayAli(int displayAli) {
		this.displayAli = displayAli;
	}
	public String getPlanMoneyAli() {
		return planMoneyAli;
	}
	public void setPlanMoneyAli(String planMoneyAli) {
		this.planMoneyAli = planMoneyAli;
	}
	public String getActualMoneyAli() {
		return actualMoneyAli;
	}
	public void setActualMoneyAli(String actualMoneyAli) {
		this.actualMoneyAli = actualMoneyAli;
	}
	public String getOrgDisMoneyAli() {
		return orgDisMoneyAli;
	}
	public void setOrgDisMoneyAli(String orgDisMoneyAli) {
		this.orgDisMoneyAli = orgDisMoneyAli;
	}
	public String getStoreDisMoneyAli() {
		return storeDisMoneyAli;
	}
	public void setStoreDisMoneyAli(String storeDisMoneyAli) {
		this.storeDisMoneyAli = storeDisMoneyAli;
	}
	public String getRefundAli() {
		return refundAli;
	}
	public void setRefundAli(String refundAli) {
		this.refundAli = refundAli;
	}
	public int getDisplayUCard() {
		return displayUCard;
	}
	public void setDisplayUCard(int displayUCard) {
		this.displayUCard = displayUCard;
	}
	public String getPlanMoneyUCard() {
		return planMoneyUCard;
	}
	public void setPlanMoneyUCard(String planMoneyUCard) {
		this.planMoneyUCard = planMoneyUCard;
	}
	public String getActualMoneyUCard() {
		return actualMoneyUCard;
	}
	public void setActualMoneyUCard(String actualMoneyUCard) {
		this.actualMoneyUCard = actualMoneyUCard;
	}
	public String getOrgDisMoneyUCard() {
		return orgDisMoneyUCard;
	}
	public void setOrgDisMoneyUCard(String orgDisMoneyUCard) {
		this.orgDisMoneyUCard = orgDisMoneyUCard;
	}
	public String getStoreDisMoneyUCard() {
		return storeDisMoneyUCard;
	}
	public void setStoreDisMoneyUCard(String storeDisMoneyUCard) {
		this.storeDisMoneyUCard = storeDisMoneyUCard;
	}
	public String getRefundUCard() {
		return refundUCard;
	}
	public void setRefundUCard(String refundUCard) {
		this.refundUCard = refundUCard;
	};
	
	
}
