package com.zhsj.util;

import java.util.Random;
import java.util.zip.CRC32;

/**
 * Created by lcg on 16/12/28.
 */
public class IDUtil {

    public String getStoreNO(){
        long time = DateUtil.unixTime();
        Random rnd = new Random();
        int num = rnd.nextInt(1000);
        String rd = String.format("%08x%03x",time ,num);
        return rd;
    }

    public String getOrderNO(String StoreNO){
        CRC32 crc32 = new CRC32();
        crc32.update(StoreNO.getBytes());
        long time = DateUtil.unixTime();
        Random rnd = new Random();
        int num = rnd.nextInt(1000);
        String rd = String.format("%08x%03x%09",time ,num,crc32.getValue());
        return rd;
    }


    public static void main(String[] args){
//        System.out.println(Long.toHexString(9223372036854775807L));



        for(int i=0;i<1000;i++){


            long time = DateUtil.unixTime();
            Random rnd = new Random();
            int num = rnd.nextInt(1000);
            String rd = String.format("%08x%03x",time ,num);
            CRC32 crc32 = new CRC32();
            crc32.update(rd.getBytes());
            System.out.println(rd+"=="+String.format("%09x", crc32.getValue()));

        }

//        f47dfdb5   8位+两位随机
    }
}
