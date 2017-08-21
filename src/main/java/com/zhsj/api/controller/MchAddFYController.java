package com.zhsj.api.controller;

import com.zhsj.api.bean.MchInfoAddBean;
import com.zhsj.api.bean.fuyou.MchInfoFY;
import com.zhsj.api.service.MchAddService;
import com.zhsj.api.util.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mchAddFY")
public class MchAddFYController {
    Logger logger = LoggerFactory.getLogger(MchAddFYController.class);

    @Autowired
    private MchAddService mchAddService;
    
    @RequestMapping(value = "/new", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //新建商户页
    public ModelAndView newMch(String auth) {
        logger.info("#MchAddFYController.newMch# ");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("./mchAddFY/new");
        modelAndView.addObject("auth", auth);
        return modelAndView;
    }
    
    @RequestMapping(value = "/mchAdd", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //新建商户
    public Object mchAdd(String storeName,String storeAccount,String storeNo,String auth) {
    	 logger.info("#MchAddFYController.mchAdd# storeName={},storeAccount={},storeNo={},auth={}",
                 storeName, storeAccount, storeNo, auth);
    	 if(StringUtils.isEmpty(storeName) || StringUtils.isEmpty(storeAccount) || StringUtils.isEmpty(storeNo)){
    		 return CommonResult.build(1, "参数不正确");
    	 }
         String result = mchAddService.addMch(storeName, storeAccount, storeNo,2, auth);
         Map<String,String> map = new HashMap<>();
         map.put("auth", auth);
         map.put("storeNo", storeNo);
         if("SUCCESS".equals(result) || "NEXT".equals(result)){
             map.put("url","./toMchUpdate");
             return CommonResult.build(0, "", map);
         }else {
             return CommonResult.build(1, result);
         }
    }

    @RequestMapping(value = "/toMchUpdate", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //更新商户资料
    public ModelAndView toMchUpdate(String storeNo,String auth) {
        logger.info("#MchAddFYController.toMchUpdate# storeNo={},auth={} ",storeNo,auth);
        ModelAndView modelAndView = new ModelAndView();
        if(StringUtils.isEmpty(storeNo)){
        	modelAndView.setViewName("error");
        	 return modelAndView;
        }
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("info", mchAddService.getByStoreNo(storeNo,2));
        modelAndView.setViewName("./mchAddFY/mchInfo");
        return modelAndView;
    }
    
    @RequestMapping(value = "/mchUpdate" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object mchUpdate(MchInfoFY info,String auth) {
        logger.info("#MchAddFYController.mchUpdate# info={},auth={},",info,auth);
        if(StringUtils.isEmpty(info.getProvince()) || StringUtils.isEmpty(info.getCity()) || StringUtils.isEmpty(info.getCounty())|| StringUtils.isEmpty(info.getAddress())){
        	return CommonResult.build(1, "城市信息出错");
        }
        
        if(StringUtils.isEmpty(info.getBusiness())){
        	return CommonResult.build(1, "营业类别出错");
        }
        
        if(StringUtils.isEmpty(info.getContact_person()) || StringUtils.isEmpty(info.getContact_mobile()) || StringUtils.isEmpty(info.getCertif_id())){
        	return CommonResult.build(1, "人员信息错误");
        }
        
        String result = mchAddService.updateMchFY(info, auth);
        Map<String,String> map = new HashMap<>();
        map.put("auth", auth);
        map.put("storeNo", info.getStoreNo());
        if("SUCCESS".equals(result)){
            map.put("url","./toMchSettle");
            return CommonResult.build(0, "", map);
        } else {
            return CommonResult.build(1, result);
        }
    }
    
    @RequestMapping(value = "/toMchSettle", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //更新商户结算
    public ModelAndView toMchSettle(String storeNo,String auth) {
    	logger.info("#MchAddFYController.toMchSettle# storeNo={},auth={} ",storeNo,auth);
        ModelAndView modelAndView = new ModelAndView();
        if(StringUtils.isEmpty(storeNo)){
        	modelAndView.setViewName("error");
        	 return modelAndView;
        }
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("info", mchAddService.getByStoreNo(storeNo,2));
        modelAndView.setViewName("./mchAddFY/settle");
        return modelAndView;
    }
    
    @RequestMapping(value = "/mchSettle" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object mchSettle(MchInfoFY info,String auth){
        logger.info("#MchAddFYController.mchSettle# info={},auth={}",info, auth);
        if(StringUtils.isEmpty(info.getAcnt_nm()) || StringUtils.isEmpty(info.getInter_bank_no()) || StringUtils.isEmpty(info.getIss_bank_nm()) ){
        	return CommonResult.build(1, "银行信息不能为空");
        }
        
        if(StringUtils.isEmpty(info.getAcnt_nm()) || StringUtils.isEmpty(info.getAcnt_certif_id())){
        	return CommonResult.build(1, "开户人信息不能为空");
        }
        
        if(StringUtils.isEmpty(info.getWx_set_cd()) || StringUtils.isEmpty(info.getAli_set_cd()) ){
        	return CommonResult.build(1, "支付费率不能为空");
        }
        
        String result = mchAddService.settleMchFY(info,auth);
        Map<String,String> map = new HashMap<>();
        map.put("auth", auth);
        map.put("storeNo",info.getStoreNo());
        if("SUCCESS".equals(result)){
            map.put("url","./mchSuccess");
            return CommonResult.build(0, "", map);
        } else {
            return CommonResult.build(1, result);
        }
    }
    
    @RequestMapping(value = "/mchSuccess", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //成功页
    public ModelAndView mchSuccess(String storeNo,String auth) {
        logger.info("#MchAddFYController.mchSuccess# storeNo={},auth={} ",storeNo,auth);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth",auth);
        modelAndView.addObject("storeNo", storeNo);
        modelAndView.setViewName("./mchAdd/mchAddSuccess");
        return modelAndView;
    }

}
