package com.zhsj.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class BaseController {
    Logger logger = LoggerFactory.getLogger(BaseController.class);


    //微信可以访问到 网页授权域名
    @RequestMapping(value = "/accWeixin", method = RequestMethod.GET)
    @ResponseBody
    public void accWeixin(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("=====================");
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            System.out.println("signature:" + signature);
           System.out.println("timestamp:" + timestamp);
            System.out.println("nonce:" + nonce);
             System.out.println("echostr:" + echostr);
            PrintWriter pw = response.getWriter();
            pw.append(echostr);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/accWeChatEvent", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object accWeChatEvent(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("=====================");
            Map<String,Object> map = request.getParameterMap();
            for(String key:map.keySet()){
            	logger.info("accWeChatEvent:"+key+"==="+request.getParameter(key));
            }
            String echostr = request.getParameter("echostr");
            return echostr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




}
