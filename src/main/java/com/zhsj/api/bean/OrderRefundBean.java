package com.zhsj.api.bean;

public class OrderRefundBean {
	private long id;
	private String refundNo;
	private double refundMoney;
	private long submitUserId;
	private int submitCtime;
	private long approveUserId;
	private int approveCtime;
	private int status;
	private String refundDes;
	private int utime;
	private int ctime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public long getSubmitUserId() {
		return submitUserId;
	}
	public void setSubmitUserId(long submitUserId) {
		this.submitUserId = submitUserId;
	}
	public int getSubmitCtime() {
		return submitCtime;
	}
	public void setSubmitCtime(int submitCtime) {
		this.submitCtime = submitCtime;
	}
	public long getApproveUserId() {
		return approveUserId;
	}
	public void setApproveUserId(long approveUserId) {
		this.approveUserId = approveUserId;
	}
	public int getApproveCtime() {
		return approveCtime;
	}
	public void setApproveCtime(int approveCtime) {
		this.approveCtime = approveCtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRefundDes() {
		return refundDes;
	}
	public void setRefundDes(String refundDes) {
		this.refundDes = refundDes;
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
	@Override
	public String toString() {
		return "OrderRefundBean [id=" + id + ", refundNo=" + refundNo
				+ ", status=" + status + ", ctime=" + ctime + "]";
	}
	
	
}
