package com.zhsj.controller;

import com.alibaba.fastjson.JSON;
import com.zhsj.bean.PayBean;
import com.zhsj.bean.UserBean;
import com.zhsj.service.AbcService;
import com.zhsj.service.MinshengService;
import com.zhsj.service.WXService;
import com.zhsj.util.CommonResult;
import com.zhsj.util.DateUtil;
import com.zhsj.util.MtConfig;
import com.zhsj.util.WebUtils;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public ModelAndView scanCode(@RequestParam("no") String no,HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, List<String>> headers = WebUtils.getHeaders(request);
        List<String> userAgentList = headers.get("user-agent");
        if(userAgentList == null || userAgentList.size() == 0){
            modelAndView.setViewName("error");
        }
        modelAndView.addObject("no", no);
        String userAgent = userAgentList.get(0);
        if(userAgent.indexOf("MicroMessenger") >= 0){
            modelAndView.addObject("appid", MtConfig.getProperty("weChat_appId","wx8651744246a92699"));
            modelAndView.setViewName("pay/wx");
        }else if(userAgent.indexOf("Alipay") >= 0){
            modelAndView.setViewName("pay/zfb");
        }else {
            modelAndView.setViewName("error");
            modelAndView.setViewName("pay/zfb");
        }
        return modelAndView;
    }

    //获取微信openId
    @RequestMapping(value = "/getUserOpenId", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUserOpenId(@RequestParam("code") String code,@RequestParam("state") String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        UserBean userBean = wxService.getUserByCode(code, state);
        if(userBean == null ){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        modelAndView.setViewName("pay/pay");
        modelAndView.addObject("openId", userBean.getOpenId());
        modelAndView.addObject("no",state);
        modelAndView.addObject("payMethod",1);
        logger.info("#PaymentController.getUserOpenId code={},state={},userId={},userId={}#",code,state,userBean.getId(),userBean.getOpenId());
        return modelAndView;
    }

    //获取微信ID
    @RequestMapping(value = "/getBuyerId", method = {RequestMethod.GET,RequestMethod.POST})
    public void getBuyerId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("getBuyerId"+"=====");
        Map<String,Object> map = request.getParameterMap();
        for(String name: map.keySet()){

            logger.info("getBuyerId"+name+"====="+request.getParameter(name));
        }
    }
    
    @RequestMapping(value = "/payMoney2", method = RequestMethod.GET)
    public ModelAndView payMoney2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        PayBean payBean = new PayBean();
        payBean.setOpenId("oFvcxwfZrQxlisYN4yIPbxmOT8KM");
        payBean.setPayMethod(1);
        payBean.setStoreNo("586886032dc");
        payBean.setOrderPrice(0.01);
        if(payBean.getPayMethod() == 1){
            Map<String,String> map = minshengService.payWeChat(payBean);
            if(StringUtils.isEmpty(map.get("return_code"))){
                modelAndView.setViewName("error");
            }
//            request.getSession().setAttribute("hashMap",map);

            modelAndView.setViewName("pay/payMoney");
            modelAndView.addObject(map);
        }else {
            modelAndView.setViewName("error");
        }
       return modelAndView;
//        response.
    }

    @RequestMapping(value = "/payMoney", method = RequestMethod.GET)
    public String payMoney(PayBean payBean,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(payBean.getPayMethod() == 1){
            Map<String,String> map = minshengService.payWeChat(payBean);
            HttpSession session = request.getSession();
            request.getSession().setAttribute("hashMap", map);

            return "pay/wxcommit";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "/payMoney1", method = RequestMethod.GET)
    public String payMoney1() throws Exception {
        return "pay/wxcommit";
    }
    
    
    @RequestMapping(value = "/payNotifyWeChat", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void payNotifyWeChat(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String,Object> map = request.getParameterMap();
    	for(String name: map.keySet()){

            logger.info(name+"====="+map.get(name));
        }

    }
    
    







}
