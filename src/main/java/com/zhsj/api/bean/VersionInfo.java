package com.zhsj.api.bean;

public class VersionInfo {
	private long id;
	private String verssion;
	private int isForce;
	private int valid;
	private int utime;
	private int ctime;
	private String downloadUrl;
	private String newUrl;
	private String newVersion;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVerssion() {
		return verssion;
	}
	public void setVerssion(String verssion) {
		this.verssion = verssion;
	}
	public int getIsForce() {
		return isForce;
	}
	public void setIsForce(int isForce) {
		this.isForce = isForce;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public int getUtime() {
		return utime;
	}
	public void setUtime(int utime) {
		this.utime = utime;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getNewUrl() {
		return newUrl;
	}
	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	
}
