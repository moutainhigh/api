package com.zhsj.api.task.async;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhsj.api.service.JPushService;
import com.zhsj.api.util.SpringBeanUtil;

/**
 * Created by lcg on 17/1/14.
 */
public class MsgSendFailAsync implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MsgSendFailAsync.class);

    private String orderNo;
    private long accountId;
    
    
    public MsgSendFailAsync(String orderNo,long accountId){
       this.orderNo = orderNo;
       this.accountId = accountId;
    }

    @Override
    public void run() {
        try{
        	JPushService jPushService = (JPushService) SpringBeanUtil.getBean("jPushService");
        	jPushService.sendRefundMsg(orderNo,accountId);
        }catch (Exception e){
            logger.error("#MsgSendFailAsync.run# e={}",e.getMessage(),e);
        }
    }
}
