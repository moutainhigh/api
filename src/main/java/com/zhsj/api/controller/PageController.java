package com.zhsj.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {

	@RequestMapping(value = "/wechat")
	public String wechat(){
		return "pay/weChatPay";
	}
	
	@RequestMapping(value = "ali")
	public String ali(){
		return "pay/aliPay";
	}
	
	@RequestMapping(value = "paySuccess")
	public String paySuccess(){
		return "pay/paySuccess";
	}
	
	@RequestMapping(value = "realnameauth")
	public String realnameauth(){
		return "manager/realnameauth";
	}
	
	@RequestMapping(value = "settlement")
	public String settlement(){
		return "manager/settlement";
	}
	
	@RequestMapping(value = "auditStatus")
	public String auditStatus(){
		return "manager/auditStatus";
	}
	
	@RequestMapping(value = "checkData")
	public String tt(){
		return "discount/activityDetail";
	}
	
	@RequestMapping(value = "test")
	public String test(){
		return "test";
	}
	
	@RequestMapping(value = "reduceSetting")
	public String reduceSet(){
		return "discount/reduceSetting";
	}
}
