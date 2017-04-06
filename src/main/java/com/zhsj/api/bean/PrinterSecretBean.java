package com.zhsj.api.bean;
/*
 * 打印机秘钥
 */
public class PrinterSecretBean {

	private int id;
	private String secertKey;//秘钥key
	private String name;//名称
	private String des;//描述
	private int valid;//0 无效 1有效
	private long ctime;//
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSecertKey() {
		return secertKey;
	}
	public void setSecertKey(String secertKey) {
		this.secertKey = secertKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	
	
	
}
