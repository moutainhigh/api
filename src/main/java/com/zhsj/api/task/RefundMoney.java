package com.zhsj.api.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.dao.TBOrderRefundDao;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;

/**
 * Created by lcg on 17/1/14.
 */
@Component
@EnableScheduling
public class RefundMoney {
    private static final Logger logger = LoggerFactory.getLogger(RefundMoney.class);
    
    @Autowired
    OrderService orderService;
    @Autowired
    TBOrderRefundDao tbOrderRefundDao;
    
    @Scheduled(cron="0 0 0/1 * * ?")   //每5秒执行一次  
//    @Scheduled(cron="0 0/3 * * * ?")   //每5秒执行一次  
    public void execute() {  
    	logger.info("#RefundMoney.execute#======");
    	try{
    		//查询所有的退款中的一个月单子
    		int todayStartTime = DateUtil.getTodayStartTime();
    		int startTime = todayStartTime - 30*24*60*60;
    		int endTime = (int)DateUtil.unixTime();
    		List<OrderBean> list = orderService.getByStatusAndCtime(3, startTime, endTime);
    		if(!CollectionUtils.isEmpty(list)){
    			for(OrderBean orderBean:list){
    				dealOrder(orderBean);
    			}
    		}
    		
    	}catch (Exception e) {
			logger.error("#RefundMoney.execute# e={}",e,e.getMessage());
		}
    }  
    
    private void dealOrder(OrderBean orderBean){
    	try{
    		CommonResult result = orderService.searchRefund(orderBean.getOrderId());
    		String data = (String)result.getData();
    		if(result.getCode() == 0 && "SUCCESS".equals(data)){
    			//退款成功
    			int num = tbOrderRefundDao.updateStatusByNo(orderBean.getRefundNo(), 5);
    			if(num >=1){
    				orderService.updateOrderByOrderId(4, orderBean.getOrderId());
    			}
    			
    		}
    	}catch (Exception e) {
    		logger.error("#RefundMoney.dealOrder# orderBean={}",orderBean,e);
		}
    }

}
