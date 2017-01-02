package com.zhsj.util;

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
        long time = DateUtil.unixTime();
        Random rnd = new Random();
        int num = rnd.nextInt(1000);
        String rd = String.format("%08x%03x%09x",time ,num,crc32.getValue());
        return rd;
    }

    public static void main(String[] args){
        System.out.println(getStoreNO());
    }
}
