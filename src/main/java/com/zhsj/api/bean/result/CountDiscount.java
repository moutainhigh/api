package com.zhsj.api.bean.result;

/**
 * Created by lcg on 17/2/18.
 */
public class CountDiscount {
    double planChargeAmount;
    double actualChargeAmount;
    int total;
    int countUser;

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

    public int getCountUser() {
        return countUser;
    }

    public void setCountUser(int countUser) {
        this.countUser = countUser;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
