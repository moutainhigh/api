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
@RequestMapping("/shop")
public class ShopController {

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


    @RequestMapping(value = "/")
    public String riderTrack(Model model, HttpServletRequest request) {
            return "index2";
    }

    private String get_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=APPID" +
            "&secret=SECRET&" +
            "code=CODE&grant_type=authorization_code";
    private String get_userinfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";



    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String,Object> map = request.getParameterMap();
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.write("<HTML>");
        out.write("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.write("  <BODY>");
        for(String name: map.keySet()){
            String value = request.getParameter(name);
            out.println(name + ":" + value + "\n\n");
            if("code".equals(name)){
                String openId = service.getOpenId(request.getParameter("code"));
                out.println("openId" + ":" + openId + "\n\n");
            }
        }


        out.write("  </BODY>");
        out.write("</HTML>");
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String,Object> map = request.getParameterMap();
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.write("<HTML>");
        out.write("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.write("  <BODY>");
        String aa = "";
        String bb = "";
        for(String name: map.keySet()){

            if("aa".equals(name)){
                aa = request.getParameter(name);
            }
            if("bb".equals(name)){
                bb = request.getParameter("bb");
            }

        }

        out.println( service.getUserInfo(aa,bb));
        out.write("  </BODY>");
        out.write("</HTML>");
        out.flush();
        out.close();
    }

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


    @RequestMapping(value = "/getBusinessLine", method = RequestMethod.GET)
    @ResponseBody
    public Object getBusinessLine(Model model) {
        try {
            return CommonResult.build(0, "success", service.abc());
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }






}
