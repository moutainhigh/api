package com.zhsj.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhsj.api.service.IfaceService;
import com.zhsj.api.util.CommonResult;

@Controller
public class IfaceController {
    Logger logger = LoggerFactory.getLogger(IfaceController.class);
    
    @Autowired
    private IfaceService ifaceService;

    /**
     * 扫码接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/micropay", method = RequestMethod.POST)
    @ResponseBody
    public Object micropay(String req){
    	logger.info("#IfaceController.micropay# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.micropay(req);
    	logger.info("#IfaceController.micropay# req={} result ={}",req,result);
    	return result;
    }
    
    /**
     * 查询接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/commonQuery", method = RequestMethod.POST)
    @ResponseBody
    public Object commonQuery(String req){
    	logger.info("#IfaceController.commonQuery# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.commonQuery(req);
    	logger.info("#IfaceController.commonQuery# req={} result ={}",req,result);
    	return result;
    }
    
    /**
     * 退款接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/commonRefund", method = RequestMethod.POST)
    @ResponseBody
    public Object commonRefund(String req){
    	logger.info("#IfaceController.commonRefund# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.commonRefund(req);
    	logger.info("#IfaceController.commonRefund# req={} result ={}",req,result);
    	return result;
    }
    
}
