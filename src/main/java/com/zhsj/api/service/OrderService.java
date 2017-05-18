package com.zhsj.api.service;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;

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
    private TbOrderDao bmOrderDao;

    public void updateOrderByOrderId(int status,String orderId){
        bmOrderDao.updateOrderByOrderId(status,orderId);
    }

    public int updateOrderByOrderIdAndStatus(int status,String orderId,int preStatus){
        return bmOrderDao.updateOrderByOrderIdIde(status,preStatus,orderId);
    }

    public OrderBean getByOrderId(String orderId){
        return bmOrderDao.getByOrderId(orderId);
    }

    public List<OrderBean> getMSAliListByCtime(long id,int time ,int pageSize){
        return  bmOrderDao.getMSAliListByCtime(id,time,pageSize);
    }
    
    public double getOrgDiscountPrice(String storeNo,int startTime,int endTime){
    	Double totalPrice = bmOrderDao.getOrgDiscountPrice(storeNo, startTime, endTime);
        return  totalPrice == null ? 0:totalPrice;
    }
    
    public int countOrgDiscountPrice(String storeNo,int startTime,int endTime){
    	Integer num = bmOrderDao.countOrgDiscountPrice(storeNo, startTime, endTime);
        return  num == null ? 0:num;
    }
    
    public CommonResult countToday(String storeNo,String userId,String auth){
		 logger.info("#OrderService.countToday# storeNO={},userId={},auth={}",
	        		storeNo,userId,auth);
		 try{
			 long accountId = Long.parseLong(userId);
			 int startTime = DateUtil.getTodayStartTime();
			 int endTime = startTime + 60*60*24;
			 Map<String, Double> map = bmOrderDao.countByNOAndTime(storeNo, startTime, endTime, accountId);
			 return CommonResult.success("", map);
		 }catch(Exception e){
			 logger.error("#OrderService.countToday# storeNO={},userId={},auth={}",
		        		storeNo,userId,auth,e);
			 return CommonResult.defaultError("出错了");
		 }
	}
    

}

