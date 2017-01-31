package com.zhsj.api.controller;

import java.util.List;
import java.util.Map;

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






}
