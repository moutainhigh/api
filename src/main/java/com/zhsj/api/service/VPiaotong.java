package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VPiaotong {
	Logger logger = LoggerFactory.getLogger(VPiaotong.class);
	
	public void getQrCodeByItems(){
		String url = "http://fpkj.testnw.vpiaotong.cn/tp/openapi/getQrCodeByItems.pt";
		
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platformCode", "FPXpQoLA");
        map.put("platformCode", "FPXpQoLA");

		
//        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taxpayerNum", "110101201702071");
        // TODO 请更换请求流水号前缀
//        map.put("invoiceReqSerialNo", prefix + "0000000000000024");
        map.put("buyerName", "购买方名称1");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> listMap = new HashMap<String, String>();
        listMap.put("taxClassificationCode", "1010101020000000000");
        listMap.put("quantity", "1.00");
        listMap.put("unitPrice", "56.64");
        listMap.put("invoiceAmount", "56.64");
        listMap.put("taxRateValue", "0.13");
        list.add(listMap);
        map.put("itemList", list);
//        String content = JsonUtil.toJson(map);
//        System.out.println(content);
//        String buildRequest = new OpenApi(password, "11111111", "DEMO", privateKey).buildRequest(content);
//        System.out.println(buildRequest);
	}
}
