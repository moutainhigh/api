package com.zhsj.api.task;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.zhsj.api.bean.MinMaxBean;
import com.zhsj.api.bean.StoreBalanceDetailBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.dao.TBStoreBalanceDetailsDao;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.StoreService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.DateUtil;

/**
 * Created by lcg on 17/1/14.
 */
@Component
@EnableScheduling
public class PayDiscountMoney {
    private static final Logger logger = LoggerFactory.getLogger(PayDiscountMoney.class);
    
    @Autowired
    StoreService storeService;
    @Autowired
    OrderService orderService;
    @Autowired
    TBStoreBalanceDetailsDao tbStoreBalanceDetailsDao;
    @Autowired
    WXService wxService;
    
    @Scheduled(cron="0 0 8 * * ?")   //每5秒执行一次  
//    @Scheduled(cron="0 0 17 * * ?")   //每5秒执行一次  
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
			//查询过去一天总部做的优惠总额
    		int todayStartTime = DateUtil.getTodayStartTime();
    		int startTime = todayStartTime - 24*60*60;
    		int endTime = todayStartTime - 1;
    		int count = orderService.countOrgDiscountPrice(storeBean.getStoreNo(), startTime, endTime);
    		if(count <=0){
    			return;
    		}
			double totalPrice = orderService.getOrgDiscountPrice(storeBean.getStoreNo(), startTime, endTime);
			if(totalPrice >0){
				//加到流水表
				StoreBalanceDetailBean bean = new StoreBalanceDetailBean();
				bean.setStoreNo(storeBean.getStoreNo());
				bean.setType(1);
				bean.setPaymentStatus(1);
				bean.setPrice(new BigDecimal(totalPrice));
				bean.setDescription("优惠返现");
				int num = tbStoreBalanceDetailsDao.insert(bean);
				if(num > 0){
					//加到总金额中
					 storeService.updatePrice(totalPrice, storeBean.getStoreNo());
					 wxService.sendPayCashMsg(storeBean,count,totalPrice);
				}
			}
    	}catch (Exception e) {
    		logger.error("#PayDiscountMoney.calStore# storeNo={}",storeBean.getStoreNo(),e);
		}
    }

}
