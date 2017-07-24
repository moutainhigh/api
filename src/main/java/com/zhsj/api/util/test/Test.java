package com.zhsj.api.util.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by lcg on 16/12/29.
 */
public class Test {


    public static void main(String[] args) throws UnsupportedEncodingException{
    	BigDecimal big = new BigDecimal(12.3333f);
    	big = big.setScale(2,BigDecimal.ROUND_HALF_UP);
    	
    	System.out.println(big.toString());
    }
}