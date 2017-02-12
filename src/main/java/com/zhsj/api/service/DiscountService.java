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
    @Autowired
    TbStoreDao tbStoreDao;
    @Autowired
    TBDiscountRuleDao tbDiscountRuleDao;

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
            if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
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

    public CommonResult addDiscount(DiscountBean discountBean,DiscountRuleBean discountRuleBean,String storeNos){
        logger.info("#DiscountService.addDiscount# discountBean={},discountRuleBean={},storeNos={}", discountBean, discountRuleBean,storeNos);
        try{
            List<String> storeNoList = new ArrayList<>();
            StoreBean storeBean = LoginUserUtil.getStore();
            if(storeBean == null){
                return CommonResult.build(1,"登录用户错误");
            }
            String parentStoreNo = storeBean.getStoreNo();
            if(StringUtils.isEmpty(storeNos)|| "-1".equals(storeNos)){
                if(!StringUtils.isEmpty(storeBean.getParentNo()) && !"0".equals(storeBean.getParentNo())){
                    parentStoreNo = storeBean.getParentNo();
                    List<StoreBean> storeBeanList = tbStoreDao.getListByParentNo(storeBean.getParentNo());
                    for(StoreBean bean:storeBeanList){
                        storeNoList.add(bean.getStoreNo());
                    }
                }
                storeNoList.add(storeBean.getStoreNo());
            }else {
                String[] nos = storeNos.split(",");
                for(String no:nos){
                    storeNoList.add(no);
                }
            }

            //查开始时间与结束时间
            List<DiscountBean> discountBeanList = tbStoreBindDiscountDao.getByParam(storeNoList, discountBean.getStartTime(), discountBean.getEndTime());
            if(!CollectionUtils.isEmpty(discountBeanList)){
                List<Long> ids = new ArrayList<>();
                Map<Long,DiscountBean> beanMap = new HashMap<>();
                for(DiscountBean bean:discountBeanList){
                    ids.add(bean.getId());
                    beanMap.put(bean.getId(),bean);
                }
                List<DiscountBean> beanList = tbDiscountDao.getByIds(ids);
                for(DiscountBean bean:beanList){
                    bean.setStoreNo(beanMap.get(bean.getId()).getStoreNo());
                }
                return CommonResult.build(1,"优惠时间有冲突",beanList);
            }
            //添加优惠
            int num = tbDiscountDao.insert(discountBean);
            if(num <=0){
                return CommonResult.build(1,"系统错误");
            }
            //添加规则
            discountRuleBean.setDiscountId(discountBean.getId());
            tbDiscountRuleDao.insert(discountRuleBean);
            //添加规则优惠与商家绑定信息
            tbStoreBindDiscountDao.insert(storeNoList, discountBean.getId(), discountBean.getStartTime(), discountBean.getEndTime(),parentStoreNo);
            return CommonResult.build(0,"");
        }catch (Exception e){
            logger.error("#DiscountService.addDiscount# discountBean={},discountRuleBean={},storeNos={}",discountBean,discountRuleBean,storeNos,e);
        }
        return CommonResult.build(1,"系统错误");
    }



}
