package com.zhsj.api.service;

import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.UserBindStoreBean;
import com.zhsj.api.dao.TbUserBindStoreDao;
import com.zhsj.api.dao.TbUserDao;
import org.apache.commons.lang3.StringUtils;
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
    private TbUserDao bmUserDao;
    @Autowired
    private TbUserBindStoreDao bmUserBindStoreDao;

    public UserBean getUserByOpenId(String openId,int type){
        return  bmUserDao.getUserByOpenId(openId,type);
    }

    public UserBean getUserId(long id){
        return  bmUserDao.getUserById(id);
    }

    public void insertOpenId(String openId,int type){
        UserBean userBean = new UserBean();
        userBean.setOpenId(openId);
        userBean.setType(type);
        Long num = bmUserDao.insertOpenId(userBean);
        logger.info("num={},id={}",num,userBean.getId());
    }



    public UserBean saveStoreUser(String openId,int type,String storeNo,String parentNo){
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
            if(StringUtils.isEmpty(parentNo) || "0".equals(parentNo)){
                parentNo = storeNo;
            }
            bmUserBindStoreDao.save(userBean.getId(),type,storeNo,parentNo);
        }else {
            bmUserBindStoreDao.updateTimeById(userBindStoreBean.getId());
        }
        return userBean;
    }
}

