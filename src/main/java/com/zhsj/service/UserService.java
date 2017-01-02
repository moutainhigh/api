package com.zhsj.service;

import com.zhsj.bean.StoreBean;
import com.zhsj.bean.UserBean;
import com.zhsj.dao.BmUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BmUserDao bmUserDao;

    public UserBean getUserByOpenId(String openId){
        return  bmUserDao.getUserByOpenId(openId);
    }

    public void insertOpenId(String openId){
        UserBean userBean = new UserBean();
        userBean.setOpenId(openId);
        Long num = bmUserDao.insertOpenId(userBean);
        logger.info("num={},id={}",num,userBean.getId());
    }
}

