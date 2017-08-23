package com.zhsj.api.controller;

import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.RefundService;
import com.zhsj.api.util.CommonResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order/v2")
public class OrderV2Controller {
    Logger logger = LoggerFactory.getLogger(OrderV2Controller.class);

    @Autowired
    OrderService orderService;
    @Autowired
    private RefundService refundService;
    
    
    @RequestMapping(value = "/refundCode", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object refundCode(String accountId){
    	logger.info("#OrderV2Controller.refundCode# accountId={}",accountId);
    	if(StringUtils.isEmpty(accountId)){
    		return CommonResult.defaultError("用户帐号不能为空");
    	}
    	CommonResult result = refundService.refundCode(accountId);
    	logger.info("#OrderV2Controller.refundCode# result accountId={} result={}",accountId,result);
    	return result;
    }
    
    @RequestMapping(value = "/searchByNo", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object searchByNo(String orderId,String transactionId){
    	logger.info("#OrderV2Controller.searchByNo# orderId={},transactionId={}",orderId,transactionId);
    	if(StringUtils.isEmpty(orderId) && StringUtils.isEmpty(transactionId)){
    		return CommonResult.defaultError("参数不正确");
    	}
    	CommonResult result = refundService.searchByNo(orderId, transactionId);
    	logger.info("#OrderV2Controller.searchByNo# return orderId={},transactionId={} result={}",orderId,transactionId,result);
    	return result;
    }
    
    @RequestMapping(value = "/searchByCode", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object searchByCode(String code){
    	logger.info("#OrderV2Controller.searchByCode# code={}",code);
    	if( StringUtils.isEmpty(code)){
    		return CommonResult.defaultError("参数不正确");
    	}
    	CommonResult result = refundService.searchByCode( code);
    	logger.info("#OrderV2Controller.searchByCode# return code={}, result={}",code,result);
    	return result;
    }
    
    
    
    @RequestMapping(value = "/refundMoney", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object refundMoney(String accountId,String price,String orderId){
    	logger.info("#OrderV2Controller.refundMoney# accountId={},price={},orderId={}",accountId,price,orderId);
    	if( StringUtils.isEmpty(accountId) || StringUtils.isEmpty(price) || StringUtils.isEmpty(orderId)){
    		return CommonResult.defaultError("参数不正确");
    	}
    	CommonResult result = refundService.refundMoney(accountId, price, orderId);
    	logger.info("#OrderV2Controller.OrderV2Controller.refundMoney# return accountId={},price={},orderId={},result={}",accountId,price,orderId,result);
    	return result;
    }
}
