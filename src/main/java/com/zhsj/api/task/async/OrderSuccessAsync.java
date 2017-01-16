package com.zhsj.api.task.async;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcg on 17/1/14.
 */
public class OrderSuccessAsync implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OrderSuccessAsync.class);

    private OrderBean orderBean;

    public OrderSuccessAsync(OrderBean orderBean){
        this.orderBean = orderBean;
    }

    @Override
    public void run() {
        try{
            OrderService orderService = (OrderService) SpringBeanUtil.getBean("orderService");
            int num = orderService.updateOrderByOrderIdAndStatus(1,orderBean.getOrderId(),0);
            logger.info("#OrderSuccessAsync.run# update orderNo={} num={}",orderBean.getOrderId(),num);
            if(num > 0){
                WXService wxService = (WXService)SpringBeanUtil.getBean("WXService");
                wxService.sendSuccess(orderBean);
            }
        }catch (Exception e){
            logger.error("#OrderSuccessAsync.run# e={}",e.getMessage(),e);
        }
    }
}
