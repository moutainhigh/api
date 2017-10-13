package com.zhsj.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhsj.api.service.CloudHornService;
import com.zhsj.api.util.CommonResult;

@RestController
@RequestMapping("cloudHorn")
public class CloudHornController {

	private Logger logger = LoggerFactory.getLogger(CloudHornController.class);
	@Autowired
	private CloudHornService cloudHornService;
	
	@RequestMapping(value="bind")
	public Object bind(String deviceId, String storeNo){
		logger.info("#bind# deviceId = {}, storeNo = {}", deviceId, storeNo);
		if(StringUtils.isEmpty(deviceId)){
			return CommonResult.build(-1, "设备编号不能为空");
		}
		if(StringUtils.isEmpty(storeNo)){
			return CommonResult.build(-1, "绑定编号不能为空");
		}
		return cloudHornService.bind(deviceId, storeNo);
	}
	
	@RequestMapping(value="unBind")
	public Object unBind(String deviceId, String storeNo){
		logger.info("#unBind# deviceId = {}, storeNo = {}", deviceId, storeNo);
		if(StringUtils.isEmpty(deviceId)){
			return CommonResult.build(-1, "设备编号不能为空");
		}
		if(StringUtils.isEmpty(storeNo)){
			return CommonResult.build(-1, "解绑编号不能为空");
		}
		return cloudHornService.unBind(deviceId, storeNo);
	}
	
    @RequestMapping(value="sendMsg")
	public Object sendMsg(String deviceId, String money){
    	logger.info("#sendMsg# deviceId = {}, money = {}", deviceId, money);
    	if(StringUtils.isEmpty(deviceId)){
			return CommonResult.build(-1, "设备编号不能为空");
		}
    	if(StringUtils.isEmpty(money)){
			return CommonResult.build(-1, "金额不能为空");
		}
		return cloudHornService.sendMsg(deviceId, money);
	}
	
    @RequestMapping(value="setVol")
	public Object setVol(String deviceId, String vol){
    	if(StringUtils.isEmpty(deviceId)){
			return CommonResult.build(-1, "设备编号不能为空");
		}
    	if(StringUtils.isEmpty(vol)){
			return CommonResult.build(-1, "音量不能为空");
		}
    	logger.info("#setVol# deviceId = {}, vol = {}", deviceId, vol);
		return cloudHornService.setVol(deviceId, vol);
	}
    
    @RequestMapping(value="getVol")
    public Object getVol(String deviceId){
    	logger.info("#getVol# deviceId = {}", deviceId);
    	if(StringUtils.isEmpty(deviceId)){
			return CommonResult.build(-1, "设备编号不能为空");
		}
    	return cloudHornService.getVol(deviceId);
    }
}
