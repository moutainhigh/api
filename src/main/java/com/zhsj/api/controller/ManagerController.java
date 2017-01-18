package com.zhsj.api.controller;

import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.service.AccountService;
import com.zhsj.api.service.UserService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.MtConfig;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private WXService wxService;
    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/getOpenId")
    @ResponseBody
    public ModelAndView getOpenId(@RequestParam("code") String code,String state) {
        logger.info("#ManagerController.login# code={}", code);
        ModelAndView modelAndView = new ModelAndView();
        String openId = wxService.getOpenId(code);
        if(StringUtils.isEmpty(openId)){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        modelAndView.setViewName(state);
        modelAndView.addObject("openId",openId);
        return modelAndView;
    }

    @RequestMapping(value = "/toBindWX")
    @ResponseBody
    public ModelAndView toLogin(HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./manager/wx");
        String method = URLDecoder.decode("manager/getOpenId", "utf-8");
        modelAndView.addObject("method", method);
        modelAndView.addObject("state","./manager/bindWeChat");
        modelAndView.addObject("appid", MtConfig.getProperty("weChat_appId", "wx8651744246a92699"));
        return modelAndView;
    }

    @RequestMapping(value = "/bindWeChat")
    @ResponseBody
    public Object bindWeChat(String account,String password,String openId) {
        logger.info("#ManagerController.bindWeChat# account={},password={},openId={}",account,password,openId);
        int num = accountService.updateOpenId(account, password, openId);
        if(num > 0){
            return CommonResult.build(0, "");
        }else {
            return CommonResult.build(1, "false");
        }
    }



    @RequestMapping(value = "/toMersettled")
    @ResponseBody
    public ModelAndView toMersettled(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./manager/wx");
        String method = URLDecoder.decode("manager/getOpenId","utf-8");
        modelAndView.addObject("appid", MtConfig.getProperty("weChat_appId","wx8651744246a92699"));
        modelAndView.addObject("method", method);
        modelAndView.addObject("state","./shop/mersettled");
        return modelAndView;
    }

}
