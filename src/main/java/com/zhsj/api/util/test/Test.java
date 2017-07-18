package com.zhsj.api.util.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import org.joda.time.DateTime;

/**
 * Created by lcg on 16/12/29.
 */
public class Test {


    public static void main(String[] args) throws UnsupportedEncodingException{
//    	System.out.println(URLDecoder.decode("1001%2C80%2Ctest3", "utf-8"));
    	DateTime time = DateTime.now();
    	System.out.println(time);
    	System.out.println(time.plusHours(4));
    }
}