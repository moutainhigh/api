package com.zhsj.api.service;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.TBAccountDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.login.LoginUserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int updateOpenId(String account,String password,String openId,String appId){
        logger.info("#AccountService.updateOpenId# account={},password={},openId={},appId={}",account,password,openId,appId);
        try{
            AccountBean accountBean = tbAccountDao.getByAccount(account);
            if(accountBean == null){
            	return 0;
            }
            String name = accountBean.getName();
            String headImg = accountBean.getHeadImg();
            if(!StringUtils.isEmpty(openId) && (StringUtils.isEmpty(name) || StringUtils.isEmpty(headImg))){
                WeixinUserBean weixinUserBean = wxService.getWeixinUser(openId,appId);
                if(weixinUserBean != null){
                    name = StringUtils.isEmpty(name)?weixinUserBean.getNickname():name;
                    headImg = StringUtils.isEmpty(headImg)?weixinUserBean.getHeadimgurl():headImg;
                }
            }
            String pw = Md5.encrypt(password);
            return tbAccountDao.updateOpenId(account, pw, openId,name,headImg);
        }catch (Exception e){
            logger.error("#AccountService.updateOpenId# account={},password={},openId={},appId={}",account,password,openId,appId,e);
        }
        return 0;
    }

    public AccountBean getByOpenId(String openId){
        return tbAccountDao.getByOpenId(openId);
    }
    
    public Object logout(){
    	logger.info("#AccountService.logout");
    	try{
    	LoginUser loginUser = LoginUserUtil.getLoginUser();
    	int code  = tbAccountDao.updateOpenId(loginUser.getAccount(), 
    			loginUser.getPassword(), "", "", "");
    	if(code == 0){
    		logger.info("#AccoutService.logout --------fail");
    		return CommonResult.build(2, "logout error");
    	}
    	return CommonResult.success("Success");
    	}catch(Exception e){
    		logger.error("#AccountService.logout",e);
    		return CommonResult.defaultError("error");
    	}
    }
}

