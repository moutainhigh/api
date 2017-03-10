package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.bean.result.CountMember;
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
import com.zhsj.api.util.test.Test;

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
    @Autowired
    TbUserDao tbUserDao;

    public Map<String,String> loginByOpenId(String code,String appId){
        logger.info("#ShopService.loginByOpenId# code={},appId={}",code,appId);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put(ResultStatus.RESULT_KEY, ResultStatus.RESULT_ERROR);
        try {
            String openId = wxService.getOpenId(code,appId);
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

    public int updateOpenId(String account, String password,String openId,String appId){
        logger.info("#ShopService.updateOpenId# account={},password={},openId={},appId={}",account,password,openId,appId);
        try{
            StoreAccountBean accountBean = tbStoreAccountDao.getByAccount(account);
            String name = accountBean.getName();
            String headImg = accountBean.getHeadImg();
            if(!StringUtils.isEmpty(openId) && (StringUtils.isEmpty(name) || StringUtils.isEmpty(headImg))){
                WeixinUserBean weixinUserBean = wxService.getWeixinUser(openId,appId);
                if(weixinUserBean != null){
                    name = StringUtils.isEmpty(name)?weixinUserBean.getNickname():name;
                    headImg = StringUtils.isEmpty(headImg)?weixinUserBean.getHeadimgurl():headImg;
                }
            }
            if(StringUtils.isNotEmpty(appId)){
                List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(accountBean.getId());
                String manager_role = MtConfig.getProperty("STORE_MANAGER_ROLE", "");

                if(!CollectionUtils.isEmpty(roleIds) && roleIds.contains(Integer.parseInt(manager_role))){
                    String storeNo = tbStoreBindAccountDao.getStoreNoByAccountId(accountBean.getId());
                    tbStoreDao.updateAppId(appId,storeNo);
                }
            }
            return tbStoreAccountDao.updateOpenId(account,password,openId,name,headImg);
        }catch (Exception e){
            logger.error("#ShopService.updateOpenId# account={},password={},openId={},appId={}",account,password,openId,appId,e);
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
                countDealBean = orderDao.countDealByParentNo(storeBean.getStoreNo(),startTime,endTime);
                countDealBean.setCountPersion(tbUserBindStoreDao.countByParentNo(storeBean.getStoreNo(),startTime, endTime));
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
    public Map<String,List<StoreAccountBean>> getStoreAccount(){
        logger.info("#ShopService.getStoreAccount#");
        Map<String,List<StoreAccountBean>> map = new HashMap<>();
        try{
            StoreBean storeBean = LoginUserUtil.getStore();
            if (storeBean == null){
                return map;
            }
            List<Long> accountList = tbStoreBindAccountDao.getAccountIdByStoreNo(storeBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountList)){
                return map;
            }
            String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
            List<Long> accountIds = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountList);
            if(!CollectionUtils.isEmpty(accountIds)){
                map.put(StroeRole.CASHIER_ROLE,tbStoreAccountDao.getListByIds(accountIds));
            }
            
            roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            accountIds = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountList);
            if(!CollectionUtils.isEmpty(accountIds)){
                map.put(StroeRole.MANAGER_ROLE,tbStoreAccountDao.getListByIds(accountIds));
            }
            
        }catch (Exception e){
            logger.error("#ShopService.getStoreAccount# e={}",e.getMessage(),e);
        }
        return map;
    }

    public void updateAccountBindRoleById(long accountId){
        String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
        tbStoreAccountBindRoleDao.updateByAccountIdAndRoleId(Long.parseLong(roleId),accountId);
        tbStoreAccountDao.delById(accountId);
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
            startTimeValue = StringUtils.isEmpty(startTime)?startTimeValue:new Long(DateUtil.formatStringUnixTime(startTime, "yyyy/MM/dd")).intValue();
            endTimeValue = StringUtils.isEmpty(endTime)?endTimeValue:new Long(DateUtil.formatStringUnixTime(endTime, "yyyy/MM/dd")).intValue();
            List<Integer> statusList = new ArrayList<>();
            if(!StringUtils.isEmpty(status) && !"0".equals(status)){
                statusList.add(Integer.parseInt(status));
            }else {
                statusList.add(1);
                statusList.add(3);
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
    
    
    public Object getMemberData(){
    	int startTime = DateUtil.getTodayStartTime();//今日开始时间
    	int endTime = startTime + 86400;//今日结束时间
    	StoreBean storeBean = LoginUserUtil.getStore();
//        if(storeBean == null){
//            return CommonResult.build(1,"系统错误");
//        }
        String storeNo = storeBean.getStoreNo();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("storeNo", storeNo);
        int sumCount = tbUserBindStoreDao.getMemberCountByParam(paramMap);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        int todayCount =tbUserBindStoreDao.getMemberCountByParam(paramMap);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("sumCount", sumCount);
        resultMap.put("todayCount", todayCount);
        
        //最近late
        int currentTime = (int) DateUtil.unixTime();//当前时间
        //1、三天购买过
        int threeDay = currentTime - 3 * 86400;
        int late1 = tbUserBindStoreDao.countByStoreNo(storeNo, threeDay, currentTime);
        //2、7天购买过
        int sevenDay = currentTime - 7 * 86400;
        int late2 = tbUserBindStoreDao.countByStoreNo(storeNo, sevenDay, threeDay);
        //3、7天以上没来过
        int thDay = currentTime - 30 * 86400;
        int late3 = tbUserBindStoreDao.countByStoreNo(storeNo, thDay, sevenDay);
        //4、30天以上没来过
//        1412870400   初始化默认时间为2014/10/10
        int late4 = tbUserBindStoreDao.countByStoreNo(storeNo, 1412870400, thDay);
        //金额money
        int money1 = orderDao.getCountByMoney(storeNo,0.00, 100.00);
        int money2 = orderDao.getCountByMoney(storeNo, 100.00, 300.00);
        int money3 = orderDao.getCountByMoney(storeNo, 300.00, null);
        //频次time
        int time1 = orderDao.getCountByTime(storeNo, 1);
        int time2 = orderDao.getCountByTime(storeNo, 2);
        int time3 = orderDao.getCountByTime(storeNo, 3);
        int time4 = orderDao.getCountByTime(storeNo, 4);
        resultMap.put("late1", late1);
        resultMap.put("late2", late2);
        resultMap.put("late3", late3);
        resultMap.put("late4", late4);
        resultMap.put("money1", money1);
        resultMap.put("money2", money2);
        resultMap.put("money3", money3);
        resultMap.put("time1", time1);
        resultMap.put("time2", time2);
        resultMap.put("time3", time3);
        resultMap.put("time4", time4);
    	return CommonResult.success("SUCCESS", resultMap);
    }
    
    
    public Object getMemberDetail(String type,int number){
    	StoreBean storeBean = LoginUserUtil.getStore();
    	String storeNo = storeBean.getStoreNo();
    	if("day".equals(type)){
    		List<CountMember> countMembers = new ArrayList<CountMember>();
        	int currentTime = (int) DateUtil.unixTime();//当前时间
        	int startTime = 0,endTime = 0;
	    	if(number == 1){
	    		//1、三天购买过
	    		startTime = currentTime - 3 * 86400;
	    		endTime = currentTime;
	    	}else if(number == 2){
	    		//7天购买过
	    		startTime = currentTime - 7 * 86400;
	    		endTime = currentTime - 3* 86400;
	    	}else if(number == 3){
	    		startTime = currentTime - 30 * 86400;
	    		endTime = currentTime - 7* 86400;
	    	}else if(number == 4){
//	          1412870400   初始化默认时间为2014/10/10
	    		startTime = currentTime - 1412870400;
	    		endTime = currentTime - 7* 86400;
	    	}
	    	List<UserBindStoreBean> ubsbBeans = tbUserBindStoreDao.getListByStoreNo(storeNo, startTime, endTime);
	        for(UserBindStoreBean usBean : ubsbBeans){
	        	CountMember cm = orderDao.getByStoreNoAndUserId(storeNo, usBean.getUserId());
	        	UserBean userBean = tbUserDao.getUserById(usBean.getUserId());
	        	cm.setHeadImg(userBean.getHeadImg());
	        	cm.setNick(userBean.getNickName());
	        	cm.setTime(usBean.getUtime());
	        	cm.setUserType(usBean.getUserType());
	        	countMembers.add(cm);
	        }
	        return countMembers;
    	}else if("money".equals(type)){
    		double money1 = 0.00,money2 = 0.00;
    		if(number == 1){
    			money2 = 100.00;
    		}else if(number == 2){
    			money1 = 100.00;
    			money2 = 300.00;
    		}else if(number == 3){
    			money1 = 300.00;
    			money2 = 10000.00;
    		}
    		List<CountMember> mList = orderDao.getByStoreNoAndMoney(storeNo, money1, money2);
    		for(CountMember cMember:mList){
    			UserBean userBean = tbUserDao.getUserById(cMember.getUserId());
    			cMember.setHeadImg(userBean.getHeadImg());
    			cMember.setNick(userBean.getNickName());
    			CountMember cm = orderDao.getByStoreNoAndUserId(storeNo, cMember.getUserId());
    			cMember.setCount(cm.getCount());
    			cMember.setSum(cm.getSum());
    			UserBindStoreBean userStore = tbUserBindStoreDao.getByStoreAndUser(cMember.getUserId(), storeNo);
    			cMember.setTime(userStore.getUtime());
    			cMember.setUserType(userStore.getUserType());
    		}
    		Collections.sort(mList);
    		return mList;
    	}else if("time".equals(type)){
    		List<CountMember> timeList = orderDao.getByStoreNoAndTime(storeNo, number);
    		for(CountMember tMember:timeList){
    			UserBean userBean = tbUserDao.getUserById(tMember.getUserId());
    			tMember.setHeadImg(userBean.getHeadImg());
    			tMember.setNick(userBean.getNickName());
    			CountMember cm = orderDao.getByStoreNoAndUserId(storeNo, tMember.getUserId());
    			tMember.setCount(cm.getCount());
    			tMember.setSum(cm.getSum());
    			UserBindStoreBean userStore = tbUserBindStoreDao.getByStoreAndUser(tMember.getUserId(), storeNo);
    			tMember.setTime(userStore.getUtime());
    			tMember.setUserType(userStore.getUserType());
    		}
    		Collections.sort(timeList);
    		return timeList;
    	}
    	
    	return null;
    }

}
