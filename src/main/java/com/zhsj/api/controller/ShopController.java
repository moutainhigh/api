package com.zhsj.api.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhsj.api.bean.*;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.service.UserService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.WebUtils;
import com.zhsj.api.service.MinshengService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.login.LoginUserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    @Autowired
    private UserService userService;


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
    public ModelAndView index(String appId,HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("./shop/wx");
            String method = URLDecoder.decode("shop/login", "utf-8");
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
        logger.info("#ShopController.login# code={},state={}", code, state);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        try {
            Map<String,String> result = shopService.loginByOpenId(code,state);
            if(ResultStatus.RESULT_ERROR.equals(result.get(ResultStatus.RESULT_KEY))){
                modelAndView.setViewName("error");

            }else if(ResultStatus.NO_REGISTER.equals(result.get(ResultStatus.RESULT_KEY))) {
                modelAndView.setViewName("./shop/bindWeChat");
                modelAndView.addObject("openId",result.get(ResultStatus.RESULT_VALUE));
                modelAndView.addObject("appId", state);
            }else {
                modelAndView.setViewName("./shop/index");
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
        String auth = "o5pmes7HP9w6OhjgjBpc5tTWL8Bs";
        modelAndView.addObject("auth", "21" + auth);
        return modelAndView;
    }


    @RequestMapping(value = "/bindWeChat" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object bindWeChat(String account,String password,String openId,String appId) {
        logger.info("#ShopController.bindWeChat# account={},password={},openId={},appId={}", account, password, openId,appId);
        String pw = Md5.encrypt(password);
        int num = shopService.updateOpenId(account, pw, openId,appId);
        if(num > 0){
            return CommonResult.build(0, "",appId);
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

    @RequestMapping(value = "/passwordReset", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object passwordReset(String auth,String password,String newPassword) throws Exception {
        logger.info("#PaymentController.passwordReset# auth={},password={},newPassword={}",auth,password,newPassword);
        return shopService.passwordReset(password,newPassword);
    }

//    @RequestMapping(value = "/toPaystyle", method = RequestMethod.GET)
//    @ResponseBody
//    public ModelAndView toPaystyle(String auth) throws Exception {
//        logger.info("#ShopController.toPaystyle# auth={}");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("auth",auth);
//        modelAndView.addObject("storeList", shopService.getStoreChild());
//        modelAndView.setViewName("./shop/paystyle");
//        return modelAndView;
//    }

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

    @RequestMapping(value = "/toTransactionDetails", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView toTransactionDetails(String payMethod,String startTime,String endTime,String status,String storeNo,String auth) throws Exception {
        logger.info("#ShopController.toTransactionDetails# auth={},payMethod={}, startTime={},endTime={},status={},storeNo={}",auth,payMethod,startTime,endTime,status,storeNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/transactionDetails");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("payMethod", payMethod);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("status", status);
        modelAndView.addObject("storeNo", storeNo);
        modelAndView.addObject("storeList", shopService.getStoreChild());
        return modelAndView;
    }

    @RequestMapping(value = "/transactionDetails", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object transactionDetails(String payMethod,String startTime,String endTime,String status,String storeNo,String auth,int pageNo,int pageSize) throws Exception {
        logger.info("#ShopController.transactionDetails# payMethod={}, startTime={},endTime={},status={},storeNo={},auth={},pageNo={},pageSize={}", payMethod, startTime, endTime, status, storeNo, auth, pageNo, pageSize);
        return CommonResult.build(0, "success",shopService.searchOrderPageByParam(payMethod, startTime, endTime, status, storeNo, pageNo, pageSize));
    }

    @RequestMapping(value = "/transactionOrder", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView transactionOrder(String auth,long id) throws Exception {
        logger.info("#ShopController.transactionOrder# auth={},id={}",auth,id);
        ModelAndView modelAndView = new ModelAndView();
        OrderBean orderBean = shopService.getOrderDetail(id);
        if(orderBean == null){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        modelAndView.setViewName("./shop/transactionOrder");
        modelAndView.addObject("order", orderBean);
        modelAndView.addObject("auth",auth);
        return modelAndView;
    }

    @RequestMapping(value = "/toWXPushMessage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toWXPushMessage(String auth) throws Exception {
        logger.info("#ShopController.toWXPushMessage# auth={}",auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./shop/wxPushMessage");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("accounts", shopService.getStoreAccount());
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
        logger.info("#ShopController.delRoleById# auth={},accountId={}", auth, accountId);
        shopService.updateAccountBindRoleById(accountId);
        return CommonResult.build(0, "success");
    }

    @RequestMapping(value = "/getStoreInfo" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getStoreInfo(String auth) {
        logger.info("#ShopController.delRoleById# auth={}", auth);
        Map<String,Object> map = new HashMap<>();
        map.put("store", LoginUserUtil.getStore());
        map.put("loginUser", LoginUserUtil.getLoginUser());
        return CommonResult.build(0, "success",map);
    }

    @RequestMapping(value = "/logout" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object logout(String auth) {
        logger.info("#ShopController.logout# auth={}", auth);
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        shopService.updateOpenId(loginUser.getAccount(), loginUser.getPassword(), "","");
        return CommonResult.build(0, "success");
    }

    @RequestMapping(value = "/searchUserInfo" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object searchUserInfo(String auth,long userId) {
        logger.info("#ShopController.searchUserInfo# auth={},userId", auth, userId);
        return CommonResult.build(0, "success",userService.getUserId(userId));
    }

    @RequestMapping(value = "toMemberData" ,method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object toMemberData(String auth){
    	logger.info("#ShopController.toMemberData# auth={}", auth);
//    	try {
//    		return shopService.getMemberData();
//		} catch (Exception e) {
//			return CommonResult.defaultError("ERROR");
//		}
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./member/memberData");
        modelAndView.addObject("data", shopService.getMemberData());
        modelAndView.addObject("auth", auth);
        return modelAndView;
    	
    }
    @RequestMapping(value = "toMdPage")
    @ResponseBody
    public Object toMemberDetailPage(String auth,String type,int number){
    	logger.info("#ShopController.toMemberDetailPage# auth={},type={},number={}", auth,type,number);
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("./member/memberDetail");
    	mv.addObject("auth", auth);
    	mv.addObject("type", type);
    	mv.addObject("number",number);
    	return mv;
    }
    
    @RequestMapping(value = "toMemberDetail" ,method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object toMemberDetail(String auth,String type,int number){
    	logger.info("#ShopController.toMemberDetail# auth={},type={},number={}", auth,type,number);
    	return CommonResult.success("success", shopService.getMemberDetail(type, number));
    }
    
}
