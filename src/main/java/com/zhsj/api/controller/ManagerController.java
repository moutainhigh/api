package com.zhsj.api.controller;

import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.service.AccountService;
import com.zhsj.api.service.ManagerService;
import com.zhsj.api.service.UserService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.MtConfig;
import org.apache.commons.codec.binary.Base64;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private ManagerService managerService;
    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView index(String appId,HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("./manager/wx");
            String method = URLDecoder.decode("manager/login", "utf-8");
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
        logger.info("#ManagerController.login# code={},state={}", code,state);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        try {
            Map<String,String> result = managerService.loginByOpenId(code,state);
            if(ResultStatus.RESULT_ERROR.equals(result.get(ResultStatus.RESULT_KEY))){
                modelAndView.setViewName("error");
            }else if(ResultStatus.NO_REGISTER.equals(result.get(ResultStatus.RESULT_KEY))) {
                modelAndView.setViewName("./manager/bindWeChat");
                modelAndView.addObject("openId", result.get(ResultStatus.RESULT_VALUE));
                modelAndView.addObject("appId",state);
            }else {
                modelAndView.setViewName("./manager/index");
                String openId = result.get(ResultStatus.RESULT_VALUE);
                modelAndView.addObject("auth", "11"+openId);
            }
        }catch (Exception e){
            logger.error("#ManagerController.login# code={},state={}#", code, state, e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/test" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView test() throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./manager/index");
        String auth = "oFvcxwfZrQxlisYN4yIPbxmOT8KM";
        modelAndView.addObject("auth", "11" + auth);
        return modelAndView;
    }


    @RequestMapping(value = "/bindWeChat" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object bindWeChat(String account,String password,String openId,String appId) {
        logger.info("#ManagerController.bindWeChat# account={},password={},openId={},appId={}", account, password, openId,appId);
        int num = accountService.updateOpenId(account, password, openId,appId);
        if(num > 0){
            return CommonResult.build(0, "",appId);
        }else {
            return CommonResult.build(1, "false");
        }
    }

    @RequestMapping(value = "/newInsert", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView newInsert(String auth) {
        logger.info("#ManagerController.newInsert# ");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./manager/newInsert");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }

    @RequestMapping(value = "/realnameauth" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object realnameauth(String storeName,String storeAccount,String storeNo,String auth) {
        logger.info("#ManagerController.realnameauth# storeName={},storeAccount={},storeNo={},auth={}",
                storeName, storeAccount, storeNo, auth);
        String result = managerService.realnameauth(storeName, storeAccount, storeNo, auth);
        Map<String,String> map = new HashMap<>();
        map.put("auth", auth);
        map.put("storeNo", storeNo);
        if("SUCCESS".equals(result)){
            map.put("url","./toRealnameauth");
            return CommonResult.build(0, "", map);
        }if("PROC:1".equals(result)) {
            map.put("url","./toRealnameauth");
            return CommonResult.build(0, "", map);
        }else if("PROC:2".equals(result)){
            map.put("url","./toSettlement");
            return CommonResult.build(0, "", map);
        } else {
            return CommonResult.build(1, result);
        }
    }
    @RequestMapping(value = "/settlement" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object settlement( String cityCode,String address,String businessType,String auth,String storeNo){
        logger.info("#ManagerController.settlement# cityCode={},address={},businessType={},auth={},storeNo={}",
                cityCode, address, businessType, auth, storeNo);
        String result = managerService.settlement( cityCode, address, businessType, auth, storeNo);
        Map<String,String> map = new HashMap<>();
        map.put("auth", auth);
        map.put("storeNo",storeNo);
        if("SUCCESS".equals(result)){
            map.put("url","./toSettlement");
            return CommonResult.build(0, "", map);
        } else {
            return CommonResult.build(1, result);
        }
    }

    @RequestMapping(value = "/auditStatus" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object auditStatus(String saName,String saNum,String saBankName,String merEmail,String auth,
    		String storeNo,String settlementType,String rate,String idCard,String phone) {
        logger.info("#ManagerService.auditStatus# saName={},saNum={},saBankName={},merEmail={},auth={},storeNO={},settlementType={},rate={},idCard={},phone={}",
                saName, saNum, saBankName, merEmail, auth, storeNo,settlementType,rate, idCard, phone);
        String result = managerService.auditStatus(saName, saNum, saBankName, merEmail, auth, storeNo,settlementType,rate, idCard, phone);
        Map<String,String> map = new HashMap<>();
        map.put("auth", auth);
        map.put("storeNo", storeNo);
        if("SUCCESS".equals(result)){
            map.put("url","./toAuditStatus");
            return CommonResult.build(0, "", map);
        } else {
            return CommonResult.build(1, result);
        }
    }


    @RequestMapping(value = "/toRealnameauth" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toRealnameauth(String auth,String storeNo) {
        logger.info("#ManagerController.toRealnameauth# ");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("storeNo", storeNo);
        modelAndView.setViewName("./manager/realnameauth");
        return modelAndView;
    }

    @RequestMapping(value = "/toSettlement" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toSettlement(String auth,String storeNo) {
        logger.info("#ManagerController.toSettlement# ");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("storeNo",storeNo);
        modelAndView.setViewName("./manager/settlement");
        return modelAndView;
    }

    @RequestMapping(value = "/toAuditStatus" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toAuditStatus(String auth,String storeNo) {
        logger.info("#ManagerController.toAuditStatus# ");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("storeNo",storeNo);
        modelAndView.setViewName("./manager/auditStatus");
        return modelAndView;
    }

    @RequestMapping(value = "/countDeal" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object countDeal(String auth) {
        logger.info("#ManagerController.countDeal# auth={}",auth);
        return CommonResult.build(0, "",managerService.countDeal(auth));
    }
}
