package com.zhsj.api.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.zhsj.api.bean.MinMaxBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.service.StoreService;

/**
 * Created by lcg on 17/1/14.
 */
@Component
@EnableScheduling
public class PayDiscountMoney {
    private static final Logger logger = LoggerFactory.getLogger(PayDiscountMoney.class);
    
    @Autowired
    StoreService storeService;
    
    @Scheduled(cron="0 0 8 * * ?")   //每5秒执行一次  
//    @Scheduled(cron="0/5 * * * * ?")   //每5秒执行一次  
    public void execute() {  
    	logger.info("#PayDiscountMoney.execute#======");
    	try{
    		//查询所有商家分页查询
    		MinMaxBean minMaxBean = storeService.getMaxMin();
    		int id = minMaxBean.getMin();
    		int size = 300;
    		while(id <= minMaxBean.getMax()){
    			int minId = id;
    			id = id + size;
    			int maxId = id;
    			List<StoreBean> storeBeans = storeService.getStoreByLimitId(minId,maxId);
    			if(CollectionUtils.isEmpty(storeBeans)){
    				continue;
    			}
    			for(StoreBean bean:storeBeans){
    				this.calStore(bean);
        		}
    		}
    	}catch (Exception e) {
			logger.error("#PayDiscountMoney.execute# e={}",e,e.getMessage());
		}
    }  
    
    private void calStore(StoreBean storeBean){
    	try{
    		//查询过去一天的订单有优惠的订单分页查询
			
			//判断是否是总部做的优惠
			
			//++++
			
			//加到流水表
			
			//加到总金额中
			
			//发消息
    	}catch (Exception e) {
    		logger.error("#PayDiscountMoney.calStore# storeNo={}",storeBean.getStoreNo(),e);
		}
    }

}
