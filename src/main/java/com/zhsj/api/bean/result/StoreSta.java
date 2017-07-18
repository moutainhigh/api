package com.zhsj.api.bean.result;

public class StoreSta {

	private long id;
    private String parentNo;
    private String storeNo;
    private String name;
    private int status;//
    private int transCount;
    private double transSumMoney;
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getParentNo() {
		return parentNo;
	}
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTransCount() {
		return transCount;
	}
	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}
	public double getTransSumMoney() {
		return transSumMoney;
	}
	public void setTransSumMoney(double transSumMoney) {
		this.transSumMoney = transSumMoney;
	}
    
    
}
