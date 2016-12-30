package com.zhsj.controller;

import com.zhsj.service.AbcService;
import com.zhsj.service.MinshengService;
import com.zhsj.service.WXService;
import com.zhsj.util.CommonResult;
import com.zhsj.util.WebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PaymentController {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PaymentController.class);
    

    @Autowired
    private WXService wxService;
    @Autowired
    private MinshengService minshengService;


    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView scanCode(@RequestParam("storeNo") String storeNo,HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, List<String>> headers = WebUtils.getHeaders(request);
        List<String> userAgentList = headers.get("user-agent");
        if(userAgentList == null || userAgentList.size() == 0){
            modelAndView.setViewName("error");
        }
        modelAndView.addObject("storeNo", storeNo);
        String userAgent = userAgentList.get(0);
        if(userAgent.indexOf("MicroMessenger") >= 0){
            modelAndView.addObject("appid","wx31626c1d3ab34e12");
            modelAndView.setViewName("wx");
        }else if(userAgent.indexOf("Alipay") >= 0){
            modelAndView.setViewName("zfb");
        }else {
            modelAndView.addObject("appid","wx31626c1d3ab34e12");
            modelAndView.setViewName("wx");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/getUserOpenId", method = RequestMethod.GET)
    @ResponseBody
    public void getUserOpenId(@RequestParam("code") String code,@RequestParam("status") String status) throws Exception {
    	String appId = "wx31626c1d3ab34e12";
    	String secret = "045a93654391302593eb3fcdeecefc54";
        String openId = wxService.getOpenId(code,appId,secret);
    }
    
    @RequestMapping(value = "/payMoney", method = RequestMethod.GET)
    @ResponseBody
    public Object payMoney(@RequestParam("code") String code,@RequestParam("status") String status) throws Exception {
    	String appId = "wx31626c1d3ab34e12";
    	String secret = "045a93654391302593eb3fcdeecefc54";
        String openId = wxService.getOpenId(code,appId,secret);
        return minshengService.WXPay();
    }
    
    
    @RequestMapping(value = "/wxPayNotify", method = RequestMethod.GET)
    @ResponseBody
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String,Object> map = request.getParameterMap();
    	for(String name: map.keySet()){

            logger.info(name+"====="+map.get(name));
        }

    }
    
    







}
