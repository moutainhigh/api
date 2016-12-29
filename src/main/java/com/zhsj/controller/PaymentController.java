package com.zhsj.controller;

import com.zhsj.service.AbcService;
import com.zhsj.util.CommonResult;
import com.zhsj.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private AbcService service;


    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    @ResponseBody
    public void scanCode(@RequestParam("code") String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, List<String>> headers = WebUtils.getHeaders(request);
        List<String> userAgentList = headers.get("user-agent");
        if(userAgentList == null || userAgentList.size() == 0){
            return ;
        }
        String userAgent = userAgentList.get(0);
        if(userAgent.indexOf("MicroMessenger") >= 0){
            //微信
        }else if(userAgent.indexOf("Alipay") >= 0){

        }else {

        }
    }








}
