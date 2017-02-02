package com.zhsj.api.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.WebUtils;
import com.zhsj.api.bean.MSStoreBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.service.MinshengService;
import com.zhsj.api.service.WXService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(MinshengService.class);

    @Autowired
    private MinshengService minshengService;
    @Autowired
    private WXService wxService;
    @Autowired
    private ShopService shopService;


    @RequestMapping(value = "/updateMerchantByPaykey", method = RequestMethod.GET)
    @ResponseBody
    public Object updateMerchantByPaykey(@RequestParam("storeNo") String storeNo,
                                         @RequestParam("wxRate")String wxRate,@RequestParam("aliRate")String aliRate,
                                         @RequestParam("settlementType")String settlementType) throws Exception {
        boolean result = minshengService.updateMerchantByPaykey(storeNo, wxRate, aliRate, settlementType);
        if(result){
            return CommonResult.build(0, "success");
        }else {
            return CommonResult.build(1, "success");
        }
    }

    @RequestMapping(value = "/queryOrder", method = RequestMethod.GET)
    @ResponseBody
    public Object queryOrder(@RequestParam("orderNo") String orderNo
                                        ) throws Exception {
        logger.info("#ShopController.queryOrder# orderNo={}",orderNo);
        String result = minshengService.queryOrderAndUpdate(orderNo);
        if(StringUtils.isEmpty(result)){
            return CommonResult.build(1, "fail");
        }else {
            return CommonResult.build(0, "success",result);
        }
    }
    
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    @ResponseBody
    public Object qrcode(@RequestParam("no") String no,HttpServletRequest request) throws Exception {
        logger.info("#ShopController.qrcode# no={}",no);
        return null;
    }


    /////=============================
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("./shop/wx");
            String method = URLDecoder.decode("shop/login", "utf-8");
            modelAndView.addObject("method", method);
            modelAndView.addObject("state", "./shop/index"); //获取成功后跳转的地址
            modelAndView.addObject("appid", MtConfig.getProperty("weChat_appId", "wx8651744246a92699"));
        }catch (Exception e){
            logger.error("",e.getMessage(),e);
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView login(String code,String state) {
        logger.info("#ShopController.login# code={},state={}", code, state);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        try {
            Map<String,String> result = shopService.loginByOpenId(code);
            if(ResultStatus.RESULT_ERROR.equals(result.get(ResultStatus.RESULT_KEY))){
                modelAndView.setViewName("error");
            }else if(ResultStatus.NO_REGISTER.equals(result.get(ResultStatus.RESULT_KEY))) {
                modelAndView.setViewName("./shop/bindWeChat");
                modelAndView.addObject("openId",result.get(ResultStatus.RESULT_VALUE));
            }else {
                modelAndView.setViewName(state);
                String openId = result.get(ResultStatus.RESULT_VALUE);
                modelAndView.addObject("auth", "21"+openId);
            }
        }catch (Exception e){
            logger.error("#ShopController.login# code={},state={}#", code, state, e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/test" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView test() throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/index");
        String auth = "oFvcxwfZrQxlisYN4yIPbxmOT8KM";
        modelAndView.addObject("auth", "21" + auth);
        return modelAndView;
    }


    @RequestMapping(value = "/bindWeChat" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object bindWeChat(String account,String password,String openId) {
        logger.info("#ShopController.bindWeChat# account={},password={},openId={}", account, password, openId);
        int num = shopService.updateOpenId(account, password, openId);
        if(num > 0){
            return CommonResult.build(0, "",openId);
        }else {
            return CommonResult.build(1, "false");
        }
    }


    @RequestMapping(value = "/toPasswordReset", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toPasswordReset(String auth) throws Exception {
        logger.info("#PaymentController.toPasswordReset# auth={}",auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/passwordReset");
        modelAndView.addObject("auth",auth);
        return modelAndView;
    }

    @RequestMapping(value = "/toPaystyle", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toPaystyle(String auth) throws Exception {
        logger.info("#ShopController.toPaystyle# auth={}");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("storeList", shopService.getStoreChild());
        modelAndView.setViewName("./shop/paystyle");
        return modelAndView;
    }

    @RequestMapping(value = "/toPrintSetting", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toPrintSetting(String auth) throws Exception {
        logger.info("#ShopController.toPrintSetting# auth={}", auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/printSetting");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }

    @RequestMapping(value = "/signInfo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView signInfo(String auth) throws Exception {
        logger.info("#ShopController.signInfo# auth={}",auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/signInfo");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("rate",shopService.getRate());
        return modelAndView;
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView setting(String auth) throws Exception {
        logger.info("#ShopController.setting# auth={}", auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/setting");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }

    @RequestMapping(value = "/store", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView store() throws Exception {
        logger.info("#ShopController.store# no={}");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/store");
        return modelAndView;
    }

    @RequestMapping(value = "/toTransactionDetails", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toTransactionDetails(String param,String auth) throws Exception {
        logger.info("#ShopController.toTransactionDetails# auth={},param={}",auth,param);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/transactionDetails");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("_param", param);
        return modelAndView;
    }

    @RequestMapping(value = "/transactionDetails", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object transactionDetails(String param,String auth,int pageNo,int pageSize) throws Exception {
        logger.info("#ShopController.transactionDetails# auth={},param={},pageNo={},pageSize={}", auth, param,pageNo,pageSize);
        return CommonResult.build(0, "success",shopService.transactionDetails(param,pageNo,pageSize));
    }

    @RequestMapping(value = "/transactionOrder", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView transactionOrder() throws Exception {
        logger.info("#ShopController.transactionOrder# no={}");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/transactionOrder");
        return modelAndView;
    }

    @RequestMapping(value = "/toWXPushMessage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toWXPushMessage(String auth) throws Exception {
        logger.info("#ShopController.toWXPushMessage# auth={}",auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/wxPushMessage");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("storeAccountList", shopService.getStoreAccount());
        return modelAndView;
    }


    @RequestMapping(value = "/countDeal" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object countDeal(String auth) {
        logger.info("#ShopController.countDeal# auth={}", auth);
        return CommonResult.build(0, "",shopService.countDeal());
    }

    @RequestMapping(value = "/toIndex" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toIndex(String auth) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/index");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }

    @RequestMapping(value = "/toStore" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toStore(String auth) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/store");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }

    @RequestMapping(value = "/delRoleById" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object delRoleById(String auth,long accountId) {
        logger.info("#ShopController.delRoleById# auth={},accountId={}",auth,accountId);
        shopService.updateAccountBindRoleById(accountId);
        return CommonResult.build(0, "success");
    }



}
