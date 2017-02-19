package com.zhsj.api.task;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.constants.OrderStatus;
import com.zhsj.api.service.MinshengService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lcg on 17/1/14.
 */
@Service
public class WeChatToken implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(WeChatToken.class);

    public static Map<String,String> TOKEN_MAP = Collections.synchronizedMap(new HashMap<String,String>());
    // 轮询
    private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);

    private boolean loker = false;

    @Autowired
    private WXService wxService;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("#WeChatToken#============");
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
        }, 10, 3600, TimeUnit.SECONDS);
    }

    public void refresh(){
        try {
            if(loker){
                return;
            }
            loker = true;
            String[] appIds = MtConfig.getProperty("weChat_appIds","wx8651744246a92699").split(",");
            for (int i =0 ;i<appIds.length;i++){
                String secret = MtConfig.getProperty(appIds[i],"");
                String token = wxService.getToken(appIds[i], secret);
                if(!"".equals(token)){
                    TOKEN_MAP.put(appIds[i], token);
                }
            }

        }catch (Exception e){
            logger.error("#WeChatToken.refresh# e={}",e.getMessage(),e);
        }finally {
            loker = false;
        }

    }
}
