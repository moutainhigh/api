package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
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

import java.util.*;

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
                countDealBean = orderDao.countDealByParentNo(storeBean.getParentNo(),startTime,endTime);
                countDealBean.setCountPersion(tbUserBindStoreDao.countByParentNo(storeBean.getParentNo(),startTime, endTime));
            } else {
                countDealBean = orderDao.countDealByStoreNo(storeBean.getStoreNo(),startTime,endTime);
                countDealBean.setCountPersion(tbUserBindStoreDao.countByStoreNo(storeBean.getStoreNo(),startTime,endTime));
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
            if(storeBean == null || StringUtils.isEmpty(storeBean.getStoreNo())){
                return new ArrayList<>();
            }
            list.add(storeBean);
            list.addAll(tbStoreDao.getListByParentNo(storeBean.getStoreNo()));
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
            if(storeBean == null ||StringUtils.isEmpty(storeBean.getStoreNo()) ){
                return rateBean;
            }
            StorePayInfo storePayInfo = tbStorePayInfoDao.getStorePayInfoByNO(storeBean.getStoreNo());
            if(storePayInfo == null || StringUtils.isEmpty(storePayInfo.getRemark())){
                return rateBean;
            }
            Map<String,String> map = JSONObject.parseObject(storePayInfo.getRemark(), Map.class);
            rateBean.setAlRate(map.get("aliRate") + "%");
            rateBean.setWxRate(map.get("wxRate") + "%");
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

    public CountDealBean countTransactionDetails(String param){
        logger.info("#ShopService.countTransactionDetails# param={}", param);
        CountDealBean countDealBean = new CountDealBean();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if (storeBean == null){
                return countDealBean;
            }
            //        var data = {"payType":chkRadio,"startTime":startTime,"endTime":endTime,"status":status,"storeNo":_selectStoreNo};
            int payType = 0;
            int startTime = DateUtil.getTodayStartTime();
            int endTime = startTime+ 86400;
            List<Integer> statusList = new ArrayList<>();
            String storeNo = "";
            String parentStoreNo="";
            if(!StringUtils.isEmpty(param)){
                Map<String ,String> map = JSONObject.parseObject(param, Map.class);
                payType = Integer.parseInt(map.get("payType"));
                startTime = StringUtils.isEmpty(map.get("startTime"))?startTime:Integer.parseInt(map.get("startTime"));
                endTime = StringUtils.isEmpty( map.get("endTime"))?endTime:Integer.parseInt( map.get("endTime"));
                String ts = map.get("status");
                if(!StringUtils.isEmpty(map.get("status"))){
                    String[] st = map.get("status").split(",");
                    for(int i=0;i<st.length;i++){
                        statusList.add(Integer.parseInt(st[i]));
                    }
                }
                storeNo = "-1".equals(map.get("storeNo"))?storeNo:map.get("storeNo");
            }
            if(storeNo.equals(storeBean.getStoreNo())){
                parentStoreNo = storeBean.getStoreNo();
                storeNo = "";
            }
            countDealBean = orderDao.countByParam(storeNo,parentStoreNo,startTime,endTime,statusList,payType);
        }catch (Exception e){
            logger.error("#ShopService.transactionDetails# param={}",param,e);
        }
        return countDealBean;

    }

    public List<OrderBean> transactionDetails(String payMethod,String startTime,String endTime,String status,String storeNo,int pageNo,int pageSize){
        logger.info("#ShopService.transactionDetails# payMethod={}, startTime={},endTime={},status={},storeNo={},pageNo={},pageSize={}",payMethod,startTime,endTime,status,storeNo,pageNo,pageSize);
        List<OrderBean> orderBeanList = new ArrayList<>();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if (storeBean == null){
                return orderBeanList;
            }
            //        var data = {"payType":chkRadio,"startTime":startTime,"endTime":endTime,"status":status,"storeNo":_selectStoreNo};
            int payMethodValue = StringUtils.isEmpty(payMethod)?0:Integer.parseInt(payMethod);
            int startTimeValue = DateUtil.getTodayStartTime();
            int endTimeValue = startTimeValue+ 86400;
            startTimeValue = StringUtils.isEmpty(startTime)?startTimeValue:new Long(DateUtil.formatStringUnixTime(startTime, "yyyy-MM-dd")).intValue();
            endTimeValue = StringUtils.isEmpty(endTime)?endTimeValue:new Long(DateUtil.formatStringUnixTime(endTime, "yyyy-MM-dd")).intValue();
            List<Integer> statusList = new ArrayList<>();
            if(!StringUtils.isEmpty(status) && !"-1".equals(status)){
                String[] st = status.split(",");
                for(int i=0;i<st.length;i++){
                    statusList.add(Integer.parseInt(st[i]));
                }
            }
            String storeNoValue = "";
            String parentStoreNo="";
            storeNoValue = "-1".equals(storeNo)?storeNoValue:storeNo;

            if(storeNo.equals(storeBean.getStoreNo())){
                parentStoreNo = storeBean.getStoreNo();
                storeNoValue = "";
            }
            orderBeanList = orderDao.getByParam(storeNoValue, parentStoreNo, startTimeValue, endTimeValue, statusList, payMethodValue, (pageNo - 1) * pageSize, pageSize);
        }catch (Exception e){
            logger.error("#ShopService.transactionDetails# payMethod={}, startTime={},endTime={},status={},storeNo={},pageNo={},pageSize={}",payMethod,startTime,endTime,status,storeNo,pageNo,pageSize,e);
        }
        return orderBeanList;
    }

}
