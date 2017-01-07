package com.zhsj.service;

import com.zhsj.bean.UserBean;
import com.zhsj.bean.UserBindStoreBean;
import com.zhsj.dao.BmOrderDao;
import com.zhsj.dao.BmUserBindStoreDao;
import com.zhsj.dao.BmUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private BmOrderDao bmOrderDao;

    public void updateOrderByOrderId(int status,String orderId){
        bmOrderDao.updateOrderByOrderId(status,orderId);
    }

}

