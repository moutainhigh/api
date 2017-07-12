package com.zhsj.api.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.wangpos.wopensdk.model.ReturnData;
import com.wangpos.wopensdk.tools.WPosOpenRequest;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.OrderRefundBean;
import com.zhsj.api.dao.TBOrderRefundDao;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;

/**
 * Created by lcg on 17/1/14.
 */
@Component
@EnableScheduling
public class RefundMoneyWPOS {
    private static final Logger logger = LoggerFactory.getLogger(RefundMoneyWPOS.class);
    
    @Autowired
    OrderService orderService;
    @Autowired
    TBOrderRefundDao tbOrderRefundDao;
    
    @Scheduled(cron="0 0 0/1 * * ?")   //每5秒执行一次  
//    @Scheduled(cron="0 0/3 * * * ?")   //每5秒执行一次  
    public void execute() {  
    	logger.info("#RefundMoneyWPOS.execute#======");
    	try{
    		//查询所有的退款中的一个月单子
    		int endTime = (int) DateUtil.unixTime();
    		int startTime = endTime - 60*60;
    		List<OrderRefundBean> list = tbOrderRefundDao.getPreRefund(startTime, endTime);
    		if(!CollectionUtils.isEmpty(list)){
    			String appId = MtConfig.getProperty("WPOS_APP_ID", "");
        		String secret = MtConfig.getProperty("WPOS_SECRET", "");
        		String token = MtConfig.getProperty("WPOS_TOKEN", "");
        		WPosOpenRequest wPosOpenRequest = new WPosOpenRequest(appId, secret, token);
    			for(OrderRefundBean refundBean:list){
    				dealOrder(refundBean,wPosOpenRequest);
    				Thread.sleep(1000*10);
    			}
    		}
    		
    	}catch (Exception e) {
			logger.error("#RefundMoneyWPOS.execute# e={}",e,e.getMessage());
		}
    }  
    
    private void dealOrder(OrderRefundBean refundBean,WPosOpenRequest wPosOpenRequest){
    	try{
    		String orderNo = refundBean.getRefundNo().substring(3);
    		HashMap<String, Object> paramMap = new HashMap<String, Object>();
    		
    	    paramMap.put("out_trade_no", orderNo);
    		ReturnData returnData = wPosOpenRequest.requestOpenApi("cashier.api.query", paramMap);
    		if(returnData != null && returnData.getStatus() == 0){
    			String data = returnData.getData();
    			Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
    			if("REFUND".equals((String)dataMap.get("trade_status"))){
    				String cashierTradeNo = (String)dataMap.get("cashier_trade_no");
    				OrderBean orderBean = orderService.getByOrderId(orderNo);
    				orderService.refundSuccess("-1", orderBean.getStoreNo(),orderBean.getOrderId(), cashierTradeNo, "");
    			}
    		}

    	}catch (Exception e) {
    		logger.error("#RefundMoneyWPOS.dealOrder# orderBean={}",refundBean,e);
		}
    }
    
    public static void main(String[] args) throws InterruptedException {
    	OrderRefundBean bean = new OrderRefundBean();
    	bean.setRefundNo("pre"+"88888888170704420311293");
    	
    	OrderRefundBean bean1 = new OrderRefundBean();
    	bean1.setRefundNo("pre"+"88888888170704426724379");
    	List<OrderRefundBean> list = new ArrayList<>();
    	list.add(bean);
    	list.add(bean1);
//		new RefundMoneyWPOS().dealOrder(bean);
		String appId = MtConfig.getProperty("WPOS_APP_ID", "");
		String secret = MtConfig.getProperty("WPOS_SECRET", "");
		String token = MtConfig.getProperty("WPOS_TOKEN", "");
		WPosOpenRequest wPosOpenRequest = new WPosOpenRequest(appId, secret, token);
		for(OrderRefundBean refundBean:list){
			new RefundMoneyWPOS().dealOrder(refundBean,wPosOpenRequest);
			Thread.sleep(1000*10);
		}
	}

}
