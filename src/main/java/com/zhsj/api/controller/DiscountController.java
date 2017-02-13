package com.zhsj.api.controller;

import com.zhsj.api.bean.DiscountBean;
import com.zhsj.api.bean.DiscountRuleBean;
import com.zhsj.api.service.DiscountService;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.util.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/discount")
public class DiscountController {
    Logger logger = LoggerFactory.getLogger(DiscountController.class);

    @Autowired
    private DiscountService discountService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/discountActivity", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView discountActivity(String auth) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/discountActivity");
        modelAndView.addObject("auth",auth);
        return modelAndView;
    }

    @RequestMapping(value = "/getDiscountPage", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getDiscountPage(String auth,int status,int pageNo,int pageSize) {
        logger.info("#DiscountController.getDiscountPage# auth={},status={},pageNO={},pageSize={}", auth, status, pageNo, pageSize);
        return  CommonResult.build(0, "", discountService.getDiscountPage(auth, status, pageNo, pageSize));
    }

    @RequestMapping(value = "/reduceSetting", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView reduceSetting(String auth) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/reduceSetting");
        modelAndView.addObject("auth", auth);
        modelAndView.addObject("shopList",shopService.getStoreChild());
        return modelAndView;
    }


    @RequestMapping(value = "/addDiscount", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public CommonResult addDiscount(String auth,String name,String startTime,String endTime,int type,String rule,String storeNos) {
        logger.info("#DiscountController.addDiscount# auth={},name={},startTime={},endTime{},type={},rule={},storeNOs={}",auth,name,startTime,endTime,type,rule,storeNos);
        return  discountService.addDiscount(name, startTime, endTime, type, rule, storeNos);
    }


    @RequestMapping(value = "/activityDetail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView activityDetail(String auth,int discountId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/activityDetail");
        return modelAndView;
    }
    

    
    @RequestMapping(value = "/menbershop", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView minusFunctionSetting(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/menbershop");
        return modelAndView;
    }
    
    @RequestMapping(value = "/updateSetting", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView updateActivity(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./discount/updateSetting");
        return modelAndView;
    }


    

}
