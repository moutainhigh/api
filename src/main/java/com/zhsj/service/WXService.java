package com.zhsj.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.bean.UserBean;
import com.zhsj.dao.BmUserDao;
import com.zhsj.util.MtConfig;
import com.zhsj.util.SSLUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class WXService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXService.class);

    @Autowired
    private BmUserDao bmUserDao;

    public UserBean getUserByCode(String code,String storeNo){
        String openId = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                         .append(MtConfig.getProperty("weChat_appId","wx8651744246a92699"))
                         .append("&secret=")
                         .append(MtConfig.getProperty("weChat_secret","7d33f606a68a8473a4919e8ff772447e"))
                         .append("&code=")
                         .append(code)
                         .append("&grant_type=authorization_code");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            Map<String,String> map = JSON.parseObject(result, Map.class);
            openId = map.get("openid");
            UserBean userBean = bmUserDao.getUserByOpenId(openId);
            if (userBean != null){
                return userBean;
            }
            userBean = new UserBean();
            userBean.setOpenId(openId);
            bmUserDao.insertOpenId(userBean);
            return userBean;
        }catch (Exception e){
            logger.error("#WXService.getUserByCode# code={},storeNo={},e={}",code,storeNo,e.getMessage(),e);
        }
        return null;
    }

}
