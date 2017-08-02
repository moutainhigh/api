package com.zhsj.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.service.FuyouService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.util.CommonResult;

@Controller
@RequestMapping("fuyou")
public class FuyouController {

	private Logger logger = LoggerFactory.getLogger(FuyouController.class);
	@Autowired
	private FuyouService fuyouService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "queryOrder",method = RequestMethod.POST)
	@ResponseBody
	public Object queryOrder(String orderId){
		logger.info("#queryOrder# orderId = {}", orderId);
		if(StringUtils.isEmpty(orderId)){
			return CommonResult.build(2, "订单号不能为空");
		}
		OrderBean orderBean = orderService.getByOrderId(orderId);
		if(null == orderBean){
			return CommonResult.build(2, "订单查询不到");
		}
		String result = fuyouService.searchOrder(orderBean);
		logger.info("#queryOrder# msg = {}", result);
		return CommonResult.success(result);
	}
}
