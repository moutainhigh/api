package com.zhsj.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhsj.api.bean.StoreBindPrinterBean;
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.service.PrinterService;

@Controller
@RequestMapping("printer")
public class PrinterController {
	
    Logger logger = LoggerFactory.getLogger(PrinterController.class);
	
    @Autowired
	private PrinterService printerService;
    
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(StoreBindPrinterBean storeBindPrinterBean, String auth){
		logger.info("#PrinterController.add# storeBindPrinterBean = {},auth = {}",
				storeBindPrinterBean,auth);
		return printerService.addStoreBindPrinter(storeBindPrinterBean);
	}
	
	@RequestMapping(value = "getSecertKeyList",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Object getSecertKeyList(String auth){
		logger.info("#PrinterController.getSecertKeyList#auth = {}", auth);
		return printerService.getSecretKeyList();
	}
	
	@RequestMapping(value = "getStorePrinter", method = RequestMethod.POST)
	@ResponseBody
	public Object getStorePrinter(String auth){
		logger.info("#PrinterController.getStorePrinter# auth = {}", auth);
		return printerService.getStorePrinter();
	}
	
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(StoreBindPrinterBean storeBindPrinterBean, String auth){
		logger.info("#PrinterController.updateStorePrinter# storeBindPtinerbean = {}, auth = {}",
				storeBindPrinterBean, auth);
		return printerService.updateStorePrinter(storeBindPrinterBean);
	}
	@RequestMapping(value = "test")
	@ResponseBody
	public Object test(String orderId){
		return printerService.printByOrder(orderId);
	}
	
	@RequestMapping(value = "ts")
	@ResponseBody
	public Object testShift(String storeNo){
		storeNo = "10001";
		ShiftBean shiftBean = new ShiftBean();
		shiftBean.setActualMoney(10.00);
		shiftBean.setStartTime("2017-06-05 17:26:30");
		shiftBean.setEndTime("2017-06-16 12:30:56");
		shiftBean.setName("hctym");
		shiftBean.setOrgDisMoney(0.00);
		shiftBean.setOrgDisNum(0);
		shiftBean.setStoreDisMoney(0.00);
		shiftBean.setStoreDisNum(0);
		shiftBean.setRefundMoney(1.00);
		shiftBean.setRefundNum(1);
		shiftBean.setTotalMoney(10.00);
		shiftBean.setTotalNum(10);
		return printerService.printerByShift(storeNo, shiftBean);
	}
}
