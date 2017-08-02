package com.zhsj.api.bean;

public class StoreItemBean {
	private long id;
	private String storeNo;
	private String itemName;
	private double taxRate;
	private String taxCode;
	private double unitPrice; // 单价(含税)
	private int quantity;//数量
	private double invoiceItemAmount;//开票项目金额
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getInvoiceItemAmount() {
		return invoiceItemAmount;
	}
	public void setInvoiceItemAmount(double invoiceItemAmount) {
		this.invoiceItemAmount = invoiceItemAmount;
	}
	
}	
