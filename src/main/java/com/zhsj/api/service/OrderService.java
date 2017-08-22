package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.OrderRefundBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreAccountSignBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.jpush.PaySuccessBean;
import com.zhsj.api.bean.result.OrderSta;
import com.zhsj.api.bean.result.RefundSta;
import com.zhsj.api.bean.result.ShiftNewBean;
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.bean.result.StoreCountResult;
import com.zhsj.api.constants.Const;
import com.zhsj.api.dao.TBOrderDao;
import com.zhsj.api.task.async.MsgSendFailAsync;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.AyncTaskUtil;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.StoreUtils;
import com.zhsj.api.dao.TBModuleBindRoleDao;
import com.zhsj.api.dao.TBOrderRefundDao;
import com.zhsj.api.dao.TBStoreAccountBindRoleDao;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreSignDao;
import com.zhsj.api.dao.TbStoreBindOrgDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.dao.TbUserBindStoreDao;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.net.URLDecoder;
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
    private MinshengService minshengService;
    @Autowired
    private PinganService pinganService;
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private TBOrderDao tbOrderDao;
    @Autowired
    private TbUserBindStoreDao tbUserBindStoreDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TBStoreSignDao tbStoreSignDao;
    @Autowired
    private TBOrderRefundDao tbOrderRefundDao;
    @Autowired
    private StoreService storeService;
    @Autowired
    private TbStoreBindOrgDao tbStoreBindOrgDao;
    @Autowired
    private TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
    @Autowired
    private TBModuleBindRoleDao tbModuleBindRoleDao;
    @Autowired
    private TbStoreDao tbStoreDao;
    @Autowired
    private FuyouService fuyouService;
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;
    @Autowired
    private VPiaotongService vpiaotongService;
    @Autowired
    private AyncTaskUtil ayncTaskUtil;

    public void updateOrderByOrderId(int status,String orderId){
    	tbOrderDao.updateOrderByOrderId(status,orderId);
    }

    public int updateOrderByOrderIdAndStatus(int status,String orderId,int preStatus){
        return tbOrderDao.updateOrderByOrderIdIde(status,preStatus,orderId);
    }

    public OrderBean getByOrderId(String orderId){
        return tbOrderDao.getByOrderId(orderId);
    }
    
    public OrderBean getByTransactionId(String transactionId){
        return tbOrderDao.getByTransactionId(transactionId);
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
    		OrderBean orderBean = tbOrderDao.getByOrderId(orderNo);
    		if(orderBean == null){
    	    	logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"订单号不存在");
    			return CommonResult.defaultError("订单号不存在");
    		}
    		if(orderBean.getRefundMoney() != price || orderBean.getRefundMoney() > orderBean.getActualChargeAmount()){
    	    	logger.info("#OrderService.refundMoney# orderNo={},price={},msg={}",orderNo,price,"退款金额不正确");
    			return CommonResult.defaultError("退款金额不正确");
    		}
    		List<Integer> statusList = new ArrayList<>();
    		statusList.add(1);
    		statusList.add(3);
    		statusList.add(5);
    		double balance = getBalance(orderBean.getId(), statusList);
    		if(Arith.sub(balance, price) < 0){
    			return CommonResult.defaultError("账户余额不足");
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
				case 6:
					//富有接口
					result = fuyouService.refundMoney(orderBean,price,userId);
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
    		OrderBean orderBean = tbOrderDao.getByOrderId(orderNo);
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
				case 6:
					//富友接口
					result = fuyouService.searchRefund(orderBean);
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
			 
			 Map<String, Object> wxMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "1");
			 Map<String, Object> aliMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "2");
			 Map<String, Object> unMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "3");
			 
			 bean.setRefundMoney(bigToDouble(((BigDecimal)refundMap.get("refundMoney"))));
			 bean.setRefundNum((Long)refundMap.get("count"));
			 bean.setStoreDisNum((Long)storeMap.get("count"));
			 bean.setStoreDisMoney(bigToDouble((BigDecimal)storeMap.get("storeDisSum")));
			 bean.setOrgDisNum((Long)orgMap.get("count"));
			 bean.setOrgDisMoney(bigToDouble((BigDecimal)orgMap.get("orgDisSum")));
			 
			 bean.setTotalNum((Long)countMap.get("count"));
			 bean.setTotalMoney(bigToDouble((BigDecimal)countMap.get("planMoney")));
			 
			 bean.setActualMoney(bigToDouble(((BigDecimal)countMap.get("actualMoney")).subtract((BigDecimal)refundMap.get("refundMoney"))));

			 if(wxMap == null){
				 bean.setDisplayWX(0);
			 }else{
				 bean.setDisplayWX(1);
				 bean.setPlanMoneyWX(wxMap.get("planMoney") == null?0:bigToDouble((BigDecimal)wxMap.get("planMoney")));
				 bean.setActualMoneyWX(wxMap.get("actualMoney") == null?0:bigToDouble((BigDecimal)wxMap.get("actualMoney")));
				 bean.setStoreDisMoneyWX(wxMap.get("storeDisMoney") == null?0:bigToDouble((BigDecimal)wxMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyWX(wxMap.get("orgDisMoney") == null?0:bigToDouble((BigDecimal)wxMap.get("orgDisMoney")));
				 bean.setRefundWX(wxMap.get("refundMoney") == null?0:bigToDouble((BigDecimal)wxMap.get("refundMoney")));
				 bean.setActualMoneyWX(Arith.sub(bean.getActualMoneyWX(), bean.getRefundWX()));
			 }
			
			 if(aliMap == null){
				 bean.setDisplayAli(0);
			 }else{
				 bean.setDisplayAli(1);
				 bean.setPlanMoneyAli(aliMap.get("planMoney") == null?0:bigToDouble((BigDecimal)aliMap.get("planMoney")));
				 bean.setActualMoneyAli(aliMap.get("actualMoney") == null?0:bigToDouble((BigDecimal)aliMap.get("actualMoney")));
				 bean.setStoreDisMoneyAli(aliMap.get("storeDisMoney") == null?0:bigToDouble((BigDecimal)aliMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyAli(aliMap.get("orgDisMoney") == null?0:bigToDouble((BigDecimal)aliMap.get("orgDisMoney")));
				 bean.setRefundAli(aliMap.get("refundMoney") == null?0:bigToDouble((BigDecimal)aliMap.get("refundMoney")));
				 bean.setActualMoneyAli(Arith.sub(bean.getActualMoneyAli(), bean.getRefundAli()));
			 }
			 
			 if(unMap == null){
				 bean.setDisplayUCard(0);
			 }else{
				 bean.setDisplayUCard(1);
				 bean.setPlanMoneyUCard(unMap.get("planMoney") == null?0:bigToDouble((BigDecimal)unMap.get("planMoney")));
				 bean.setActualMoneyUCard(unMap.get("actualMoney") == null?0:bigToDouble((BigDecimal)unMap.get("actualMoney")));
				 bean.setStoreDisMoneyUCard(unMap.get("storeDisMoney") == null?0:bigToDouble((BigDecimal)unMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyUCard(unMap.get("orgDisMoney") == null?0:bigToDouble((BigDecimal)unMap.get("orgDisMoney")));
				 bean.setRefundUCard(unMap.get("refundMoney") == null?0:bigToDouble((BigDecimal)unMap.get("refundMoney")));
				 bean.setActualMoneyUCard(Arith.sub(bean.getActualMoneyUCard(), bean.getRefundUCard()));
			 }
			 
			 return CommonResult.success("", bean);
    	 }catch (Exception e) {
    		 logger.error("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
    	         		storeNo,userId,startTime,endTime,auth,e);
		}
    	return CommonResult.defaultError("出错了");
    }

    public CommonResult countNewShift(String storeNo,String userId,int startTime,int endTime, String auth){
   	 logger.info("#OrderService.countNewShift# storeNO={},userId={},startTime={},endTime={},auth={}",
        		storeNo,userId,startTime,endTime,auth);
   	 try{
   		 ShiftNewBean bean = new ShiftNewBean();
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
			 
			 Map<String, Object> wxMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "1");
			 Map<String, Object> aliMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "2");
			 Map<String, Object> unMap = tbOrderDao.countByUserTimeMethod(storeNo, startTime, endTime, accountId, statuses, "3");
			 
			 bean.setRefundMoney(bigToStr(((BigDecimal)refundMap.get("refundMoney"))));
			 bean.setRefundNum(String.valueOf(refundMap.get("count")));
			 bean.setStoreDisNum(String.valueOf(storeMap.get("count")));
			 bean.setStoreDisMoney(bigToStr((BigDecimal)storeMap.get("storeDisSum")));
			 bean.setOrgDisNum(String.valueOf(orgMap.get("count")));
			 bean.setOrgDisMoney(bigToStr((BigDecimal)orgMap.get("orgDisSum")));
			 
			 bean.setTotalNum(String.valueOf(countMap.get("count")));
			 bean.setTotalMoney(bigToStr((BigDecimal)countMap.get("planMoney")));
			 
			 bean.setActualMoney(bigToStr(((BigDecimal)countMap.get("actualMoney")).subtract((BigDecimal)refundMap.get("refundMoney"))));

			 if(wxMap == null){
				 bean.setDisplayWX(0);
			 }else{
				 bean.setDisplayWX(1);
				 bean.setPlanMoneyWX(bigToStr((BigDecimal)wxMap.get("planMoney")));
				 bean.setActualMoneyWX(bigToStr((BigDecimal)wxMap.get("actualMoney")));
				 bean.setStoreDisMoneyWX(bigToStr((BigDecimal)wxMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyWX(bigToStr((BigDecimal)wxMap.get("orgDisMoney")));
				 bean.setRefundWX(bigToStr((BigDecimal)wxMap.get("refundMoney")));
				 bean.setActualMoneyWX(Arith.sub(bean.getActualMoneyWX(), bean.getRefundWX()));
			 }
			
			 if(aliMap == null){
				 bean.setDisplayAli(0);
			 }else{
				 bean.setDisplayAli(1);
				 bean.setPlanMoneyAli(bigToStr((BigDecimal)aliMap.get("planMoney")));
				 bean.setActualMoneyAli(bigToStr((BigDecimal)aliMap.get("actualMoney")));
				 bean.setStoreDisMoneyAli(bigToStr((BigDecimal)aliMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyAli(bigToStr((BigDecimal)aliMap.get("orgDisMoney")));
				 bean.setRefundAli(bigToStr((BigDecimal)aliMap.get("refundMoney")));
				 bean.setActualMoneyAli(Arith.sub(bean.getActualMoneyAli(), bean.getRefundAli()));
			 }
			 
			 if(unMap == null){
				 bean.setDisplayUCard(0);
			 }else{
				 bean.setDisplayUCard(1);
				 bean.setPlanMoneyUCard(bigToStr((BigDecimal)unMap.get("planMoney")));
				 bean.setActualMoneyUCard(bigToStr((BigDecimal)unMap.get("actualMoney")));
				 bean.setStoreDisMoneyUCard(bigToStr((BigDecimal)unMap.get("storeDisMoney")));
				 bean.setOrgDisMoneyUCard(bigToStr((BigDecimal)unMap.get("orgDisMoney")));
				 bean.setRefundUCard(bigToStr((BigDecimal)unMap.get("refundMoney")));
				 bean.setActualMoneyUCard(Arith.sub(bean.getActualMoneyUCard(),bean.getRefundUCard()));
			 }
			 
			 return CommonResult.success("", bean);
   	 }catch (Exception e) {
   		 logger.error("#OrderService.countShift# storeNO={},userId={},startTime={},endTime={},auth={}",
   	         		storeNo,userId,startTime,endTime,auth,e);
		}
   	return CommonResult.defaultError("出错了");
   }
    
    public List<OrderBean> getByStatusAndCtime(int status,int startTime,int endTime){
    	return tbOrderDao.getByStatusAndCtime(status, startTime, endTime);
    }
    
    
    public Object getOrderListByParam(int type,String storeNo,int payChannel,int payMethod, int startTime, int endTime, int status, int page,int pageSize){
    	logger.info("#getOrderListByParam# type={},storeNo = {}, payChanel = {}, payMethod = {}, startTime= {},endTime={}, status ={},page={}, pageSize={}",
    			type,storeNo, payChannel,payMethod, startTime, endTime, status, page, pageSize);
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("payChannel", payChannel);
    	paramMap.put("payMethod", payMethod);
    	paramMap.put("startTime", startTime);
    	paramMap.put("endTime", endTime);
    	paramMap.put("status", status);
    	paramMap.put("start", (page-1)*pageSize);
    	paramMap.put("pageSize", pageSize);
    	try {
    		paramMap.put("isAll",type);
//    		if("-1".equals(storeNo)){
//    			StoreBean storeBean = LoginUserUtil.getStore();
//    			storeNo = storeBean.getStoreNo();
//    		}
			paramMap.put("storeNo", storeNo);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<OrderBean> list = tbOrderDao.getListByParamMap(paramMap);
			resultMap.put("list", list);
			if(page == 1){
				int count = tbOrderDao.getCountByParamMap(paramMap);
				resultMap.put("count", count);
				OrderSta orderSta = tbOrderDao.getOrderStaByParamMap(paramMap);
				RefundSta refundSta = tbOrderDao.getRefundStaByParamMap(paramMap);
				orderSta.setAm(Arith.sub(orderSta.getAm(), refundSta.getRefundMoney()));
				resultMap.put("orderSta", orderSta);
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
    		List<StoreBean> storeBeans = tbStoreDao.getListByParentNo(storeNo);
    		List<String> storeNos = new ArrayList<String>();
    		for(StoreBean sb:storeBeans){
    			storeNos.add(sb.getStoreNo());
    		}
    		storeNos.add(storeNo);
			OrderBean bean = tbOrderDao.getByOrderIdOrTransId(storeNos, orderId, transId);
			if(bean == null){
				return CommonResult.build(2, "订单号不存在");
			}
			if(bean.getStatus() == 3){
				return CommonResult.success("该订单已经在处理中");
			}else if(bean.getStatus() == 4){
				return CommonResult.success("该订单退款成功");
			}else if(bean.getStatus() == 5){
				return CommonResult.success("该订单退款失败");
			}
			
			return CommonResult.success("", bean);
		} catch (Exception e) {
			logger.error("#serachByOrderIdOrTransId# storeNo = {}, orderId = {}, transId = {}", storeNo, orderId, transId, e);
			return CommonResult.defaultError("系统异常");
		}
    }
    
    public CommonResult appRefund(long id,double price,int accountId){
    	logger.info("#appRefund# id={}, price={},accountId={}", id, price, accountId);
    	try {
			OrderBean orderBean = tbOrderDao.getById(id);
			if("3".equals(orderBean.getPayMethod())){
				return CommonResult.build(2, "不支持银联卡退款");
			}
//			String refundNo = "re"+orderBean.getOrderId();
//			OrderRefundBean orderRefund = tbOrderRefundDao.getByRefundNo(refundNo);
//			if(orderRefund != null){
//				return CommonResult.build(2, orderBean.getStatus() == 3?"该订单正在处理中":"该订单已经处理完成");
//			}
			
			if(price > orderBean.getActualChargeAmount()){
				return CommonResult.build(2, "退款金额不能大于实付金额");
			}
			List<Integer> statusList = new ArrayList<>();
			statusList.add(1);
			double balance = getBalance(id, statusList);
			if(Arith.sub(balance, price) < 0){
				return CommonResult.build(2, "退款失败,原因:账户余额不足");
			}
			if(orderBean.getStatus() == 1){
				String refundNo = "re"+orderBean.getOrderId();
				OrderRefundBean orderRefund = tbOrderRefundDao.getByRefundNo(refundNo);
				if(orderRefund == null){
					OrderRefundBean orderRefundBean = new OrderRefundBean();
					orderRefundBean.setRefundNo(refundNo);
					orderRefundBean.setRefundMoney(price);
					orderRefundBean.setSubmitUserId(accountId);
					int reCode = tbOrderRefundDao.insert(orderRefundBean);
					if(reCode != 1){
						logger.info("#appRefund# 添加orderRefundBean出错了");
						return CommonResult.build(2, "系统异常");
					}
				}
				//更新status为3订单处理中
				int code = tbOrderDao.updateOrderRefundById(orderBean.getId(), refundNo, price, 3);
				if(code != 1){
					logger.info("#appRefund# 更新order出错了");
					return CommonResult.build(2, "系统异常");
				}
				CommonResult commonResult = refundMoney(orderBean.getOrderId(),price,accountId);
				if(commonResult.getCode() == 0){
//					jPushService.sendRefundMsg(orderBean.getOrderId(),accountId);
					MsgSendFailAsync msgSendFailAsync = new MsgSendFailAsync(orderBean.getOrderId(),accountId);
					ayncTaskUtil.commitAyncTask(msgSendFailAsync);
					CommonResult commonResult2 = searchRefund(orderBean.getOrderId());
					if(commonResult2.getCode() == 0 && "SUCCESS".equals(commonResult2.getData())){
						int sCode = tbOrderDao.updateStatusById(orderBean.getId(), 4);
						if(sCode != 1){
							logger.info("#appRefund# 退款成功。更新order状态失败");
							return CommonResult.build(2, "系统异常");
						}
						int reSCode = tbOrderRefundDao.updateStatusByNo(refundNo, 5);
						if(reSCode != 1){
							logger.info("#appRefund# 退款成功。更新orderRefund状态失败");
							return CommonResult.build(2, "系统异常");
						}
						return CommonResult.success("退款成功");
					}else{
						int rCode = tbOrderRefundDao.updateStatusByNo(refundNo, 2);
						if(rCode != 1){
							logger.info("#appRefund# 退款中。更新OrderRefund状态失败");
							return CommonResult.build(2, "系统异常");
						}
						return CommonResult.success("退款中");
					}
				}else{
					//更新status为初始状态1
					int failCode = tbOrderDao.updateOrderRefundById(orderBean.getId(),"",0.00,1);
					if(failCode != 1){
						logger.info("#appRefund# 退款失败 。更新order状态失败");
						return CommonResult.build(2, "系统异常");
					}
					//更新status为初始状态1
					int reFailCode = tbOrderRefundDao.updateStatusByNo(refundNo, 1);
					if(reFailCode != 1){
						logger.info("#appRefund# 退款失败 。 更新refundorder状态失败");
						return CommonResult.build(2, "系统异常");
					}
					return CommonResult.defaultError("退款失败,原因:"+commonResult.getMsg());
				}
			}else if(orderBean.getStatus() == 3){
				return CommonResult.build(2, "该订单正在退款中");
			}else if(orderBean.getStatus() == 4){
				return CommonResult.build(2, "该订单已经退款成功");
			}else if(orderBean.getStatus() == 5){
				return CommonResult.build(2, "该订单已经退款失败");
			}else{
				return CommonResult.build(2, "该订单出错了");
			}
		} catch (Exception e) {
			logger.error("#appRefund# id={}, price={},accountId={}", id, price, accountId, e);
			return CommonResult.defaultError("系统异常");
		}
    }
    
    //获取账户余额
    private double getBalance(long id,List<Integer> statusList){
    	logger.info("#getBalance# id = {}", id);
    	OrderBean orderBean = tbOrderDao.getById(id);
    	String storeNo = orderBean.getStoreNo();
    	int payType = orderBean.getPayType();
    	String payMethod = orderBean.getPayMethod();
    	StorePayInfo storePayInfo = tbStorePayInfoDao.getByStoreNoAndTypeAndMethod(storeNo, payType, payMethod);
    	List<String> storeNos = new ArrayList<>();
    	if(StringUtils.isNotEmpty(storePayInfo.getField1()) && payType != 1 && payType != 6){
    		String field1 = storePayInfo.getField1();
    		storeNos = tbStorePayInfoDao.getStoreNosByField1(field1);
    	}else if(StringUtils.isNotEmpty(storePayInfo.getMchId()) && payType == 1 && "1".equals(payMethod)){
    		String mchId = storePayInfo.getMchId();
    		storeNos = tbStorePayInfoDao.getStoreNosByMchId(mchId);
    	}else if(StringUtils.isNotEmpty(storePayInfo.getMchId()) && payType == 6 ){
    		String mchId = storePayInfo.getMchId();
    		storeNos = tbStorePayInfoDao.getStoreNosByMchId(mchId);
    	}
    	if(CollectionUtils.isEmpty(storeNos)){
    		return 0;
    	}
    	int todayStartTime = DateUtil.getTodayStartTime();
    	int todayEndTime = todayStartTime + 24*60*60-1;
    	Map<String, Object> amMap = tbOrderDao.getAmSum(storeNos,todayStartTime,todayEndTime,payType,payMethod, statusList);
    	BigDecimal amSum = (BigDecimal) amMap.get("amsum");
    	return amSum.doubleValue();
    }
    
    public Map<String, Object> getTodaySta(){
    	logger.info("#getTodaySta#");
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			int startTime = DateUtil.getTodayStartTime();
			int endTime = startTime+24*60*60-1;
			OrderSta orderSta = tbOrderDao.getTodayOrderSta(storeNo, startTime, endTime);
			RefundSta refundSta = tbOrderDao.getTodayRefundSta(storeNo, startTime, endTime);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("orderSta", orderSta);
			resultMap.put("refundSta", refundSta);
			resultMap.put("am", Arith.sub(orderSta.getAm(), refundSta.getRefundMoney()));
			return resultMap;
		} catch (Exception e) {
			logger.error("#getTodaySta#", e);
		}
    	return null;
	}

    public CommonResult savePreOrder(String userId,String storeNo,int planAmount,int actualmount,int payType,int payMethod,int channel,String auth){
    	logger.info("#OrderService.savePreOrder# userId={},storeNo,palnAmount={},actualAmount={},payType={},payMethod={},channel={},auth={}",
        											userId,storeNo,planAmount,actualmount,payType,payMethod,channel,auth);
    	try{
    		StoreBean storeBean = storeService.getStoreByNO(storeNo);
    		if(storeBean == null){
    			return CommonResult.defaultError("商家不存在");
    		}
    		Long orgId = tbStoreBindOrgDao.getOrgIdByStoreNO(storeNo);
    		String orderNo = StoreUtils.getOrderNO(storeNo);
    		//保存定单
			OrderBean orderBean = new OrderBean();
			orderBean.setOrderId(orderNo);
			orderBean.setActualChargeAmount(Arith.div(actualmount, 100.00));
			orderBean.setPlanChargeAmount(Arith.div(planAmount, 100.00));
			orderBean.setStatus(0);
			orderBean.setDiscountType(0);
			orderBean.setDiscountId(0);
			orderBean.setPayType(payType);
			orderBean.setPayMethod(String.valueOf(payMethod));
			orderBean.setStoreNo(storeNo);
			if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
				orderBean.setParentStoreNo(storeBean.getStoreNo());
			}else {
				orderBean.setParentStoreNo(storeBean.getParentNo());
			}
			orderBean.setOrgId(orgId);
			orderBean.setUserId(-1);
			orderBean.setOrgIds(storeBean.getOrgIds());
			orderBean.setSaleId(storeBean.getSaleId());

			orderBean.setStoreDiscountPrice(0.0);
			orderBean.setOrgDiscountPrice(0.0);
			orderBean.setDiscountDetail("");
			
			orderBean.setAccountId(Long.parseLong(userId));
			orderBean.setPayChannel(channel);
			int num = tbOrderDao.insertOrder(orderBean);
			if(num >=1){
				Map<String, String> data = new HashMap<>();
				data.put("orderNo", orderNo);
				data.put("callback", MtConfig.getProperty("UNIONPAY_CALLBACK", ""));
				data.put("actualAmount", actualmount+"");
				return CommonResult.success("", data);
			}else{
				logger.error("#OrderService.savePreOrder# msg={},userId={},storeNo={},actualAmount={}","保存订单失败",userId,storeNo,actualmount);
				return CommonResult.defaultError("保存订单失败");
			}
    	}catch(Exception e){
    		logger.error("#OrderService.savePreOrder# userId={},storeNo={},palnAmount={},actualAmount={},payType={},payMethod={},channel={},auth={}",
					userId,storeNo,planAmount,actualmount,payType,payMethod,channel,auth,e);
    		return CommonResult.defaultError("系统出错");
    	}
    	
    }
    
    public CommonResult updateOrderStatus(String userId,String storeNo,String orderNo,String cashierTradeNo,int status,String auth) {
        logger.info("#OrderService.updateOrderStatus# userId={},storeNo={},orderNo={},cashierTradeNo={},status={},auth={}",
        											userId,storeNo,orderNo,cashierTradeNo,status,auth);
        try{
        	StoreBean storeBean = storeService.getStoreByNO(storeNo);
    		if(storeBean == null){
    			return CommonResult.defaultError("商家不存在");
    		}
    		int num = tbOrderDao.updateByAccount(status, 0, orderNo, Long.parseLong(userId), cashierTradeNo);
    		if(num > 0){
    			return CommonResult.success("更新成功");
    		}
    		
    		OrderBean orderBean = tbOrderDao.getByOrderId(orderNo);
    		if(orderBean.getStatus() == status){
    			return CommonResult.success("更新成功");
    		}
    		return CommonResult.success("更新失败");
        }catch (Exception e) {
        	logger.error("#OrderService.updateOrderStatus# userId={},storeNo={},orderNo={},cashierTradeNo={},status={},auth={}",
					userId,storeNo,orderNo,cashierTradeNo,status,auth,e);
		}
        return CommonResult.defaultError("系统异常");
    }
    
    public CommonResult refundUnionpay(String userId,String storeNo,int type,String orderNo,String cashierTradeNo,String auth){
    	 logger.info("#OrderService.refundUnionpay# userId={},storeNo={},orderNo={},cashierTradeNo={},auth={}",
					userId,storeNo,orderNo,cashierTradeNo,auth);
    	 try{
    		 auth = URLDecoder.decode(auth, "utf-8");
			 String[] args = auth.split(",");
			 
			 List<Integer> moduleIds = new ArrayList<>();
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(Long.parseLong(args[1]));
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 
			 int refundRole = Integer.parseInt(MtConfig.getProperty("STORE_REFUND_MODULE_ID", "0"));
			 if(!moduleIds.contains(refundRole)){
				return CommonResult.build(2, "没有权限操作");
			 }
    		 if(type == 1){
    			 if(cashierTradeNo.startsWith("09")){
					cashierTradeNo = cashierTradeNo.substring(2);
				} 
    		 }
			
    		OrderBean bean = tbOrderDao.getByTypeOrderIdOrTransId(storeNo, orderNo, cashierTradeNo,type);
 			if(bean == null){
 				return CommonResult.build(2, "订单号不存在");
 			}
 			if(bean.getStatus() == 3){
 				return CommonResult.success("该订单已经在处理中");
 			}else if(bean.getStatus() == 4){
 				return CommonResult.success("该订单退款成功");
 			}else if(bean.getStatus() == 5){
 				return CommonResult.success("该订单退款失败");
 			}
 			
 			//保存定单信息ss
 			OrderRefundBean orderRefundBean = new OrderRefundBean();
			orderRefundBean.setRefundNo("pre"+bean.getOrderId());
			orderRefundBean.setRefundMoney(bean.getActualChargeAmount());
			orderRefundBean.setSubmitUserId(Long.parseLong(userId));
			
			OrderRefundBean refundBean = tbOrderRefundDao.getByRefundNo("pre"+bean.getOrderId());
			if(refundBean == null){
				int reCode = tbOrderRefundDao.insert(orderRefundBean);
				if(reCode != 1){
					logger.info("#appRefund# 添加orderRefundBean出错了");
					return CommonResult.build(2, "系统异常");
				}
			}
			return CommonResult.success("", bean.getTransactionId());
    	 }catch (Exception e) {
    		 logger.error("#OrderService.refundUnionpay# userId={},orderNo={},cashierTradeNo={},auth={}",
 					userId,orderNo,cashierTradeNo,auth,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    public CommonResult refundUP(String userId,String storeNo,int type,String orderNo,String cashierTradeNo,String auth){
   	 logger.info("#OrderService.refundUP# userId={},storeNo={},type={},orderNo={},cashierTradeNo={},auth={}",
					userId,storeNo,type,orderNo,cashierTradeNo,auth);
   	 try{
   		 auth = URLDecoder.decode(auth, "utf-8");
			 String[] args = auth.split(",");
			 
			 List<Integer> moduleIds = new ArrayList<>();
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(Long.parseLong(args[1]));
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 
			 int refundRole = Integer.parseInt(MtConfig.getProperty("STORE_REFUND_MODULE_ID", "0"));
			 if(!moduleIds.contains(refundRole)){
				return CommonResult.build(2, "没有权限操作");
			 }
   		 
			if(cashierTradeNo.startsWith("09")){
				cashierTradeNo = cashierTradeNo.substring(2);
			} 
			OrderBean bean = tbOrderDao.getByTypeOrderIdOrTransId(storeNo, orderNo, cashierTradeNo,type);
			if(bean == null){
				return CommonResult.build(2, "订单号不存在");
			}
			if(bean.getStatus() == 3){
				return CommonResult.success("该订单已经在处理中");
			}else if(bean.getStatus() == 4){
				return CommonResult.success("该订单退款成功");
			}else if(bean.getStatus() == 5){
				return CommonResult.success("该订单退款失败");
			}
			
			//保存定单信息
			OrderRefundBean orderRefundBean = new OrderRefundBean();
			orderRefundBean.setRefundNo("pre"+bean.getOrderId());
			orderRefundBean.setRefundMoney(bean.getActualChargeAmount());
			orderRefundBean.setSubmitUserId(Long.parseLong(userId));
			
			OrderRefundBean refundBean = tbOrderRefundDao.getByRefundNo("pre"+bean.getOrderId());
			if(refundBean == null){
				int reCode = tbOrderRefundDao.insert(orderRefundBean);
				if(reCode != 1){
					logger.info("#appRefund# 添加orderRefundBean出错了");
					return CommonResult.build(2, "系统异常");
				}
			}
			Map<String,String> map = new HashMap<>();
			map.put("orderNo", bean.getOrderId());
			map.put("amount",String.valueOf((int)(Arith.mul(bean.getActualChargeAmount(),100))));
			map.put("transactionId", bean.getTransactionId());
			return CommonResult.success("",map);
   	 }catch (Exception e) {
   		 logger.error("#OrderService.refundUnionpay# userId={},orderNo={},cashierTradeNo={},auth={}",
					userId,orderNo,cashierTradeNo,auth,e);
		}
   	return CommonResult.defaultError("系统异常");
   }
    
    
    public CommonResult refundUPFY(String userId,String storeNo,String cashierTradeNo,double price,String auth){
      	 logger.info("#OrderService.refundUPFY# userId={},storeNo={},cashierTradeNo={},price={},auth={}",
   					userId,storeNo,cashierTradeNo,price,auth);
      	 try{
      		 auth = URLDecoder.decode(auth, "utf-8");
   			 String[] args = auth.split(",");
   			 
   			 List<Integer> moduleIds = new ArrayList<>();
   			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(Long.parseLong(args[1]));
   			 if(!CollectionUtils.isEmpty(roleIds)){
   				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
   				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
   			 }
   			 
   			 int refundRole = Integer.parseInt(MtConfig.getProperty("STORE_REFUND_MODULE_ID", "0"));
   			 if(!moduleIds.contains(refundRole)){
   				return CommonResult.build(2, "没有权限操作");
   			 }
      		 
   			OrderBean bean = tbOrderDao.getByTransactionIdAndStoreNo(cashierTradeNo, storeNo);
   			if(bean == null){
   				return CommonResult.build(2, "订单号不存在");
   			}
   			if(bean.getStatus() == 3){
   				return CommonResult.success("该订单已经在处理中");
   			}else if(bean.getStatus() == 4){
   				return CommonResult.success("该订单退款成功");
   			}else if(bean.getStatus() == 5){
   				return CommonResult.success("该订单退款失败");
   			}
   			
   			if(bean.getActualChargeAmount() < price){
   				return CommonResult.success("退款金额不能大于实付金额");
   			}
   			
   			//保存定单信息
   			OrderRefundBean orderRefundBean = new OrderRefundBean();
   			orderRefundBean.setRefundNo("pre"+bean.getOrderId());
   			orderRefundBean.setRefundMoney(bean.getActualChargeAmount());
   			orderRefundBean.setSubmitUserId(Long.parseLong(userId));
   			
   			OrderRefundBean refundBean = tbOrderRefundDao.getByRefundNo("pre"+bean.getOrderId());
   			if(refundBean == null){
   				int reCode = tbOrderRefundDao.insert(orderRefundBean);
   				if(reCode != 1){
   					logger.info("#appRefund# 添加orderRefundBean出错了");
   					return CommonResult.build(2, "系统异常");
   				}
   			}
   			Map<String,String> map = new HashMap<>();
   			map.put("orderNo", bean.getOrderId());
   			map.put("amount",String.valueOf((int)(Arith.mul(price,100))));
   			map.put("transactionId", bean.getTransactionId());
   			return CommonResult.success("",map);
      	 }catch (Exception e) {
      		 logger.error("#OrderService.refundUPFY# userId={},cashierTradeNo={},auth={}",
   					userId,cashierTradeNo,auth,e);
   		}
      	return CommonResult.defaultError("系统异常");
      }
    
    public CommonResult refundSuccess(String userId,String storeNo,String orderNo,String cashierTradeNo,String auth) {
        logger.info("#OrderService.refundSuccess# userId={},storeNo={},orderNo={},cashierTradeNo={},auth={}",
				userId,storeNo,orderNo,cashierTradeNo,auth);
        try{
        	List<String> storeNoList = new ArrayList<>();
        	storeNoList.add(storeNo);
        	OrderBean bean = tbOrderDao.getByOrderIdOrTransId(storeNoList, orderNo, cashierTradeNo);
        	if(bean == null){
 				return CommonResult.build(2, "订单不存在");
 			}
        	int reCode = tbOrderRefundDao.updateStatusAndOrderNo("re"+bean.getOrderId(), "pre"+bean.getOrderId(), 5, bean.getActualChargeAmount());
			if(reCode != 1){
				logger.info("#OrderService.refundSuccess# 更新orderRefundBean出错了");
				return CommonResult.build(2, "系统异常");
			}
			int code = tbOrderDao.updateStatusAndMoney(bean.getId(), 4,bean.getActualChargeAmount(),"re"+bean.getOrderId());
			if(code != 1){
				logger.info("#OrderService.refundSuccess#  更新order出错了");
				return CommonResult.build(2, "系统异常");
			}
			return CommonResult.success("");
        }catch (Exception e) {
        	logger.error("#OrderService.refundSuccess# userId={},storeNo={},cashierTradeNo={},auth={}",
    				userId,storeNo,cashierTradeNo,auth,e);
        }
        return CommonResult.defaultError("系统异常");
    }
    
    public boolean callbackWPOS(String content){
    	boolean flag = false;
    	logger.info("#OrderService.callbackWPOS# content={}",content);
    	try{
    		Map<String,String> map = new HashMap<>();
    		String[] args = content.split("&");
    		for(int i = 0;i<args.length;i++){
    			map.put(args[i].split("=")[0], URLDecoder.decode(args[i].split("=")[1]));
    		}
    		if("PAY".equals(map.get("trade_status"))){
    			String orderNo = map.get("out_trade_no");
    			String transactionId = map.get("cashier_trade_no");
//    			String buyer = map.get("buyer");
    			
    			OrderBean bean = tbOrderDao.getByOrderId(orderNo);
    			StoreBean storeBean = tbStoreDao.getStoreByNo(bean.getStoreNo());
    			//添加用户
//    			UserBean userBean = userService.saveStoreUser(buyer,3,storeBean.getStoreNo(),storeBean.getParentNo(),"",0);
    			//更新表信息
    			int num = tbOrderDao.updateStatus(bean.getId(), 1, 0, transactionId, -1);
//    			if(num <=0){
//    				tbOrderDao.updateUser(bean.getId(), transactionId, -1);
//    			}
    			flag = true;
    		}
    	}catch (Exception e) {
    		logger.error("#OrderService.callbackWPOS# content={}",content,e);
		}
    	return flag;
    }
    
    public boolean callbackFY(String content){
    	boolean flag = false;
    	logger.info("#OrderService.callbackFY# content={}",content);
    	try{
    		Map<String,String> map = JSON.parseObject(content,Map.class);
    		String out_trade_no = map.get("out_trade_no");
    		String terminal_id = map.get("terminal_id");// 终端ID
    		String terminal_trace = map.get("terminal_trace");// 凭证号
    		String pay_type = map.get("pay_type");  //交易类型： 1 微信 2支付宝 3银行卡 4现金 5无卡支付 6qq钱包 7百度钱包8京东钱包 
    		String pay_status = map.get("pay_status"); //交易状态描述：1.支付成功，2退款成功，3撤销成功，4冲正成功
    		String card_type = map.get("card_type");//卡属性, 卡属性, 01借记卡, 02信用卡, 03准贷记卡,04预付卡
    		if(!"3".equals(pay_type)){
    			return true;
    		}
    		if("1".equals(pay_status)){
    			if(StringUtils.isEmpty(terminal_id)){
    				return false;
    			}
    			String transactionId = terminal_id+"-"+terminal_trace;
    			OrderBean  orderBean = tbOrderDao.getByTransactionId(transactionId);
    			if(orderBean == null){
    				return false;
    			}
    			
    			StorePayInfo payInfo = tbStorePayInfoDao.getByStoreNoAndTypeAndMethod(orderBean.getStoreNo(), 6, "3");
    			if(payInfo == null){
    				return false;
    			}
    			String rateStr = "";
    			if("01".equals(card_type)){
    				//01借记卡
    				rateStr = payInfo.getField1();
    			}else if("02".equals(card_type)){
//    				02信用卡
    				rateStr = payInfo.getField2();
    			}else{
    				return false;
    			}
    			
    			if(StringUtils.isEmpty(rateStr)){
    				return false;
    			}
    			String[] args = rateStr.split(",");
    			double rate = Double.parseDouble(args[0]);
    			
    			double serviceChange = Arith.div(Arith.mul(orderBean.getActualChargeAmount(), rate),100);
    			if(args.length > 1){
    				double highCharge = Double.parseDouble(args[2]);
    				if(serviceChange > highCharge ){
    					serviceChange = highCharge;
    				}
    			}
    			tbOrderDao.updateFYUP(1, transactionId, card_type, serviceChange, rate,out_trade_no);
    		}else if("3".equals(pay_status) || "2".equals(pay_status)){
    			
    		}
    	}catch (Exception e) {
    		logger.error("#OrderService.callbackWPOS# content={}",content,e);
		}
    	return flag;
    }
    
    public OrderBean getOrder(String mchnt_order_no,String transaction_id,String wwt_order_no,String storeNo,String orderType){
    	return tbOrderDao.getBy3Id( mchnt_order_no, transaction_id, wwt_order_no,storeNo,orderType);
    }
    
    public static void main(String[] args) {
		new OrderService().callbackWPOS("");
	}
    
    private double bigToDouble(BigDecimal bigd){
    	if(bigd == null){
    		return 0.0;
    	}
    	bigd = bigd.setScale(2,BigDecimal.ROUND_HALF_UP);
    	return bigd.doubleValue();
    }
    
    public PaySuccessBean getPaySuccessBean(OrderBean bean){
    	String qr = vpiaotongService.getStoreQRCode(bean.getStoreNo(), bean.getOrderId(), bean.getActualChargeAmount());
    	String desc = "";
    	if(StringUtils.isNotEmpty(qr)){
    		desc = Const.ELE_INVOICE_DESC;
    	}
		String apiUri = MtConfig.getProperty("API_URL", "");
		return new PaySuccessBean().toBean(bean, qr,apiUri,desc);
    }
    
    private String bigToStr(BigDecimal bigd){
    	if(bigd == null){
    		return "0";
    	}
    	bigd = bigd.setScale(2,BigDecimal.ROUND_HALF_UP);
    	return bigd.toString();
    }
    
}

