package com.zhsj.api.service;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.dao.TBAccountDao;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class AccountService {
    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private TBAccountDao tbAccountDao;

    public int countByOpenId(String openId){
        return tbAccountDao.countByOpenId(openId);
    }

    public int updateOpenId(String account,String password,String openId){
        String pw = Md5.encrypt(password);
        return tbAccountDao.updateOpenId(account, pw, openId);
    }

    public AccountBean getByOpenId(String openId){
        return tbAccountDao.getByOpenId(openId);
    }
}

