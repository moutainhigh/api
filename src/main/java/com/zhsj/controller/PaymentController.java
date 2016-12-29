package com.zhsj.controller;

import com.zhsj.service.AbcService;
import com.zhsj.service.WXService;
import com.zhsj.util.CommonResult;
import com.zhsj.util.WebUtils;
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

    @Autowired
    private WXService wxService;


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


    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public void getUser(@RequestParam("code") String code,@RequestParam("status") String status) throws Exception {
        wxService.getOpenId(code,appId,secret);
    }







}
