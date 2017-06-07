package com.zhsj.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.service.BaseService;
import com.zhsj.api.service.JPushService;
import com.zhsj.api.service.MchAddService;
import com.zhsj.api.service.ModuleService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.PrinterService;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.service.StoreAccountService;
import com.zhsj.api.service.StoreService;
import com.zhsj.api.util.CommonResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cashier")
public class CashierController {
    Logger logger = LoggerFactory.getLogger(CashierController.class);

    @Autowired
    private StoreAccountService storeAccountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private JPushService jPushService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PrinterService printerService;
    @Autowired
    private StoreService storeService;
    
    @RequestMapping(value = "/sign", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //新建商户页
    public Object sign(String account,String passwd,String lat,String lon,String regId,String imei,String auth) {
        logger.info("#CashierController.sign# account={},passwd={},lat={},lon={},regId={},imei={},auth={}",
        		account,passwd,lat,lon,regId,imei,auth);
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(passwd)){
        	return CommonResult.defaultError("账号密码不能为空");
        }
        if(StringUtils.isEmpty(lat) || StringUtils.isEmpty(lon)){
        	return CommonResult.defaultError("位置信息不能为空");
        }
        return  storeAccountService.signCashier(account, passwd, lat, lon,regId,imei, auth);

    }
    
