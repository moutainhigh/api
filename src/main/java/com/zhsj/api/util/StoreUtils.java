package com.zhsj.api.util;

import java.util.Random;
import java.util.zip.CRC32;

/**
 * Created by lcg on 17/1/1.
 */
public class StoreUtils {
    public static String getStoreNO(){
        long time = DateUtil.unixTime();
        Random rnd = new Random();
        int num = rnd.nextInt(1000);
        String rd = String.format("%08x%03x",time ,num);
        return rd;
    }

    public static String getOrderNO(String StoreNO){
        CRC32 crc32 = new CRC32();
        crc32.update(StoreNO.getBytes());
        String date = DateUtil.getCurrentTime();
        Random rnd = new Random();
        int num = rnd.nextInt(1000);
        String rd = String.format("%14s%03xSN%09x",date ,num,crc32.getValue());
        return rd;
    }

    public static void main(String[] args){
        System.out.println(getStoreNO());
        System.out.println(getOrderNO("587058c02e2"));
    }
}
