package com.zhsj.api.service;

import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreAccountSignBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.bean.result.StoreCountResult;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreSignDao;
import com.zhsj.api.dao.TbUserBindStoreDao;
import com.zhsj.api.dao.TbUserDao;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.login.LoginUserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private MinshengService minshengService;
    @Autowired
    private PinganService pinganService;
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private TbOrderDao tbOrderDao;
    @Autowired
    private TbUserBindStoreDao tbUserBindStoreDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TBStoreSignDao tbStoreSignDao;

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
			 StoreAccountSignBean storeAccountSignBean = tbStoreSignDao.getLastStoreSign(accountId, 1);
			 if(storeAccountSignBean != null){
				 startTime = storeAccountSignBean.getCtime();
			 }
			 List<Integer> statuses = new ArrayList<>();
			 statuses.add(1);
			 statuses.add(3);
			 statuses.add(4);
			 statuses.add(5);
			 Map<String, Object> countMap = tbOrderDao.countByUserAndTime(storeNo, startTime, endTime, accountId,statuses);
			 Map<String, Object> refundMap = tbOrderDao.countRefundByUserAndTime(storeNo, startTime, endTime, accountId);
			 Map<String,Double> map = new HashMap<String, Double>();
			 map.put("planMoney", ((BigDecimal)countMap.get("planMoney")).doubleValue());
			 map.put("actualMoney", ((BigDecimal)countMap.get("actualMoney")).subtract((BigDecimal)refundMap.get("refundMoney")).doubleValue());
			 return CommonResult.success("", map);
		 }catch(Exception e){
			 logger.error("#OrderService.countToday# storeNO={},userId={},auth={}",
		        		storeNo,userId,auth,e);
			 return CommonResult.defaultError("出错了");
		 }
	}

    public CommonResult refundMoney(String orderNo,double price,int userId){
    	logger.info("#OrderService.refundMoney# orderNo={},price={},userId={}",orderNo,price,userId);
    	try{
    		OrderBean orderBean = bmOrderDao.getByOrderId(orderNo);
    		if(orderBean == null){
    	    	logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"订单号不存在");
    			return CommonResult.defaultError("订单号不存在");
    		}
    		if(orderBean.getRefundMoney() != price || orderBean.getRefundMoney() > orderBean.getActualChargeAmount()){
    	    	logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"退款金额不正确");
    			return CommonResult.defaultError("退款金额不正确");
    		}
    		String result = "Fail";
    		switch(orderBean.getPayType()){
				case 1:
					//官方接口
					if("1".equals(orderBean.getPayMethod())){//微信
						result = weChatService.refundMoney(orderBean,price,userId);
					}else {
		    	    	logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"支付方式不支持");
						return CommonResult.defaultError("支付方式不支持");
					}
					break;
				case 2:
					//民生接口
					result = minshengService.refundMoney(orderBean,price,userId);
					break;
				case 3:
					//平安接口
					result = pinganService.refundMoney(orderBean,price,userId);
					break;
				case 4:
					//中信接口
					result = pinganService.refundMoney(orderBean,price,userId);
					break;
				default:
					logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"支付方式不支持");
					return CommonResult.defaultError("支付方式不支持");
    		}
    		if("SUCCESS".equals(result)){
    			return CommonResult.success("");
    		}else {
    			return CommonResult.defaultError(result);
			}
    		
    	}catch(Exception e){
        	logger.error("#OrderService.refundMoney# orderNo={},price={}",orderNo,price,e);
        	return CommonResult.defaultError("系统异常");
    	}
    }
    
    public CommonResult searchRefund(String orderNo){
    	logger.info("#OrderService.searchRefund# orderNo={}",orderNo);
    	try{
    		OrderBean orderBean = bmOrderDao.getByOrderId(orderNo);
    		if(orderBean == null){
    	    	logger.info("#OrderService.searchRefund# orderNo={},msg={}",orderNo,"订单号不存在");
    			return CommonResult.defaultError("订单号不存在");
    		}
    		String result = "Fail";
    		switch(orderBean.getPayType()){
				case 1:
					//官方接口
					if("1".equals(orderBean.getPayMethod())){//微信
						result = weChatService.searchRefund(orderBean);
					}else {
		    	    	logger.info("#OrderService.searchRefund# orderNo={},msg={}",orderNo,"支付方式不支持");
						return CommonResult.defaultError("支付方式不支持");
					}
					break;
				case 2:
					//民生接口
					result = minshengService.searchRefund(orderBean);
					break;
				case 3:
					//平安接口
					result = pinganService.searchRefund(orderBean);
					break;
				case 4:
					//中信接口
					result = pinganService.searchRefund(orderBean);
					break;
				default:
					logger.info("#OrderService.searchRefund# orderNo={},msg={}",orderNo,"支付方式不支持");
					return CommonResult.defaultError("支付方式不支持");
    		}
    		return CommonResult.success("", result);
    	}catch(Exception e){
        	logger.error("#OrderService.searchRefund# orderNo={},",orderNo,e);
        	return CommonResult.defaultError("系统异常");
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
    
    public CommonResult countShift(String storeNo,String userId,int startTime,int endTime, String auth){
    	 logger.info("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
         		storeNo,userId,startTime,endTime,auth);
    	 try{
    		 ShiftBean bean = new ShiftBean();
    		 bean.setStartTime(DateUtil.getTime(((long)startTime)*1000));
    		 bean.setEndTime(DateUtil.getTime(((long)endTime)*1000));
    		 
    		 long accountId = Long.parseLong(userId);
    		 StoreAccountBean storeAccountBean = tbStoreAccountDao.getById(accountId);
    		 if(storeAccountBean == null){
    			 return CommonResult.defaultError("用户信息出错");
    		 }
    		 bean.setName(storeAccountBean.getName());
    		 if(StringUtils.isEmpty(storeAccountBean.getName())){
    			 bean.setName(storeAccountBean.getAccount());
    		 }
    		 
			 List<Integer> statuses = new ArrayList<>();
			 statuses.add(1);
			 statuses.add(3);
			 statuses.add(4);
			 statuses.add(5);
			 Map<String, Object> countMap = tbOrderDao.countByUserAndTime(storeNo, startTime, endTime, accountId,statuses);
			 Map<String, Object> refundMap = tbOrderDao.countRefundByUserAndTime(storeNo, startTime, endTime, accountId);
			 Map<String, Object> storeMap = tbOrderDao.countStoreDisByUserAndTime(storeNo, startTime, endTime, accountId, statuses);
			 Map<String, Object> orgMap = tbOrderDao.countOrgDisByUserAndTime(storeNo, startTime, endTime, accountId,statuses);

			 bean.setRefundMoney(((BigDecimal)refundMap.get("refundMoney")).doubleValue());
			 bean.setRefundNum((Long)refundMap.get("count"));
			 bean.setStoreDisNum((Long)storeMap.get("count"));
			 bean.setStoreDisMoney(((BigDecimal)storeMap.get("storeDisSum")).doubleValue());
			 bean.setOrgDisNum((Long)orgMap.get("count"));
			 bean.setOrgDisMoney(((BigDecimal)orgMap.get("orgDisSum")).doubleValue());
			 
			 bean.setTotalNum((Long)countMap.get("count"));
			 bean.setTotalMoney(((BigDecimal)countMap.get("planMoney")).doubleValue());
			 
			 bean.setActualMoney(((BigDecimal)countMap.get("actualMoney")).subtract((BigDecimal)refundMap.get("refundMoney")).doubleValue());

			 return CommonResult.success("", bean);
    	 }catch (Exception e) {
    		 logger.error("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
    	         		storeNo,userId,startTime,endTime,auth,e);
		}
    	return CommonResult.defaultError("出错了");
    }

    
    public List<OrderBean> getByStatusAndCtime(int status,int startTime,int endTime){
    	return bmOrderDao.getByStatusAndCtime(status, startTime, endTime);
    }
    
    
    public Object getOrderListByParam(String storeNo,int payMethod, int startTime, int endTime, int status, int page,int pageSize){
    	logger.info("#getOrderListByParam# storeNo = {}, payMethod = {}, startTime= {},endTime={}, status ={},page+{}, pageSize={}",
    			storeNo, payMethod, startTime, endTime, status, page, pageSize);
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("payMethod", payMethod);
    	paramMap.put("startTime", startTime);
    	paramMap.put("endTime", endTime);
    	paramMap.put("status", status);
    	paramMap.put("start", (page-1)*pageSize);
    	paramMap.put("pageSize", pageSize);
    	try {
			paramMap.put("storeNo", storeNo);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<OrderBean> list = bmOrderDao.getListByParamMap(paramMap);
			resultMap.put("list", list);
			if(page == 1){
				int count = bmOrderDao.getCountByParamMap(paramMap);
				resultMap.put("count", count);
			}
			return CommonResult.success("", resultMap);
		} catch (Exception e) {
			logger.error("#getOrderListByParam# storeNo = {}, payMethod = {}, startTime= {},endTime={}, status ={},page+{}, pageSize={}",
	    			storeNo, payMethod, startTime, endTime, status, page, pageSize, e);
			return CommonResult.defaultError("系统异常");
		}
    }
    
    
    public Object serachByOrderIdOrTransId(String storeNo, String orderId, String transId){
    	logger.info("#serachByOrderIdOrTransId# storeNo = {}, orderId = {}, transId = {}", storeNo, orderId, transId);
    	try {
			OrderBean bean = bmOrderDao.getByOrderIdOrTransId(storeNo, orderId, transId);
			return CommonResult.success("", bean);
		} catch (Exception e) {
			logger.error("#serachByOrderIdOrTransId# storeNo = {}, orderId = {}, transId = {}", storeNo, orderId, transId, e);
			return CommonResult.defaultError("系统异常");
		}
    }
    
    public Object appRefund(String orderNo,double price,int userId){
    	
    	return null;
    }
}

