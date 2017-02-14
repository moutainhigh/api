package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
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

    public CommonResult addDiscount(String name,String startTime,String endTime,int type,String rule,String storeNos){
    	logger.info("#DiscountService.addDiscount# name={},startTime={},endTime{},type={},rule={},storeNOs={}"
    			,name,startTime,endTime,type,rule,storeNos);
        try{
        	//2017/02/14 01:05
           	int start = new Long(DateUtil.formatStringUnixTime(startTime, "yyyy/MM/dd HH:mm")).intValue();
            int end = new Long(DateUtil.formatStringUnixTime(endTime, "yyyy/MM/dd HH:mm")).intValue();
            if(start >= end){
            	return CommonResult.build(1,"开始时间不能大于结束时间");
            }
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
            List<Map<String,String>> list = JSON.parseObject(rule,List.class);
            //查开始时间与结束时间
            List<DiscountBean> discountBeanList = tbStoreBindDiscountDao.getByParam(storeNoList, start, end);
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
            DiscountBean discountBean = new DiscountBean();
            discountBean.setName(name);
            discountBean.setType(type);
            discountBean.setStartTime(start);
            discountBean.setEndTime(end);
            int num = tbDiscountDao.insert(discountBean);
            if(num <=0){
                return CommonResult.build(1,"系统错误");
            }
            //添加规则
            List<DiscountRuleBean> beanList = new ArrayList<>();
            for(Map<String,String> map:list){
            	DiscountRuleBean bean = new DiscountRuleBean();
            	bean.setDiscountId(discountBean.getId());
            	bean.setExpendAmount(Double.parseDouble(map.get("expendAmount")));
            	bean.setDiscount1(Double.parseDouble(map.get("discount1")));
            	bean.setDiscount2(Double.parseDouble(map.get("discount2")));
            	beanList.add(bean);
            }
            tbDiscountRuleDao.insert(beanList);
            //添加规则优惠与商家绑定信息
            tbStoreBindDiscountDao.insert(storeNoList, discountBean.getId(), discountBean.getStartTime(), discountBean.getEndTime(),parentStoreNo);
            return CommonResult.build(0,"");
        }catch (Exception e){
            logger.error("#DiscountService.addDiscount# name={},startTime={},endTime{},type={},rule={},storeNOs={}"
        			,name,startTime,endTime,type,rule,storeNos,e);
        }
        return CommonResult.build(1,"系统错误");
    }

    public DiscountBean getDiscountById(long id){
        return tbDiscountDao.getById(id);
    }

    public List<DiscountRuleBean> getListByDisId(long id){
        return tbDiscountRuleDao.getByDisId(id);
    }

    public List<StoreBean> getStoreListByDisId(long id){
        logger.info("#DiscountService.getStoreListByDisId# id={}",id);
        List<String> storeNOList = tbStoreBindDiscountDao.getStoreNoByDiscountId(id);
        if(CollectionUtils.isEmpty(storeNOList)){
            return  new ArrayList<>();
        }
        List<StoreBean> list = tbStoreDao.getListByNos(storeNOList);
        return list;
    }

}
