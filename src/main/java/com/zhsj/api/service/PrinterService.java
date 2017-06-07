package com.zhsj.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.PrinterSecretBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StoreBindPrinterBean;
import com.zhsj.api.bean.StoreSettingsBean;
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.dao.TBPrinterSecretDao;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindPrinterDao;
import com.zhsj.api.dao.TBStoreSettingsDao;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
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
	@Autowired
	private TbOrderDao tbOrderDao;
	@Autowired
	private TBStoreSettingsDao tbStoreSettingsDao;
	@Autowired
	private TbStoreDao tbStoreDao;
	@Autowired
	private TBStoreAccountDao tbStoreAccountDao;
	@Autowired
	private WXService wxService;
	
	
	
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
	
	
	public CommonResult printByOrder(String orderId){
		logger.info("#printByOrder# orderId = {}",orderId);
		try {
			OrderBean orderBean = tbOrderDao.getByOrderId(orderId);
			String storeNo = orderBean.getStoreNo();
			StoreBean storeBean = tbStoreDao.getStoreByNo(storeNo);
			orderBean.setStoreName(storeBean.getName());
			StoreSettingsBean storeSettingsBean = tbStoreSettingsDao.getByStoreNo(storeNo);
			if(storeSettingsBean == null){
				return CommonResult.build(2, "没有关联云打印机");
			}
			if(storeSettingsBean.getCloudPrint() != 1){
				return CommonResult.build(2, "没有关联云打印机");
			}
			StoreBindPrinterBean storeBindPrinterBean = tbStoreBindPrinterDao.getByStoreNo(storeNo);
			if(storeBindPrinterBean  == null){
				return CommonResult.build(2, "没有关联云打印机");
			}
			String deviceId = storeBindPrinterBean.getDeviceId();
			String secretKey = storeBindPrinterBean.getSecretKey();
			// 初始化打印机
			String initial =  CloudPrinter.PRINTER_INIT;
			byte[] initByte = PrinterUtil.hexStringToBytes(initial);
			String printInitial = null;
			printInitial = PrinterUtil.requestPrintPost(deviceId, secretKey, initByte);
			Map<String,Object> map = JSON.parseObject(printInitial, Map.class);
			if(!"ok".equals(map.get("state"))){
				logger.info("#printerByOrder# 打印机没有准备好");
				return CommonResult.build(2, "打印机没有准备好");
			}
			long accountId = orderBean.getAccountId();
			String cashierName = "";
			if(accountId != 0){
				StoreAccountBean storeAccountBean = tbStoreAccountDao.getById(accountId);
				cashierName = storeAccountBean.getName();
			}
			//打印内容
			String result = PrinterUtil.request(deviceId, secretKey, orderBean, cashierName);
			logger.info("#printerByOrder# result = {}", result);
			Map<String,Object> rsmap = JSON.parseObject(result, Map.class);
			if(!"ok".equals(rsmap.get("state"))){
				logger.info("#printerByOrder# 打印机出错了");
				return CommonResult.build(2, "打印机出错了");
			}
		} catch (Exception e) {
			logger.error("#PrinterService.printByOrder# orderId={}",orderId,e);
			return CommonResult.success("系统出错");
		}
		return CommonResult.success("打印完成");
	}
	
	public CommonResult sentShiftMsg(String storeNo,String userId,int startTime,int endTime,String type, String auth){
		logger.info("#CashierController.countShift# storeNO={},userId={},startTime={},endTime={},type={},auth={}",
        		storeNo,userId,startTime,endTime,type,auth);
		try{
			ShiftBean bean = new ShiftBean();
	   		bean.setStartTime(DateUtil.getTime(((long)startTime)*1000));
	   		 bean.setEndTime(DateUtil.getTime(((long)endTime)*1000));
	   		 
	   		 long accountId = Long.parseLong(userId);
	   		 StoreAccountBean storeAccountBean = tbStoreAccountDao.getById(accountId);
	   		 if(storeAccountBean == null){
	   			 return CommonResult.defaultError("用户信息出错");
	   		 }
	   		 bean.setName(storeAccountBean.getName());
	   		 if(StringUtils.isEmpty(storeAccountBean.getName())){
	   			 bean.setName(storeAccountBean.getAccount());
	   		 }
   		 
			 List<Integer> statuses = new ArrayList<>();
			 statuses.add(1);
			 statuses.add(3);
			 statuses.add(4);
			 statuses.add(5);
			 Map<String, Object> countMap = tbOrderDao.countByUserAndTime(storeNo, startTime, endTime, accountId,statuses);
			 Map<String, Object> refundMap = tbOrderDao.countRefundByUserAndTime(storeNo, startTime, endTime, accountId);
			 Map<String, Object> storeMap = tbOrderDao.countStoreDisByUserAndTime(storeNo, startTime, endTime, accountId, statuses);
			 Map<String, Object> orgMap = tbOrderDao.countOrgDisByUserAndTime(storeNo, startTime, endTime, accountId,statuses);

			 bean.setRefundMoney(((BigDecimal)refundMap.get("refundMoney")).doubleValue());
			 bean.setRefundNum((Long)refundMap.get("count"));
			 bean.setStoreDisNum((Long)storeMap.get("count"));
			 bean.setStoreDisMoney(((BigDecimal)storeMap.get("storeDisSum")).doubleValue());
			 bean.setOrgDisNum((Long)orgMap.get("count"));
			 bean.setOrgDisMoney(((BigDecimal)orgMap.get("orgDisSum")).doubleValue());
			 
			 bean.setTotalNum((Long)countMap.get("count"));
			 bean.setTotalMoney(((BigDecimal)countMap.get("planMoney")).doubleValue());
			 
			 bean.setActualMoney(((BigDecimal)countMap.get("actualMoney")).subtract((BigDecimal)refundMap.get("refundMoney")).doubleValue());

			if("1".equals(type)){
				//模板消息
				wxService.sendStoreThift(storeNo, bean);
			}else if("2".equals(type)){
				//云打印
				return printShiftMsg(storeNo,bean);
			}
		}catch (Exception e) {
			logger.error("#CashierController.countShift# storeNO={},userId={},startTime={},endTime={},type={},auth={}",
	        		storeNo,userId,startTime,endTime,type,auth,e);
			return CommonResult.defaultError("系统出错");
		}
		return CommonResult.success("");
	}
	
	private CommonResult printShiftMsg(String storeNo,ShiftBean bean){
		StoreSettingsBean storeSettingsBean = tbStoreSettingsDao.getByStoreNo(storeNo);
		if(storeSettingsBean == null || storeSettingsBean.getCloudPrint() != 1){
			return CommonResult.defaultError("没有配置云打印机");
		}
		StoreBindPrinterBean storeBindPrinterBean = tbStoreBindPrinterDao.getByStoreNo(storeNo);
		if(storeBindPrinterBean == null){
			return CommonResult.defaultError("没有配置云打印机");
		}
		return CommonResult.success("打印成功");
	}
	
}
