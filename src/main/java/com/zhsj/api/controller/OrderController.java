package com.zhsj.api.controller;

import com.zhsj.api.service.ModuleService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;
    
    @RequestMapping(value = "/countToday", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object countToday(String auth){
    	logger.info("#ModuleController.countToday# auth={}",auth);
    	return CommonResult.success("",orderService.countStoreToday(auth));
    }
    
    
}
