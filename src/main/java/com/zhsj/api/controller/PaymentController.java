package com.zhsj.api.controller;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.PayBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.service.*;
import com.zhsj.api.task.async.OrderSuccessAsync;
import com.zhsj.api.util.AyncTaskUtil;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.WebUtils;
import com.zhsj.api.util.MtConfig;
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
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    

    @Autowired
    private WXService wxService;
    @Autowired
    private MinshengService minshengService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AyncTaskUtil ayncTaskUtil;



    @RequestMapping(value = "/scanCode", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView scanCode(@RequestParam("no") String no,HttpServletRequest request) throws Exception {
        logger.info("#PaymentController.scanCode# no={}",no);
        ModelAndView modelAndView = new ModelAndView();
        Map<String, List<String>> headers = WebUtils.getHeaders(request);
        List<String> userAgentList = headers.get("user-agent");
        //得不到类型，返回错误
        if(userAgentList == null || userAgentList.size() == 0){
            modelAndView.setViewName("error");
        }
        modelAndView.addObject("no", no);
        String userAgent = userAgentList.get(0);
        if(userAgent.indexOf("MicroMessenger") >= 0){
            //微信
            modelAndView.addObject("appid", MtConfig.getProperty("weChat_appId","wx8651744246a92699"));
            modelAndView.setViewName("pay/wx");
        }else if(userAgent.indexOf("Alipay") >= 0){
            //支付宝
            modelAndView.setViewName("pay/zfb");
        }else {
            //其它
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    //获取微信openId
    @RequestMapping(value = "/getUserOpenId", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUserOpenId(@RequestParam("code") String code,@RequestParam("state") String state) throws Exception {
        logger.info("#PaymentController.getUserOpenId# code={},state={}",code,state);
        ModelAndView modelAndView = new ModelAndView();
        String openId = wxService.getOpenId(code);
        if(StringUtils.isEmpty(openId)){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        StoreBean storeBean = storeService.getStoreByNO(state);
        UserBean userBean = userService.saveStoreUser(openId, 1, state,storeBean.getParentNo());
        modelAndView.setViewName("pay/weChatPay");
        modelAndView.addObject("openId", openId);
        modelAndView.addObject("payMethod",1);
        modelAndView.addObject("store",storeBean);
        logger.info("#PaymentController.getUserOpenId# result code={},state={},userId={},userId={}#",code,state,userBean.getId(),userBean.getOpenId());
        return modelAndView;
    }

    //获取支付宝ID
    @RequestMapping(value = "/getBuyerId", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView getBuyerId(String storeNo,HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#PaymentController.getBuyerId# storeNo={}",storeNo);
        String[] args = storeNo.split("\\?buyer_id=");
        StoreBean storeBean = storeService.getStoreByNO(args[0]);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pay/aliPay");
        modelAndView.addObject("buyerId", args[1]);
        modelAndView.addObject("payMethod", 2);
        UserBean userBean = userService.saveStoreUser(args[1], 2, args[0],storeBean.getParentNo());
        modelAndView.addObject("store",storeBean);
        logger.info("#PaymentController.getBuyerId# result storeNo={},buyerId={},userId={}", args[0], args[1], userBean.getId());
        return modelAndView;
    }
    

    @RequestMapping(value = "/payMoney", method =  {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object payMoney(PayBean payBean,HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#PaymentController.payMoney# payBean={}",payBean);
        String hostName = InetAddress.getLocalHost().getHostAddress();
        if(payBean.getPayMethod() == 1){
            Map<String,String> map = minshengService.payWeChat(payBean,hostName);
            if(map == null || "FAIL".equals(map.get("return_code"))){
                return CommonResult.build(1, "false");
            }else {
                return CommonResult.build(0, "success",map);
            }
        }else if(payBean.getPayMethod() == 2){
            Map<String,String> map = minshengService.payAli(payBean,hostName);
            if(map == null || "FAIL".equals(map.get("return_code"))){
                return CommonResult.build(1, "false");
            }else {
                return CommonResult.build(0, "success", map);
            }
//            modelAndView.setViewName("pay/alicommit");
//            modelAndView.addObject("data",resultMap.get("trade_no"));
        }else {
            return CommonResult.build(1, "false");
        }
    }

    @RequestMapping(value = "/paySuccess", method =  {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView paySuccess(String orderId) throws Exception {
        logger.info("#PaymentController.paySuccess# orderId={}", orderId);
        OrderBean orderBean = minshengService.paySuccess(orderId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("order",orderBean);
        modelAndView.setViewName("pay/paySuccess");
        return modelAndView;
    }


    @RequestMapping(value = "/payNotifyWeChat", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void payNotifyWeChat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result_code = request.getParameter("result_code");
        String order_no = request.getParameter("order_no");
        String field1 = request.getParameter("field1");
        logger.info("#PaymentController.payNotifyWeChat# result_code={},order_no={},field1={}",result_code,order_no,field1);
        if("SUCCESS".equals(result_code)){
           int num = orderService.updateOrderByOrderIdAndStatus(1,order_no,0);
           if(num <= 0){
        	  return;
           }
           wxService.sendSuccess(order_no);
        }else {
            orderService.updateOrderByOrderId(2,order_no);
        }
        wxService.getUserInfo(field1);

    	Map<String,Object> map = request.getParameterMap();
    	for(String name: map.keySet()){
            logger.info(name+"====="+request.getParameter(name));
//          result_code=====SUCCESS
//            pay_way_code=====WXF2F
//            remark=====测试商家-支付
//            order_time=====20170107141624
//            product_name=====测试商家-支付
//            field1=====oFvcxwfZrQxlisYN4yIPbxmOT8KM
//            order_date=====20170107
//            paykey=====85a6c4e20bf54505bea8e75bc870d587
//            order_price=====0.010
//            order_no=====20170107141624252SN0ba482a1d

        }
    }

    @RequestMapping(value = "/payNotifyAli", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void payNotifyAli(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#PaymentController.payNotifyAli# ");

        Map<String,Object> map = request.getParameterMap();
        for(String name: map.keySet()){
            logger.info(name+"====="+request.getParameter(name));
        }

    }
    







}
