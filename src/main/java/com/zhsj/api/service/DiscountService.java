package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.bean.result.DiscountPage;
import com.zhsj.api.bean.result.OrderPage;
import com.zhsj.api.bean.result.RateBean;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.constants.StroeRole;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcg on 17/2/1.
 */
@Service
public class DiscountService {
    Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    TBDiscountDao tbDiscountDao;
    @Autowired
    TBStoreBindDiscountDao tbStoreBindDiscountDao;

    public DiscountPage getDiscountPage(String auth,int status,int pageNo,int pageSize){
        logger.info("#DiscountService.getDiscountPage# auth={},status={},pageNo={},pageSize={}",auth,status,pageNo,pageSize);
        DiscountPage page = new DiscountPage();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if(storeBean == null){
                return page;
            }
            String storeNo = storeBean.getStoreNo();
            String parentStoreNo= "";
            if(StringUtils.isEmpty(storeBean.getParentNo())){
                storeNo = "";
                parentStoreNo = storeBean.getStoreNo();
            }
            Long count = tbStoreBindDiscountDao.countDiscountPage(storeNo, parentStoreNo, status);
            if(count == null || count <= 0){
                page.setPageNum(pageNo);
                page.setTotal(count.intValue());
                page.setList(new ArrayList<DiscountBean>());
                return page;
            }
            List<Long> discountIdList = tbStoreBindDiscountDao.getDiscountPage(storeNo,parentStoreNo,status,(pageNo-1)*pageSize,pageSize);
            if(CollectionUtils.isEmpty(discountIdList)){
                return page;
            }
            List<DiscountBean> discountBeanList = tbDiscountDao.getByIds(discountIdList);
            page.setTotal(count.intValue());
            page.setList(discountBeanList);
        }catch (Exception e){
            logger.error("#DiscountService.getDiscountPage# auth={},status={},pageNo={},pageSize={}",auth,status,pageNo,pageSize,e);
        }
        return page;
    }


}
