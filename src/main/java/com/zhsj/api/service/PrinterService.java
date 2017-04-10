package com.zhsj.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.PrinterSecretBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StoreBindPrinterBean;
import com.zhsj.api.dao.TBPrinterSecretDao;
import com.zhsj.api.dao.TBStoreBindPrinterDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.login.LoginUserUtil;
import com.zhsj.api.util.print.CloudPrinter;
import com.zhsj.api.util.print.PrinterUtil;

@Service
public class PrinterService {
    Logger logger = LoggerFactory.getLogger(PrinterService.class);
	
	@Autowired
	private TBStoreBindPrinterDao tbStoreBindPrinterDao;
	@Autowired
	private TBPrinterSecretDao tbPrinterSecretDao;
	
	
	public Object addStoreBindPrinter(StoreBindPrinterBean storeBindPrinterBean){
		logger.info("#PrinterService.addStoreBindPrinter #storeBindPrinterBean={}",storeBindPrinterBean);
		String deviceId = storeBindPrinterBean.getDeviceId();
		StoreBindPrinterBean deBean = tbStoreBindPrinterDao.getByDeviceId(deviceId);
		if(deBean != null){
			return CommonResult.build(2, "该设备已经被使用!");
		}
		StoreBean storeBean = LoginUserUtil.getStore();
		storeBindPrinterBean.setStoreNo(storeBean.getStoreNo());
		try{
		  int code = tbStoreBindPrinterDao.insert(storeBindPrinterBean);
		  if(code == 0){
			  logger.info("#PrinterService.addStoreBindPrinter #fail");
			  return CommonResult.defaultError("fail");
		  }
		  return CommonResult.success("添加成功");
		}catch(Exception e){
			logger.error("#PrinterService.addStoreBindPrinter#",e);
			return CommonResult.defaultError("error");
		}
	}
	
	public Object getSecretKeyList(){
		logger.info("#PrinterService.getSecretKeyList#");
		try {
			List<PrinterSecretBean> list =  tbPrinterSecretDao.getList();
			return CommonResult.success("success", list);
		} catch (Exception e) {
			logger.error("#PrinterService.getSecretKeyList# ",e);
			return CommonResult.defaultError("error");
		}
	}
	
	public Object getStorePrinter(){
		logger.info("#PrinterService.getStorePrinter#");
		StoreBean storeBean = LoginUserUtil.getStore();
		String storeNo = storeBean.getStoreNo();
		try {
	        StoreBindPrinterBean storeBindPrinterBean = tbStoreBindPrinterDao.getByStoreNo(storeNo);
	        if(storeBindPrinterBean != null){
	        	PrinterSecretBean printerSecretBean = tbPrinterSecretDao.getBySecretKey(storeBindPrinterBean.getSecretKey());
	        	storeBindPrinterBean.setName(printerSecretBean.getName());
	        	return CommonResult.success("succes",storeBindPrinterBean);
	        }
	        return CommonResult.build(2, "没有关联打印机");
		} catch(Exception e){
			logger.error("#PrinterService.getStorePrinter# ",e);
			return CommonResult.defaultError("error");
		}
	    
	}
	
	public Object updateStorePrinter(StoreBindPrinterBean storeBindPrinterBean){
		logger.info("#PrinterService.updateStorePrinter#");
		try {
			int code = tbStoreBindPrinterDao.update(storeBindPrinterBean);
			if(code == 0){
				return CommonResult.success("fail");
			}
			return CommonResult.success("更新成功");
		} catch (Exception e) {
			logger.error("#PrinterService.updateStorePrinter# ",e);
			return CommonResult.defaultError("error");
		}
	}
	
	/**
	 * 
	 * @Title: queryState
	 * @Description: 查询打印机状态
	 * @param deviceId  设备id
	 * @param secertKey   设备秘钥
	 * @return http://easyprt.com/jszc
	 * @throws Exception
	 */
	public String queryState(String deviceId,String secertKey) throws Exception{
		logger.info("#PrinterService.queryState# deviceId={},secertKey = {}",deviceId,secertKey);
	   String state = PrinterUtil.queryState(deviceId, secertKey);
	   return state;	
	}
	
	/**
	 * 
	 * @Title: print
	 * @Description: 打印命令
	 * @param deviceId设备id
	 * @param secertKey 秘钥
	 * @param content 字节内容
	 * @return  
	 * @throws Exception
	 */
	public boolean print(String deviceId, String secertKey, byte[] content) throws Exception{
		logger.info("#PrinterService.print# deviceId = {},secertKey = {},content={}",deviceId,secertKey,new String(content));
		// 初始化打印机
		String initial =  CloudPrinter.PRINTER_INIT;
		byte[] initByte = PrinterUtil.hexStringToBytes(initial);
		String printInitial = PrinterUtil.requestPrintPost(deviceId, CloudPrinter.SECRETKEY, initByte);
		Map<String,Object> map = JSON.parseObject(printInitial, Map.class);
		if(!"ok".equals(map.get("state"))){
			return false;
		}
		//发送打印内容
		String result = PrinterUtil.requestPrintPost(deviceId, secertKey, content);
		Map<String,Object> rsmap = JSON.parseObject(result, Map.class);
		if(!"ok".equals(rsmap.get("state"))){
			return false;
		}
		return true;
	}
	
}
