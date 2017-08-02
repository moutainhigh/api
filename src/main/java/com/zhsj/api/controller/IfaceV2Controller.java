package com.zhsj.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhsj.api.service.IfaceService;
import com.zhsj.api.util.CommonResult;

@Controller
@RequestMapping("/v2")
public class IfaceV2Controller {
    Logger logger = LoggerFactory.getLogger(IfaceV2Controller.class);
    
    @Autowired
    private IfaceService ifaceService;

    /**
     * 扫码接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/micropay", method = RequestMethod.POST)
    @ResponseBody
    public Object micropay(@RequestBody String req){
    	logger.info("#IfaceV2Controller.micropay# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.micropayV2(req);
    	logger.info("#IfaceV2Controller.micropay# req={} result ={}",req,result);
    	return result;
    }
    
    /**
     * 查询接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/commonQuery", method = RequestMethod.POST)
    @ResponseBody
    public Object commonQuery(@RequestBody String req){
    	logger.info("#IfaceV2Controller.commonQuery# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.commonQueryV2(req);
    	logger.info("#IfaceV2Controller.commonQuery# req={} result ={}",req,result);
    	return result;
    }
    
    /**
     * 退款接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/commonRefund", method = RequestMethod.POST)
    @ResponseBody
    public Object commonRefund(@RequestBody String req){
    	logger.info("#IfaceV2Controller.commonRefund# req={}",req);
    	if(StringUtils.isEmpty(req)){
    		return CommonResult.build(2, "参数出错");
    	}
    	CommonResult result = ifaceService.commonRefundV2(req);
    	logger.info("#IfaceV2Controller.commonRefund# req={} result ={}",req,result);
    	return result;
    }
    
}
