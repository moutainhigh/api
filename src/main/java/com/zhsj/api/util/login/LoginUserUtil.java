package com.zhsj.api.util.login;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcg on 17/1/24.
 */
public class LoginUserUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserUtil.class);
    private static final ThreadLocal<LoginUser> BM_LOGIN_USER_THREAD_LOCAL = new ThreadLocal();

    public LoginUserUtil() {
    }

    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser)BM_LOGIN_USER_THREAD_LOCAL.get();
        return loginUser;
    }

    public static void removeAccountBean() {
        BM_LOGIN_USER_THREAD_LOCAL.remove();
    }

    public static void setBmLoginUser(LoginUser loginUser) {
        BM_LOGIN_USER_THREAD_LOCAL.set(loginUser);
    }

    public static StoreBean getStore(){
        LoginUser loginUser = (LoginUser)BM_LOGIN_USER_THREAD_LOCAL.get();
        if(loginUser != null && loginUser.getType() == 2){
            TBStoreBindAccountDao tbStoreBindAccountDao = (TBStoreBindAccountDao) SpringBeanUtil.getBean("TBStoreBindAccountDao");
            String storeNo = tbStoreBindAccountDao.getStoreNoByAccountId(loginUser.getId());
            if(StringUtils.isEmpty(storeNo)){
                return null;
            }
            TbStoreDao tbStoreDao = (TbStoreDao) SpringBeanUtil.getBean("tbStoreDao");
            return  tbStoreDao.getStoreByNo(storeNo);
        }
        return null;
    }

}
