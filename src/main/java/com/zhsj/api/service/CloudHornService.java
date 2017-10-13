package com.zhsj.api.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.horn.HornUtil;

@Service
public class CloudHornService {

	Logger logger = LoggerFactory.getLogger(CloudHornService.class);
	
	
	public Object bind(String deviceId, String storeNo){
		logger.info("#bind# deviceId = {}, storeNo = {}", deviceId, storeNo);
		try {
			String response = HornUtil.bind(deviceId, "1", storeNo);
			Map<String, Object> map = JSON.parseObject(response, Map.class);
			int code =  (int) map.get("errcode");
			String msg = (String) map.get("errmsg");
			if(0 == code){
				return CommonResult.success("绑定成功");
			}else{
				return CommonResult.build(code, msg);
			}
		} catch (Exception e) {
			logger.error("#bind# deviceId = {}, storeNo = {}", deviceId, storeNo, e);
		}
		return CommonResult.defaultError("系统异常");
	}
	
	public Object unBind(String deviceId, String storeNo){
		logger.info("#unBind# deviceId = {}, storeNo = {}", deviceId, storeNo);
		try {
			String response = HornUtil.bind(deviceId, "0", storeNo);
			Map<String, Object> map = JSON.parseObject(response, Map.class);
			int code =  (int) map.get("errcode");
			String msg = (String) map.get("errmsg");
			if(0 == code){
				return CommonResult.success("解绑成功");
			}else{
				return CommonResult.build(code, msg);
			}
		} catch (Exception e) {
			logger.error("#unBind# deviceId = {}, storeNo = {}", deviceId, storeNo, e);
		}
		return CommonResult.defaultError("系统异常");
	}
	
	public Object sendMsg(String deviceId, String money){
		logger.info("#sendMsg# deviceId = {}, money = {}", deviceId, money);
		try {
			String response = HornUtil.msgSubmit(deviceId, money);
			Map<String, Object> map = JSON.parseObject(response, Map.class);
			int code =  (int) map.get("errcode");
			String msg = (String) map.get("errmsg");
			if(0 == code){
				return CommonResult.success("发送成功");
			}else{
				return CommonResult.build(code, msg);
			}
		} catch (Exception e) {
			logger.error("#sendMsg# deviceId = {}, money = {}", deviceId, money, e);
		}
		return CommonResult.defaultError("系统异常");
	}
	
	public Object getVol(String deviceId){
		logger.info("#getVol# deviceId = {}", deviceId);
		try {
			String response = HornUtil.getLast(deviceId);
			Map<String, Object> map = JSON.parseObject(response, Map.class);
			System.err.println(map);
			if(map.get("errcode") != null){
				int code =  (int) map.get("errcode");
				String msg = (String) map.get("errmsg");
				if(0 == code){
					return CommonResult.success("获取音量成功", msg);
				}else{
					return CommonResult.build(code, msg);
				}
			}else{
//				String price = (String) map.get("price");
				int volume = (int) map.get("volume");
				return CommonResult.success("获取音量成功", volume);
			}
		} catch (Exception e) {
			logger.error("#getVol# deviceId = {}", deviceId, e);
		}
		return CommonResult.defaultError("系统异常");
	}
	
	public Object setVol(String deviceId, String vol){
		logger.info("#setVol# deviceId = {}, vol = {}", deviceId, vol);
		try {
			String response = HornUtil.setVolumne(deviceId, vol);
			Map<String, Object> map = JSON.parseObject(response, Map.class);
			int code =  (int) map.get("errcode");
			String msg = (String) map.get("errmsg");
			if(0 == code){
				return CommonResult.success("设置音量成功");
			}else{
				return CommonResult.build(code, msg);
			}
		} catch (Exception e) {
			logger.error("#setVol# deviceId = {}, vol = {}", deviceId, vol, e);
		}
		return CommonResult.defaultError("系统异常");
	}
}
