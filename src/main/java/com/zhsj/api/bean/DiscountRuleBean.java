package com.zhsj.api.bean;

/**
 * Created by lcg on 17/2/5.
 */
public class DiscountRuleBean {
    private long id;
    private long discountId;
    private double expendAmount;
    private double discount1;
    private double discount2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public double getExpendAmount() {
        return expendAmount;
    }

    public void setExpendAmount(double expendAmount) {
        this.expendAmount = expendAmount;
    }

    public double getDiscount1() {
        return discount1;
    }

    public void setDiscount1(double discount1) {
        this.discount1 = discount1;
    }

    public double getDiscount2() {
        return discount2;
    }

    public void setDiscount2(double discount2) {
        this.discount2 = discount2;
    }
}
