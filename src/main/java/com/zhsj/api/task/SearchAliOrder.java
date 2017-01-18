package com.zhsj.api.task;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.constants.OrderStatus;
import com.zhsj.api.service.MinshengService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lcg on 17/1/14.
 */
@Service
public class SearchAliOrder implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SearchAliOrder.class);

    // 轮询
    private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);
    // 轮询间隔
    private long refreshPeriod = 300; // seconds

    private long id;
    private boolean loker = false;

    @Autowired
    private OrderService orderService;
    @Autowired
    private MinshengService minshengService;
    @Autowired
    private WXService wxService;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("#SearchOrder#============");
        refresh();
        refreshExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (Exception e) {
                    logger.error("pull failed!", e);
                }
            }
        }, 60, refreshPeriod, TimeUnit.SECONDS);
    }

    public void refresh(){
        logger.info("#SearchOrder#============");
        try {
            if(loker){
                return;
            }

            loker = true;
            long ctime = 0;
            if(id <=0 ){
                ctime = DateUtil.unixTime() - 60*5;
            }
            List<OrderBean> list = orderService.getMSAliListByCtime(id,new Long(ctime).intValue(),100);
            for(OrderBean orderBean:list){
            	logger.info("#refresh# orderNo={}",orderBean.getOrderId());
                String result = minshengService.queryOrder(orderBean.getOrderId());
                if(StringUtils.isEmpty(result)){
                    continue;
                }
                if(OrderStatus.FAIL.equals(result) || OrderStatus.NOTPAY.equals(result)){
                    orderService.updateOrderByOrderIdAndStatus(2, orderBean.getOrderId(), 0);
                }else if(OrderStatus.SUCCESS.equals(result)){
                    int num = orderService.updateOrderByOrderIdAndStatus(1,orderBean.getOrderId(),0);
                    if(num > 0){
                        wxService.sendSuccess(orderBean);
                    }
                }
                id = orderBean.getId();
            }
        }catch (Exception e){
            logger.error("#SearchAliOrder.refresh# e={}",e.getMessage(),e);
        }finally {
            loker = false;
        }

    }
}
