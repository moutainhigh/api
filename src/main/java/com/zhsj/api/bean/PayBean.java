package com.zhsj.api.bean;


/**
 * Created by lcg on 17/1/1.
 */
public class PayBean {
    private int payMethod; //支付方式   微信、支付宝

    private String storeNo;    //商家号
    private double orderPrice;  //价格
    private int discountType;   // 优惠类型
    private long discountId;    //  优惠id;
    private double discountPrice; //优惠减金额


    private String openId;  //微信openId
    private String buyerId; //支付宝唯一id

    private String discountIds;

    public long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountIds() {
        return discountIds;
    }

    public void setDiscountIds(String discountIds) {
        this.discountIds = discountIds;
    }

    @Override
    public String toString() {
        return "PayBean{" +
                "payMethod=" + payMethod +
                ", storeNo='" + storeNo + '\'' +
                ", orderPrice=" + orderPrice +
                ", discountType=" + discountType +
                ", discountId=" + discountId +
                ", discountPrice=" + discountPrice +
                ", openId='" + openId + '\'' +
                ", buyerId='" + buyerId + '\'' +
                '}';
    }
}
