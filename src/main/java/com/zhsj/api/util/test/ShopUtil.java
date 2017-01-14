package com.zhsj.api.util.test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lcg on 16/12/23.
 */
public class ShopUtil {
    public static void main(String[] args){
        long time = System.currentTimeMillis();
        Calendar ca = Calendar.getInstance();//创建一个日期实例
        ca.setTime(new Date(time));//实例化一个日期
        System.out.println(ca.get(Calendar.DAY_OF_YEAR));
        System.out.println(ca.get(Calendar.YEAR)-2000);
        System.out.println(ca.get(Calendar.SECOND));
    }
}
