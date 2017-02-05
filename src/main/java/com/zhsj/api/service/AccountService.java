package com.zhsj.api.service;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.TBAccountDao;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private WXService wxService;

    public int countByOpenId(String openId){
        return tbAccountDao.countByOpenId(openId);
    }

    public int updateOpenId(String account,String password,String openId){
        logger.info("#AccountService.updateOpenId# account={},password={},openId={}",account,password,openId);
        try{
            LoginUser loginUser = LoginUserUtil.getLoginUser();
            String name = loginUser.getName();
            String headImg = loginUser.getHeadImg();
            if(!StringUtils.isEmpty(openId) && (StringUtils.isEmpty(name) || StringUtils.isEmpty(headImg))){
                WeixinUserBean weixinUserBean = wxService.getWeixinUser(openId);
                if(weixinUserBean != null){
                    name = StringUtils.isEmpty(name)?weixinUserBean.getNickname():name;
                    headImg = StringUtils.isEmpty(headImg)?weixinUserBean.getHeadimgurl():headImg;
                }
            }
            String pw = Md5.encrypt(password);
            return tbAccountDao.updateOpenId(account, pw, openId,name,headImg);
        }catch (Exception e){
            logger.error("#AccountService.updateOpenId# account={},password={},openId={}",account,password,openId,e);
        }
        return 0;
    }

    public AccountBean getByOpenId(String openId){
        return tbAccountDao.getByOpenId(openId);
    }
}

