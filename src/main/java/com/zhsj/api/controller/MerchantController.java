package com.zhsj.api.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.Md5;

@Controller
@RequestMapping("/merchant")
public class MerchantController {
	Logger logger = LoggerFactory.getLogger(MerchantController.class);
	@Autowired
	ShopService shopService;
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView index(String appId,HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("./merchant/wx");
            String method = URLDecoder.decode("merchant/login", "utf-8");
            modelAndView.addObject("method", method);
            modelAndView.addObject("state", appId); //获取成功后跳转的地址
            modelAndView.addObject("appid", appId);
        }catch (Exception e){
            logger.error("",e.getMessage(),e);
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView login(String code,String state) {
        logger.info("#MerchantController.login# code={},state={}", code, state);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        try {
            Map<String,String> result = shopService.loginByOpenId(code,state);
            if(ResultStatus.RESULT_ERROR.equals(result.get(ResultStatus.RESULT_KEY))){
                modelAndView.setViewName("error");

            }else if(ResultStatus.NO_REGISTER.equals(result.get(ResultStatus.RESULT_KEY))) {
                modelAndView.setViewName("./merchant/bindWeChat");
                modelAndView.addObject("openId",result.get(ResultStatus.RESULT_VALUE));
                modelAndView.addObject("appId", state);
            }else {
                modelAndView.setViewName("./merchant/index");
                String openId = result.get(ResultStatus.RESULT_VALUE);
                modelAndView.addObject("auth", "21"+openId);
            }
        }catch (Exception e){
            logger.error("#MerchantController.login# code={},state={}#", code, state, e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/bindWeChat" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object bindWeChat(String account,String password,String openId,String appId) {
        logger.info("#MerchantController.bindWeChat# account={},password={},openId={},appId={}", account, password, openId,appId);
        String pw = Md5.encrypt(password);
        int num = shopService.updateOpenId(account, pw, openId,appId);
        if(num > 0){
            return CommonResult.build(0, "",appId);
        }else {
            return CommonResult.build(1, "false");
        }
    }
    
    @RequestMapping(value = "/test" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView test() throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./merchant/index");
        String auth = "o5pmesyob9P9Otj-jl-U3ETnArlY";
        modelAndView.addObject("auth", "21" + auth);
        return modelAndView;
    }
    
    @RequestMapping(value = "/store" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView store(String auth) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./merchant/store");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }
    
    @RequestMapping(value = "/assistant" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView assistant(String auth) throws UnsupportedEncodingException {
    	 ModelAndView modelAndView = new ModelAndView();
         modelAndView.setViewName("./merchant/assistant");
         modelAndView.addObject("auth", auth);
         return modelAndView;
    }
    
    @RequestMapping(value = "/mine" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView mine(String auth) throws UnsupportedEncodingException {
    	 ModelAndView modelAndView = new ModelAndView();
         modelAndView.setViewName("./merchant/mine");
         modelAndView.addObject("auth", auth);
         return modelAndView;
    }
}
