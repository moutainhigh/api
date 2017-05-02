package com.zhsj.api.controller;

import com.zhsj.api.service.ModuleService;
import com.zhsj.api.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/module")
public class ModuleController {
    Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Autowired
    private ModuleService moduleService;
    
    @RequestMapping(value = "/getMainMenu", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getMainMenu(String auth){
    	logger.info("#ModuleController.getMainMenu# auth={}",auth);
    	return CommonResult.success("",moduleService.getMainMenu(auth));
    }
    
    @RequestMapping(value = "/getById", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getById(int id,String auth){
    	logger.info("#ModuleController.getById# id={},auth={}",id,auth);
    	return CommonResult.success("",moduleService.getById(id, auth));
    }
    
}
