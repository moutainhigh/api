package com.zhsj.api.bean.result;

import com.zhsj.api.bean.DiscountBean;

import java.util.List;

/**
 * Created by lcg on 17/2/7.
 */
public class DiscountPage {

    private int total; // optional
    private int pageNum; // optional
    private int pageSize; // optional
    private List<DiscountBean> list; // optional

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

    public List<DiscountBean> getList() {
        return list;
    }

    public void setList(List<DiscountBean> list) {
        this.list = list;
    }
}
