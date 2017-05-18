package com.zhsj.api.service;

import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.result.StoreCountResult;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.dao.TbUserBindStoreDao;
import com.zhsj.api.dao.TbUserDao;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private TbOrderDao tbOrderDao;
    @Autowired
    private TbUserBindStoreDao tbUserBindStoreDao;

    public void updateOrderByOrderId(int status,String orderId){
    	tbOrderDao.updateOrderByOrderId(status,orderId);
    }

    public int updateOrderByOrderIdAndStatus(int status,String orderId,int preStatus){
        return tbOrderDao.updateOrderByOrderIdIde(status,preStatus,orderId);
    }

    public OrderBean getByOrderId(String orderId){
        return tbOrderDao.getByOrderId(orderId);
    }

    public List<OrderBean> getMSAliListByCtime(long id,int time ,int pageSize){
        return  tbOrderDao.getMSAliListByCtime(id,time,pageSize);
    }
    
    public double getOrgDiscountPrice(String storeNo,int startTime,int endTime){
    	Double totalPrice = tbOrderDao.getOrgDiscountPrice(storeNo, startTime, endTime);
        return  totalPrice == null ? 0:totalPrice;
    }
    
    public int countOrgDiscountPrice(String storeNo,int startTime,int endTime){
    	Integer num = tbOrderDao.countOrgDiscountPrice(storeNo, startTime, endTime);
        return  num == null ? 0:num;
    }
    
    public CommonResult countToday(String storeNo,String userId,String auth){
		 logger.info("#OrderService.countToday# storeNO={},userId={},auth={}",
	        		storeNo,userId,auth);
		 try{
			 long accountId = Long.parseLong(userId);
			 int startTime = DateUtil.getTodayStartTime();
			 int endTime = startTime + 60*60*24;
			 Map<String, Double> map = tbOrderDao.countByNOAndTime(storeNo, startTime, endTime, accountId);
			 return CommonResult.success("", map);
		 }catch(Exception e){
			 logger.error("#OrderService.countToday# storeNO={},userId={},auth={}",
		        		storeNo,userId,auth,e);
			 return CommonResult.defaultError("出错了");
		 }
	}
    
    public StoreCountResult countStoreToday(String auth){
    	logger.info("#OrderService.countStoreToday# auth={}",auth);
    	StoreCountResult result = new StoreCountResult();
    	try{
    		StoreBean storeBean = LoginUserUtil.getStore();
			if(storeBean == null){
				return result;
			}
			String storeNo = StringUtils.isEmpty(storeBean.getParentNo())?storeBean.getStoreNo():"";
			
			int startTime = DateUtil.getTodayStartTime();
    		int endTime = startTime + 24*60*60-1;
    		
    		result = tbOrderDao.countStore(storeNo, storeBean.getParentNo(), startTime, endTime);
    		
    		int countStoreRefund = tbOrderDao.countStoreRefund(storeNo, storeBean.getParentNo(), startTime, endTime);
    		result.setRefundCount(countStoreRefund);
    		
    		if(StringUtils.isEmpty(storeNo)){
    			result.setUserCount(tbUserBindStoreDao.countByParentNo(storeBean.getParentNo(), startTime, endTime));
    		}else {
    			result.setUserCount(tbUserBindStoreDao.countByStoreNo(storeNo, startTime, endTime));
			}
    		logger.info("#OrderService.countOrgDiscountPrice# auth={} result={}",auth,result);
    	}catch (Exception e) {
			logger.error("#OrderService.countOrgDiscountPrice# auth={}",auth,e);
		}
    	return result;
    }
    
    public CommonResult countShift(String storeNo,String userId,long startTime,long endTime, String auth){
    	 logger.info("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
         		storeNo,userId,startTime,endTime,auth);
    	 try{
    		 
    		 
    		 
    	 }catch (Exception e) {
    		 logger.error("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
    	         		storeNo,userId,startTime,endTime,auth,e);
		}
    	return CommonResult.defaultError("出错了");
    }

}