    @RequestMapping(value = "/countToday", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //统计今天流水
    public Object countToday(String storeNo,String userId,String auth) {
        logger.info("#CashierController.countToday# storeNO={},userId={},auth={}",
        		storeNo,userId,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        return  orderService.countToday(storeNo, userId, auth);
    }
    
    @RequestMapping(value = "/getModel", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //统计今天流水
    public Object getModel(String storeNo,String os,String rate,String auth,HttpServletRequest request) {
        logger.info("#CashierController.getModel# storeNO={},os={},rate={},auth={}",
        		storeNo,os,rate,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(os)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(auth)){
        	return CommonResult.defaultError("auth字段不能为空");
        }
        return  moduleService.getAppModule(storeNo, os,rate, auth,request);
    }
   
    @RequestMapping(value = "/countShift", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //交班统计
    public Object countShift(String storeNo,String userId,String startTime,String endTime, String auth) {
        logger.info("#CashierController.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
        		storeNo,userId,startTime,endTime,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
        	return CommonResult.defaultError("时间信息有误");
        }
        int stime = Integer.parseInt(startTime);
        int etime = Integer.parseInt(endTime);
        
        return  orderService.countShift(storeNo, userId, stime, etime, auth);
    }
    
    @RequestMapping(value = "/sentShiftMsg", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //交班统计打印
    public Object sentShiftMsg(String storeNo,String userId,String startTime,String endTime,String type, String auth) {
        logger.info("#CashierController.countShift# storeNO={},userId={},startTime={},endTime={},type={},auth={}",
        		storeNo,userId,startTime,endTime,type,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
        	return CommonResult.defaultError("时间信息有误");
        }
        if(StringUtils.isEmpty(type)){
        	return CommonResult.defaultError("发送类型有误");
        }
        int stime = Integer.parseInt(startTime);
        int etime = Integer.parseInt(endTime);
        
        return  printerService.sentShiftMsg(storeNo, userId, stime, etime, type, auth);
    }
    
    
    @RequestMapping(value = "/createPayCode", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //生成商家二维码
    public Object createPayCode(String storeNo,String userId,String price, String auth) {
        logger.info("#CashierController.createPayCode# storeNO={},userId={},price={},auth={}",
        		storeNo,userId,price,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(price) ){
        	return CommonResult.defaultError("金额信息有误");
        }
       return baseService.createPayCode(storeNo, userId, price, auth);
    }
    
    @RequestMapping(value = "/microPay", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //主扫支付
    public Object microPay(String storeNo,String userId,String price,String authCode, String auth) {
        logger.info("#CashierController.microPay# storeNO={},userId={},price={},authCode={},auth={}",
        		storeNo,userId,price,authCode,auth);
        if(StringUtils.isEmpty(storeNo) || StringUtils.isEmpty(userId)){
        	return CommonResult.defaultError("输入信息有误");
        }
        if(StringUtils.isEmpty(price) ){
        	return CommonResult.defaultError("金额信息有误");
        }
        if(StringUtils.isEmpty(authCode) ){
        	return CommonResult.defaultError("授权码不能为空");
        }
        return  baseService.microPay(storeNo, userId, price, authCode, auth);
    }
    
    
    @RequestMapping(value = "/checkUpdate", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //版本更新
    public Object checkUpdate(String version,String os,String auth) {
        logger.info("#CashierController.checkUpdate# version={},os={},auth={}",version,os,auth);
        if(StringUtils.isEmpty(version)){
        	return CommonResult.defaultError("版本号不能为空");
        }
        Map<String,Object> map = new HashMap<String, Object>();
        return  baseService.checkUpdate(version,os, auth);
    }
    
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //极光推送+云打印
    public Object sendMessage(String orderNo) {
        logger.info("#CashierController.sendMessage# orderNo={}",orderNo);
        if(StringUtils.isEmpty(orderNo)){
        	return CommonResult.defaultError("订单号不存在");
        }
        return  jPushService.sendSuccessMsg(orderNo);
    }

    @RequestMapping(value = "/signOut", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //退出
    public Object signOut(String account,String lat,String lon,String regId,String imei,String auth) {
        logger.info("#CashierController.signOut# account={},lat={},lon={},regId={},imei={},auth={}",
        		account,lat,lon,regId,imei,auth);
        if(StringUtils.isEmpty(account)){
        	return CommonResult.defaultError("账号密码不能为空");
        }
        if(StringUtils.isEmpty(lat) || StringUtils.isEmpty(lon)){
        	return CommonResult.defaultError("位置信息不能为空");
        }
        
        return  storeAccountService.signOutCashier(account, lat, lon,regId,imei, auth);

    }
    
    @RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
    @ResponseBody
    //订间详情
    public ModelAndView orderDetail(long id) throws Exception {
        logger.info("#CashierController.orderDetail# id={}",id);
        ModelAndView modelAndView = new ModelAndView();
        OrderBean orderBean = shopService.getOrderDetail(id);
        if(orderBean == null){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        modelAndView.setViewName("./cashier/orderDetail");
        modelAndView.addObject("order", orderBean);
        return modelAndView;
    }
    
    
    @RequestMapping(value = "orderListPage")
    public Object orderListPage(ModelAndView mv, String storeNo){
    	logger.info("#orderListPage# storeNo ={}", storeNo);
    	mv.addObject("storeList", storeService.getListByStoreNo(storeNo));
    	mv.addObject("storeNo", storeNo);
    	mv.setViewName("app/order");
    	return mv;
    }
    
    @RequestMapping(value = "orderList")
    @ResponseBody
    public Object orderList(String storeNo,int payMethod, int startTime, int endTime, int status, int page,int pageSize){
    	logger.info("#orderList# storeNo = {}, payMethod = {}, startTime= {},endTime={}, status ={},page+{}, pageSize={}",
    			storeNo, payMethod, startTime, endTime, status, page, pageSize);
    	if(StringUtils.isEmpty(storeNo)){
    		return CommonResult.build(2, "门店编号有误");
    	}
    	return orderService.getOrderListByParam(storeNo, payMethod, startTime, endTime, status, page, pageSize);
    }
    
    @RequestMapping(value = "refundPage")
    @ResponseBody
    public Object refundPage(String storeNo,String userId,ModelAndView mv){
    	logger.info("#refund# storeNo={},userId={}", storeNo, userId);
    	if(StringUtils.isEmpty(storeNo) && StringUtils.isEmpty(userId)){
    		return CommonResult.build(2, "参数有误");
    	}
    	mv.addObject("storeNo", storeNo);
    	mv.addObject("userId", userId);
    	mv.setViewName("app/refund");
    	return mv;
    }
    
    @RequestMapping(value = "serach")
    @ResponseBody
    public Object serach(String storeNo, String orderId,String transId){
    	logger.info("#serach# storeNo = {}, orderId = {}, transId = {}", storeNo, orderId, transId);
    	if(StringUtils.isEmpty(orderId) && StringUtils.isEmpty(transId)){
    		return CommonResult.build(2, "参数不能为空");
    	}
    	return orderService.serachByOrderIdOrTransId(storeNo, orderId, transId);
    }
    
    @RequestMapping(value = "refund")
    @ResponseBody
    public Object refund(String orderNo,double price,int userId){
    	logger.info("#refund# orderNo = {}, price = {}, userId = {}", orderNo, price, userId);
    	return null;
    }
}
