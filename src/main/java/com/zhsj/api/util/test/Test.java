package com.zhsj.api.util.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.zhsj.api.bean.iface.MicroPayReqBean;
import com.zhsj.api.bean.iface.QueryReqBean;
import com.zhsj.api.bean.iface.RefundReqBean;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.wft.RandomStringGenerator;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

import org.joda.time.DateTime;

/**
 * Created by lcg on 16/12/29.
 */
public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException{
//    	MicroPayReqBean reqBean = new MicroPayReqBean();
//    	reqBean.setVersion("1.0");
//    	reqBean.setIns_cd("d7772b0674a318d5");
//    	reqBean.setMchnt_cd("1001");
//    	reqBean.setTerm_id("62");
//    	reqBean.setMchnt_order_no("1");
//    	reqBean.setOrder_amt(1);
//    	reqBean.setAuth_code("130176453852579773");
//    	reqBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
//    	reqBean.setSign(reqBean.sign());
//    	Map<String,Object> parameters = new HashMap();
//    	parameters.put("req", JSON.toJSON(reqBean).toString());
//    	String st = HttpClient.sendPost("http://127.0.0.1:8080/api/micropay", parameters);
//    	System.out.println(st);
    	
//    	QueryReqBean reqBean = new QueryReqBean();
//    	reqBean.setVersion("1.0");
//    	reqBean.setIns_cd("d7772b0674a318d5");
//    	reqBean.setMchnt_cd("1001");
//    	reqBean.setMchnt_order_no("1");
//    	reqBean.setOrder_type("WECHAT");
//    	reqBean.setTransaction_id("4007202001201707262667953653");
//    	reqBean.setWwt_order_no("1001170726034463332");
//    	reqBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
//    	reqBean.setSign(reqBean.sign());
//    	Map<String,Object> parameters = new HashMap();
//    	parameters.put("req", JSON.toJSON(reqBean).toString());
//    	String st = HttpClient.sendPost("http://127.0.0.1:8080/api/commonQuery", parameters);
//    	System.out.println("=====================================");
//    	System.out.println(st);
    	
    	RefundReqBean reqBean = new RefundReqBean();
    	reqBean.setVersion("1.0");
    	reqBean.setIns_cd("d7772b0674a318d5");
    	reqBean.setMchnt_cd("1001");
    	reqBean.setTerm_id("62");
    	reqBean.setOrder_type("WECHAT");
    	reqBean.setRefund_order_no("1001170726034463332");
    	reqBean.setTotal_amt(1);
    	reqBean.setRefund_amt(1);
    	reqBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
    	reqBean.setSign(reqBean.sign());
    	Map<String,Object> parameters = new HashMap();
    	parameters.put("req", JSON.toJSON(reqBean).toString());
    	String st = HttpClient.sendPost("http://127.0.0.1:8080/api/commonRefund", parameters);
    	System.out.println("=====================================");
    	System.out.println(st);
    	
//    	System.out.println(URLDecoder.decode("1001%2C80%2Ctest3", "utf-8"));
    	DateTime time = DateTime.now();
    	System.out.println(time);
    	System.out.println(time.plusHours(4));
    	BigDecimal big = new BigDecimal(12.3333f);
    	big = big.setScale(2,BigDecimal.ROUND_HALF_UP);
    	
    }
}