package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.bean.result.OrderPage;
import com.zhsj.api.bean.result.RateBean;
import com.zhsj.api.constants.ResultStatus;
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
        resultMap.put(ResultStatus.RESULT_KEY, ResultStatus.RESULT_ERROR);
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
        logger.info("#ShopService.updateOpenId# account={},password={},openId={}",account,password,openId);
        try{
            LoginUser loginUser = LoginUserUtil.getLoginUser();
            String name = loginUser.getName();
            String headImg = loginUser.getHeadImg();
            if(!StringUtils.isEmpty(openId) && (StringUtils.isEmpty(name) || StringUtils.isEmpty(headImg))){
                WeixinUserBean weixinUserBean = wxService.getWeixinUser(openId);
                if(weixinUserBean != null){
                    name = StringUtils.isEmpty(name)?weixinUserBean.getNickname():name;
                    headImg = StringUtils.isEmpty(headImg)?weixinUserBean.getHeadimgurl():headImg;
                }
            }
            return tbStoreAccountDao.updateOpenId(account,password,openId,name,headImg);
        }catch (Exception e){
            logger.error("#ShopService.updateOpenId# account={},password={},openId={}",account,password,openId,e);
        }
        return 0;
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


    public OrderPage searchOrderPageByParam(String payMethod,String startTime,String endTime,String status,String storeNo,int pageNo,int pageSize ){
        logger.info("#ShopService.searchOrderPageByParam# payMethod={}, startTime={},endTime={},status={},storeNo={},pageNo={},pageSize={}",payMethod,startTime,endTime,status,storeNo,pageNo,pageSize);
        OrderPage orderPage = new OrderPage();
        orderPage.setPageNum(pageNo);
        orderPage.setPageSize(pageSize);
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if (storeBean == null){
                return orderPage;
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
            String storeNoValue = storeBean.getStoreNo();
            String parentStoreNo="";
            storeNoValue = "-1".equals(storeNo)?storeNoValue:storeNo;

            if(storeNo.equals(storeBean.getStoreNo())){
                parentStoreNo = storeBean.getStoreNo();
                storeNoValue = "";
            }

            CountDealBean countDealBean = orderDao.countByParam(storeNoValue,parentStoreNo,startTimeValue,endTimeValue,statusList,payMethodValue);
            if(countDealBean.getCount() <= 0){
                return orderPage;
            }
            orderPage.setTotal(countDealBean.getCount());
            orderPage.setTotalPrice(countDealBean.getSum());
            List<OrderBean> orderBeanList = orderDao.getByParam(storeNoValue, parentStoreNo, startTimeValue, endTimeValue, statusList, payMethodValue, (pageNo - 1) * pageSize, pageSize);
            orderPage.setList(orderBeanList);
        }catch (Exception e){
            logger.error("#ShopService.searchOrderPageByParam# payMethod={}, startTime={},endTime={},status={},storeNo={},pageNo={},pageSize={}",payMethod,startTime,endTime,status,storeNo,pageNo,pageSize,e);
        }
        return orderPage;
    }

    public OrderBean getOrderDetail(long id){
        logger.info("#ShopService.getOrderDetail# id={}",id);
        OrderBean orderBean = orderDao.getById(id);
        if(orderBean == null){
            return orderBean;
        }
        StoreBean storeBean = LoginUserUtil.getStore();
        if (storeBean == null){
           storeBean = tbStoreDao.getStoreByNo(orderBean.getStoreNo());
        }
        orderBean.setStoreName(storeBean.getName());
        return orderBean;
    }

    public CommonResult passwordReset(String password,String newPassword){
        logger.info("#ShopService.passwordReset# password={},newPassword={}",password,newPassword);
        CommonResult result = CommonResult.build(1,"系统错误");
        try{
            LoginUser loginUser = LoginUserUtil.getLoginUser();
            if(loginUser == null){
                return CommonResult.build(1,"系统错误");
            }
            String pd = Md5.encrypt(password);
            if(!pd.equals(loginUser.getPassword())){
                return CommonResult.build(1,"旧密码不正确");
            }
            String npd = Md5.encrypt(newPassword);

            int num =tbStoreAccountDao.updatePassword(loginUser.getAccount(),pd,npd);
            if(num > 0){
                return CommonResult.build(0,"");
            }else {
                return CommonResult.build(1,"修改失败");
            }

        }catch (Exception e){
            logger.error("#ShopService.passwordReset# password={},newPassword={}",password,newPassword,e);
        }
        return result;
    }

}
