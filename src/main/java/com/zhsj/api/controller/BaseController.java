package com.zhsj.api.controller;

import com.zhsj.api.service.AbcService;
import com.zhsj.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
public class BaseController {
    Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private AbcService service;
    @Autowired
    private UserService userService;




    @RequestMapping(value = "/")
    public String riderTrack(Model model, HttpServletRequest request) {
            return "index";
    }

    private String get_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=APPID" +
            "&secret=SECRET&" +
            "code=CODE&grant_type=authorization_code";
    private String get_userinfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";





    //微信可以访问到
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








}
