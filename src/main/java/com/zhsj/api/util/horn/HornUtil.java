package com.zhsj.api.util.horn;

import java.util.HashMap;
import java.util.Map;

import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.print.CloudPrinter;

public class HornUtil {

	public static void bind(String deviceId){
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("m", "1");
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendGet(CloudHornConst.BIND, paramMap);
		System.err.println(responseStr);
	}
	//id=SPEAKERID&price=PRICEVALUE&token=TOKEN
	public static void msgSubmit(String deviceId, String price){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("price", price);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendPost(CloudHornConst.MSGSUBMIT, paramMap);
		System.err.println(responseStr);
	}
	
	////id=SPEAKERID&vol=VOLVALUE&token=TOKEN
	public static void setVolumne(String deviceId, String vol){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("vol", vol);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendPost(CloudHornConst.SETVOLUMNE, paramMap);
		System.err.println(responseStr);
	}
	
	public static void getRecord(){
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("ids", "335");
		paramMap.put("ide", "335");
		paramMap.put("ts", "20170928133500");
		paramMap.put("te", "");
		paramMap.put("m", "0");
		paramMap.put("p", "0");
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr = HttpClient.sendGet(CloudHornConst.HISTROY, paramMap);
		System.err.println(responseStr);
	}
	
	public static void main(String[] args) {
//		bind("336");
		msgSubmit("335","1120000000");
//		msgSubmit("335","1120");
//		msgSubmit("335","11200");
//		msgSubmit("335","112000");
		
//		setVolumne("335","100");
//		getRecord();
	}
}
