package com.zhsj.api.bean.result;

public class CheckUpdateResult {
	private int isUpdate;
	private int isForce;
	private String downLoadUrl;
	private String version;
	private String newVersion;
	public int getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}
	public int getIsForce() {
		return isForce;
	}
	public void setIsForce(int isForce) {
		this.isForce = isForce;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	
}
