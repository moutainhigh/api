package com.zhsj.api.util.horn;

import java.util.HashMap;
import java.util.Map;

import com.zhsj.api.util.HttpClient;

public class HornUtil {

	public static String bind(String deviceId, String type, String storeNo){
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("m", type);
//		paramMap.put("uid", "AF337099");
		paramMap.put("uid", storeNo);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendGet(CloudHornConst.BIND, paramMap);
		return responseStr;
	}
	//id=SPEAKERID&price=PRICEVALUE&token=TOKEN
	public static String msgSubmit(String deviceId, String price){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("price", price);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendPost(CloudHornConst.MSGSUBMIT, paramMap);
		return responseStr;
	}
	
	////id=SPEAKERID&vol=VOLVALUE&token=TOKEN
	public static String setVolumne(String deviceId, String vol){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("vol", vol);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendPost(CloudHornConst.SETVOLUMNE, paramMap);
		return responseStr;
	}
	
	public static String getLast(String deviceId){
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", deviceId);
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendGet(CloudHornConst.LAST, paramMap);
		return responseStr;
	}
	
	
	public static void bindList(){
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("token", CloudHornConst.TOKEN);
		String responseStr  = HttpClient.sendGet(CloudHornConst.BINDLIST, paramMap);
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
//		bind("335");
//		msgSubmit("335","11200000");
//		msgSubmit("335","1120");
		msgSubmit("335","10000");
//		msgSubmit("335","112000");
		
//		getLast("335");
		
//		bindList();
		
//		setVolumne("335","80");
//		getRecord();
	}
}
