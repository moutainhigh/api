package com.zhsj.api.service;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    
    public List<OrderBean> getByStatusAndCtime(int status,int startTime,int endTime){
    	return bmOrderDao.getByStatusAndCtime(status, startTime, endTime);
    }
    
}

