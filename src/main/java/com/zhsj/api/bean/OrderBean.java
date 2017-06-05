package com.zhsj.api.bean;

/**
 * Created by lcg on 17/1/1.
 */
public class OrderBean {
    private long id;
    private String orderId;
    private double planChargeAmount;
    private double actualChargeAmount;
    private int status;
    private int discountType;
    private long discountId;
    private int payType;
    private String payMethod;
    private String storeNo;
    private String storeName;
    private String parentStoreNo;
    private long orgId;
    private long userId;
    private long utime;
    private long ctime;
    private String orgIds;
    private long saleId;
    private String discountIds;
    private String refundNo;
    private double refundMoney;
    private int payChannel;
    private long accountId;

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPlanChargeAmount() {
        return planChargeAmount;
    }

    public void setPlanChargeAmount(double planChargeAmount) {
        this.planChargeAmount = planChargeAmount;
    }

    public double getActualChargeAmount() {
        return actualChargeAmount;
    }

    public void setActualChargeAmount(double actualChargeAmount) {
        this.actualChargeAmount = actualChargeAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getParentStoreNo() {
        return parentStoreNo;
    }

    public void setParentStoreNo(String parentStoreNo) {
        this.parentStoreNo = parentStoreNo;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDiscountIds() {
        return discountIds;
    }

    public void setDiscountIds(String discountIds) {
        this.discountIds = discountIds;
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

	public int getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
	}
	

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "OrderBean [orderId=" + orderId + ", planChargeAmount="
				+ planChargeAmount + ", actualChargeAmount="
				+ actualChargeAmount + ", status=" + status + ", storeNo="
				+ storeNo + "]";
	}
    
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
    
    
}
