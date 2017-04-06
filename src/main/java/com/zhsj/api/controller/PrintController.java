package com.zhsj.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.StoreBindPrinterBean;
import com.zhsj.api.service.PrinterService;
import com.zhsj.api.util.print.CloudPrinter;
import com.zhsj.api.util.print.Tool;

@Controller
@RequestMapping("print")
public class PrintController {
	
    Logger logger = LoggerFactory.getLogger(PrintController.class);
	
    @Autowired
	private PrinterService printerService;
    
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Object add(StoreBindPrinterBean storeBindPrinterBean){
		logger.info("#PrintController.add# storeBindPrinterBean = {}",storeBindPrinterBean);
		return printerService.addStoreBindPrinter(storeBindPrinterBean);
	}
	
}
