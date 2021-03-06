package com.zhsj.api.bean;


/*
 * 门店和打印机关系
 */
public class StoreBindPrinterBean {

	private long id;
	private String storeNo;//门店编号
	private String deviceId;//设备id
	private int number;//打印联数
	private String secretKey;//秘钥key
	private int valid;//0 无效 1有效
	private long utime;
	private long ctime;
    //transient 20170407
	private String name;
	
	
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
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public long getUtime() {
		return utime;
	}
	public void setUtime(long utime) {
		this.utime = utime;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
