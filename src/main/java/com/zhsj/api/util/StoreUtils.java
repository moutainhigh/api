package com.zhsj.api.util;

import java.util.Random;
import java.util.zip.CRC32;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lcg on 17/1/1.
 */
public class StoreUtils {
    public static synchronized String getStoreNO(long seq){
    	seq = seq^674653;
        String rd = String.format("%3s%07d","111",seq);
        return rd;
    }

    public static synchronized String getOrderNO(String storeNo){
    	//8位商家编号+YYMMDD+84600+4位
    	if(StringUtils.isNotEmpty(storeNo)){
    		storeNo = storeNo.length() <= 8 ? storeNo:storeNo.substring(storeNo.length()-8);
    	}
    	String date = DateUtil.getDateFormat("yyMMdd");
    	long time = DateUtil.unixTime() - DateUtil.getTodayStartTime();
    	int min = 1,max = Integer.MAX_VALUE;
    	Random random = new Random();
    	int num = random.nextInt(max)%(max-min+1) + min;
    	String no = (String.valueOf(num)+"000").substring(0, 4);
    	
        String rd = String.format("%s%6s%05d%4s",storeNo ,date,time,no);
        return rd;
    }

    public static void main(String[] args){
    	System.out.println(StoreUtils.getOrderNO("1110674308"));
    	System.out.println(StoreUtils.getStoreNO(345L));
    }
}
