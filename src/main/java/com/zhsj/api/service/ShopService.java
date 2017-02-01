package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.bean.result.RateBean;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.dao.*;
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
public class ShopService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    WXService wxService;
    @Autowired
    TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    TbOrderDao orderDao;
    @Autowired
    TbUserBindStoreDao tbUserBindStoreDao;
    @Autowired
    TbStoreDao tbStoreDao;
    @Autowired
    TbStorePayInfoDao tbStorePayInfoDao;
    @Autowired
    TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;

    public Map<String,String> loginByOpenId(String code){
        logger.info("#ShopService.loginByOpenId# code={}",code);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_ERROR);
        try {
            String openId = wxService.getOpenId(code);
            if(StringUtils.isEmpty(openId)){
                resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_ERROR);
                return resultMap;
            }
            StoreAccountBean storeAccountBean = tbStoreAccountDao.getByOpenId(openId);
            if(storeAccountBean == null){
                resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.NO_REGISTER);
                resultMap.put(ResultStatus.RESULT_VALUE,openId);
                return resultMap;
            }
            resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_SUCCESS);
            resultMap.put(ResultStatus.RESULT_VALUE,openId);
        }catch (Exception e){
            logger.error("#ShopService.loginByOpenId# code={}",code,e);
        }
        return resultMap;
    }

    public int updateOpenId(String account, String password,String openId){
        String pw = Md5.encrypt(password);
        return tbStoreAccountDao.updateOpenId(account, pw, openId);
    }

    public StoreAccountBean getStoreAccountByOpenId(String openId){
        return tbStoreAccountDao.getByOpenId(openId);
    }

    public CountDealBean countDeal(){
        logger.info("#ShopService.countDeal# ");
        CountDealBean countDealBean = new CountDealBean();
        try {
            StoreBean storeBean = LoginUserUtil.getStore();
            int startTime = DateUtil.getTodayStartTime();
            int endTime = startTime + 86400;
            if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
                countDealBean = orderDao.countDealByStoreNo(storeBean.getStoreNo(),startTime,endTime);
                countDealBean.setCountPersion(tbUserBindStoreDao.countByStoreNo(storeBean.getStoreNo(),startTime,endTime));
            }else {
                countDealBean = orderDao.countDealByParentNo(storeBean.getParentNo(),startTime,endTime);
                countDealBean.setCountPersion(tbUserBindStoreDao.countByParentNo(storeBean.getParentNo(),startTime,endTime));
            }
        }catch (Exception e){
            logger.error("#ShopService.loginByOpenId# e={}",e.getMessage(),e);
        }
        return countDealBean;
    }


    public List<StoreBean> getStoreChild(){
        logger.info("#ShopService.getStoreChild#");
        List<StoreBean> list = new ArrayList<>();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
                return new ArrayList<>();
            }
            list =tbStoreDao.getListByParentNo(storeBean.getParentNo());
        }catch (Exception e){
            logger.error("#ShopService.getStoreChild# e={}",e.getMessage(),e);
        }
        return list;

    }

    public RateBean getRate(){
        logger.info("#ShopService.getRate#");
        RateBean rateBean = new RateBean();
        rateBean.setWxRate("暂末开通");
        rateBean.setAlRate("暂末开通");
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
                return rateBean;
            }
            StorePayInfo storePayInfo = tbStorePayInfoDao.getStorePayInfoByNO(storeBean.getStoreNo());
            if(storePayInfo == null || StringUtils.isEmpty(storePayInfo.getRemark())){
                return rateBean;
            }
            Map<String,Object> map = JSONObject.parseObject(storePayInfo.getRemark(), Map.class);
            rateBean.setAlRate(String.valueOf((Double)map.get("aliRate"))+"%");
            rateBean.setWxRate(String.valueOf((Double) map.get("wxRate")) +"%");
        }catch (Exception e){
            logger.error("#ShopService.getStoreChild# e={}",e.getMessage(),e);
        }
        return rateBean;
    }


    //获取收银员
    public List<StoreAccountBean> getStoreAccount(){
        logger.info("#ShopService.getStoreAccount#");
        List<StoreAccountBean> list = new ArrayList<>();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if (storeBean == null){
                return list;
            }
            List<Long> accountList = tbStoreBindAccountDao.getAccountIdByStoreNo(storeBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountList)){
                return list;
            }
            String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
            List<Long> accountIds = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountList);
            if(CollectionUtils.isEmpty(accountIds)){
                return list;
            }
            list = tbStoreAccountDao.getListByIds(accountIds);
        }catch (Exception e){
            logger.error("#ShopService.getStoreAccount# e={}",e.getMessage(),e);
        }
        return list;
    }

    public void updateAccountBindRoleById(long accountId){
        String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
        tbStoreAccountBindRoleDao.updateByAccountIdAndRoleId(Long.parseLong(roleId),accountId);
    }

}
