package com.zhsj.controller;

import com.zhsj.bean.PayBean;
import com.zhsj.bean.UserBean;
import com.zhsj.service.MinshengService;
import com.zhsj.service.UserService;
import com.zhsj.service.WXService;
import com.zhsj.util.MtConfig;
import com.zhsj.util.WebUtils;
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
import javax.servlet.http.HttpSession;
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
        UserBean userBean = userService.saveStoreUser(openId, 1, state);
        modelAndView.setViewName("pay/pay");
        modelAndView.addObject("openId", openId);
        modelAndView.addObject("no",state);
        modelAndView.addObject("payMethod",1);
        logger.info("#PaymentController.getUserOpenId# result code={},state={},userId={},userId={}#",code,state,userBean.getId(),userBean.getOpenId());
        return modelAndView;
    }

    //获取支付宝ID
    @RequestMapping(value = "/getBuyerId", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView getBuyerId(String storeNo,HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#PaymentController.getBuyerId# storeNo={}",storeNo);
        String[] args = storeNo.split("\\?buyer_id=");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pay/pay");
        modelAndView.addObject("buyerId", args[1]);
        modelAndView.addObject("no", args[0]);
        modelAndView.addObject("payMethod", 2);
        UserBean userBean = userService.saveStoreUser(args[1], 2, args[0]);
        logger.info("#PaymentController.getBuyerId# result storeNo={},buyerId={},userId={}", args[0], args[1], userBean.getId());
        return modelAndView;
    }
    

    @RequestMapping(value = "/payMoney", method = RequestMethod.GET)
    public ModelAndView payMoney(PayBean payBean,HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#PaymentController.payMoney# payBean={}",payBean);
        ModelAndView modelAndView = new ModelAndView();
        if(payBean.getPayMethod() == 1){
            Map<String,String> map = minshengService.payWeChat(payBean);
            modelAndView.setViewName("pay/wxcommit");
            modelAndView.addObject(map);
            return modelAndView;
        }else if(payBean.getPayMethod() == 2){
            Map<String,String> resultMap = minshengService.payAli(payBean);
            modelAndView.setViewName("pay/alicommit");
            modelAndView.addObject("data",resultMap.get("trade_no"));
        }else {
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/payNotifyWeChat", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void payNotifyWeChat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result_code = request.getParameter("result_code");
        String order_no = request.getParameter("order_no");
        String field1 = request.getParameter("field1");
        logger.info("#PaymentController.payNotifyWeChat# result_code={},order_no={},field1={}",result_code,order_no,field1);

    	Map<String,Object> map = request.getParameterMap();
    	for(String name: map.keySet()){
            logger.info(name+"====="+request.getParameter(name));
//            sign=====[Ljava.lang.String;@58372003
//            result_code=====[Ljava.lang.String;@742599c
//            pay_way_code=====[Ljava.lang.String;@1509147d
//            remark=====[Ljava.lang.String;@7202555
//            order_time=====[Ljava.lang.String;@4f72f31a
//            product_name=====[Ljava.lang.String;@3b658b6
//            field1=====[Ljava.lang.String;@387a16a1
//            order_date=====[Ljava.lang.String;@5763c0fe
//            paykey=====[Ljava.lang.String;@56139084
//            order_price=====[Ljava.lang.String;@1e74f642
//             order_no=====[Ljava.lang.String;@10d376ad
        }

    }

    @RequestMapping(value = "/payNotifyAli", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void payNotifyAli(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String result_code = request.getParameter("result_code");
//        String order_no = request.getParameter("order_no");
//        String field1 = request.getParameter("field1");
//        logger.info("#PaymentController.payNotifyAli# result_code={},order_no={},field1={}",result_code,order_no,field1);

        Map<String,Object> map = request.getParameterMap();
        for(String name: map.keySet()){
            logger.info(name+"====="+request.getParameter(name));
//            sign=====[Ljava.lang.String;@58372003
//            result_code=====[Ljava.lang.String;@742599c
//            pay_way_code=====[Ljava.lang.String;@1509147d
//            remark=====[Ljava.lang.String;@7202555
//            order_time=====[Ljava.lang.String;@4f72f31a
//            product_name=====[Ljava.lang.String;@3b658b6
//            field1=====[Ljava.lang.String;@387a16a1
//            order_date=====[Ljava.lang.String;@5763c0fe
//            paykey=====[Ljava.lang.String;@56139084
//            order_price=====[Ljava.lang.String;@1e74f642
//             order_no=====[Ljava.lang.String;@10d376ad
        }

    }
    







}
