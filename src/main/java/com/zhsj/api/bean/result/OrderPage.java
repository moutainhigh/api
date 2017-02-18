package com.zhsj.api.bean.result;

import com.zhsj.api.bean.OrderBean;

import java.util.List;

/**
 * Created by lcg on 17/2/5.
 */
public class OrderPage {
    private int total; // optional
    private int pageNum; // optional
    private int pageSize; // optional
    private List<OrderBean> list; // optional
    private double totalPrice;
    private double discountPrice;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
