package com.zhsj.api.controller;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/refund/")
public class RefundController {
    Logger logger = LoggerFactory.getLogger(RefundController.class);

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
    
    @RequestMapping(value = "/scanCode", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object scanCode(String code,HttpServletRequest request){
    	logger.info("#OrderV2Controller.scanCode# code={}",code);
    	ModelAndView modelAndView = new ModelAndView();
    	if( StringUtils.isEmpty(code) ){
    		 modelAndView.setViewName("error");
 	         return modelAndView;
    	}
    	modelAndView = refundService.scanCode(code, request);
    	return modelAndView;
    }


	@RequestMapping(value = "/getUserOpenId", method =  {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ModelAndView getUserOpenId(HttpServletRequest request) throws Exception {
	    ModelAndView modelAndView = new ModelAndView();
	    String state = request.getParameter("state");
		 
	    String code = request.getParameter("code");
	    String appid = request.getParameter("appid");
	    String auth_code = request.getParameter("auth_code");
	    String app_id = request.getParameter("app_id");
	    if(StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(appid)){
	    	//微信
	    	modelAndView = refundService.getUserOpenId(code, appid, state, 1);
	    }else if(StringUtils.isNotEmpty(auth_code) && StringUtils.isNotEmpty(app_id)){
	    	//支付宝
	    	modelAndView = refundService.getUserOpenId(auth_code, app_id, state, 2);
	    }else{
	    	 modelAndView.setViewName("error");
	    }
	    
	    return modelAndView;
	    
	}

}
