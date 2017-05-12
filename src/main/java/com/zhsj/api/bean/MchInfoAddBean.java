package com.zhsj.api.bean;

public class MchInfoAddBean {
	
	private String storeName;
	private String shortName;
	private String storeNo;
	private int step;
	//基本信息
	private String province;//省编码
	private String city;//市编码
	private String county;//县
	private String street; //街道
	private String address;
	private double lat;
	private double lon;
	private int businessType;
	private String contactsPeople;
	private String phone;
	private String email;
	private String idCard; //身份证号
	private String intro;
	//结算信息
	private String bankAccount;
	private String bankName;
	private String accountName;
	private String accountIdCard;//开户身份证号码
	private String accountPhone;//开户手机号
	private String wxRate;
	private String aliRate;
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public String getContactsPeople() {
		return contactsPeople;
	}
	public void setContactsPeople(String contactsPeople) {
		this.contactsPeople = contactsPeople;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountIdCard() {
		return accountIdCard;
	}
	public void setAccountIdCard(String accountIdCard) {
		this.accountIdCard = accountIdCard;
	}
	public String getAccountPhone() {
		return accountPhone;
	}
	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}
	public String getWxRate() {
		return wxRate;
	}
	public void setWxRate(String wxRate) {
		this.wxRate = wxRate;
	}
	public String getAliRate() {
		return aliRate;
	}
	public void setAliRate(String aliRate) {
		this.aliRate = aliRate;
	}
	
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	@Override
	public String toString() {
		return "MchInfoAddBean [storeName=" + storeName + ", shortName="
				+ shortName + ", storeNo=" + storeNo + ", step=" + step
				+ ", province=" + province + ", city=" + city + ", county="
				+ county + ", street=" + street + ", address=" + address
				+ ", lat=" + lat + ", lon=" + lon + ", businessType="
				+ businessType + ", contactsPeople=" + contactsPeople
				+ ", phone=" + phone + ", email=" + email + ", idCard="
				+ idCard + ", intro=" + intro + ", bankAccount=" + bankAccount
				+ ", bankName=" + bankName + ", accountName=" + accountName
				+ ", accountIdCard=" + accountIdCard + ", accountPhone="
				+ accountPhone + ", wxRate=" + wxRate + ", aliRate=" + aliRate
				+ "]";
	}

	
	
}
