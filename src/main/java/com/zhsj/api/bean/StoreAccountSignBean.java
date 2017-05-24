package com.zhsj.api.bean;

public class StoreAccountSignBean {
	private long id;
	private long accountId;
	private String lat;
	private String lon;
	private int status;
	private int ctime;
	private String stroeNo;
	private String imei;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	public String getStroeNo() {
		return stroeNo;
	}
	public void setStroeNo(String stroeNo) {
		this.stroeNo = stroeNo;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
}
