package com.zhsj.api.controller;

import com.zhsj.api.service.MchAddService;
import com.zhsj.api.service.ModuleService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.StoreAccountService;
import com.zhsj.api.util.CommonResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cashier")
public class CashierController {
    Logger logger = LoggerFactory.getLogger(CashierController.class);

    @Autowired
    private StoreAccountService storeAccountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModuleService moduleService;
    
    @RequestMapping(value = "/sign", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //新建商户页
    public Object sign(String account,String passwd,String lat,String lon,String auth) {
        logger.info("#CashierController.sign# account={},passwd={},lat={},lon={},auth={}",
        		account,passwd,lat,lon,auth);
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(passwd)){
        	return CommonResult.defaultError("账号密码不能为空");
        }
        return  storeAccountService.signCashier(account, passwd, lat, lon, auth);

    }
    
    @RequestMapping(value = "/countToday", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //统计今天流水
    public Object countToday(String storeNo,String userId,String auth) {
        logger.info("#CashierController.countToday# storeNO={},userId={},auth={}",
        		storeNo,userId,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        return  orderService.countToday(storeNo, userId, auth);
    }
    
    @RequestMapping(value = "/getModel", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //统计今天流水
    public Object getModel(String storeNo,String os,String auth) {
        logger.info("#CashierController.getModel# storeNO={},os={},auth={}",
        		storeNo,os,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(os)){
        	return CommonResult.defaultError("输入信息有误");
        }
        return  moduleService.getAppModule(storeNo, os, auth);
    }
   
    @RequestMapping(value = "/countShift", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //交班统计
    public Object countShift(String storeNo,String userId,String startTime,String endTime, String auth) {
        logger.info("#CashierController.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
        		storeNo,userId,startTime,endTime,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
        	return CommonResult.defaultError("时间信息有误");
        }
        long stime = Long.parseLong(startTime);
        long etime = Long.parseLong(endTime);
        
        return  moduleService.getAppModule(storeNo, os, auth);
    }

}
