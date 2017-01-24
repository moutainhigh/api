package com.zhsj.api.util.login;

import com.zhsj.api.bean.AccountBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcg on 17/1/24.
 */
public class LoginUserUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserUtil.class);
    private static final ThreadLocal<AccountBean> BM_LOGIN_USER_THREAD_LOCAL = new ThreadLocal();

    public LoginUserUtil() {
    }

    public static AccountBean getLoginUser() {
        AccountBean loginUser = (AccountBean)BM_LOGIN_USER_THREAD_LOCAL.get();
        return loginUser;
    }

    public static void removeAccountBean() {
        BM_LOGIN_USER_THREAD_LOCAL.remove();
    }

    public static void setBmLoginUser(AccountBean accountBean) {
        BM_LOGIN_USER_THREAD_LOCAL.set(accountBean);
    }

}
