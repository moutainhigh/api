package com.zhsj.service;

import com.zhsj.bean.StoreBean;
import com.zhsj.bean.UserBean;
import com.zhsj.bean.UserBindStoreBean;
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
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BmUserDao bmUserDao;
    @Autowired
    private BmUserBindStoreDao bmUserBindStoreDao;

    public UserBean getUserByOpenId(String openId,int type){
        return  bmUserDao.getUserByOpenId(openId,type);
    }

    public void insertOpenId(String openId,int type){
        UserBean userBean = new UserBean();
        userBean.setOpenId(openId);
        userBean.setType(type);
        Long num = bmUserDao.insertOpenId(userBean);
        logger.info("num={},id={}",num,userBean.getId());
    }

    public UserBean saveStoreUser(String openId,int type,String storeNo){
        UserBean userBean = bmUserDao.getUserByOpenId(openId,type);
        if (userBean == null){
            userBean = new UserBean();
            userBean.setOpenId(openId);
            userBean.setType(type);
            bmUserDao.insertOpenId(userBean);
        }
        //查询人员与商家绑定关系
        UserBindStoreBean userBindStoreBean = bmUserBindStoreDao.getByStoreAndUser(userBean.getId(), storeNo);
        if(userBindStoreBean == null){
            bmUserBindStoreDao.save(userBean.getId(),type,storeNo);
        }
        return userBean;
    }
}

