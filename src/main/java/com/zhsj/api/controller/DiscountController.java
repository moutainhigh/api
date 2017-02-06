package com.zhsj.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("/discount")
public class DiscountController {
    Logger logger = LoggerFactory.getLogger(DiscountController.class);


    //微信可以访问到 网页授权域名
    @RequestMapping(value = "/activityDetail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView activityDetail(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/activityDetail");
        return modelAndView;
    }
    
    @RequestMapping(value = "/discountActivity", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView discountActivity(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/discountActivity");
        return modelAndView;
    }
    
    @RequestMapping(value = "/minusFunctionSetting", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView minusFunctionSetting(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/minusFunctionSetting");
        return modelAndView;
    }
    
    @RequestMapping(value = "/selectShop", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView selectShop(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/selectShop");
        return modelAndView;
    }
    
    @RequestMapping(value = "/updateActivity", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView updateActivity(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/updateActivity");
        return modelAndView;
    }
    

}
