package com.zhsj.api.interceptors;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.service.AccountService;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yaoliuqing
 * Time: 15/3/31 下午7:45
 */
@Component
public class LoginInterceptor extends AbstractInterceptor {


    public static final int USER_INVALID_TOKEN_CODE = 10004;                // token无效
    public static final String SYS_AUTH_FAILED_MSG  =   "认证失败";

    @Autowired
    private AccountService accountService;
    @Autowired
    ShopService shopService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String auth = request.getParameter("auth");
        if(StringUtils.isEmpty(auth)){
            return false;
        }
        String type = auth.substring(0, 2);
        if("11".equals(type)){
            //为openID、管理员
            String openId = auth.substring(2);
            AccountBean accountBean = accountService.getByOpenId(openId);
            if (accountBean == null){
                return false;
            }
            LoginUser loginUser = new LoginUser();
            loginUser.setPassword(accountBean.getPassword());
            loginUser.setId(accountBean.getId());
            loginUser.setAccount(accountBean.getAccount());
            loginUser.setName(accountBean.getName());
            loginUser.setOpenId(accountBean.getOpenId());
            loginUser.setType(1);
            LoginUserUtil.setBmLoginUser(loginUser);
        }
        if("21".equals(type)){
            //为openID,商家
            String openId = auth.substring(2);
            StoreAccountBean storeAccountBean = shopService.getStoreAccountByOpenId(openId);
            if (storeAccountBean == null){
                return false;
            }
            LoginUser loginUser = new LoginUser();
            loginUser.setPassword(storeAccountBean.getPassword());
            loginUser.setId(storeAccountBean.getId());
            loginUser.setAccount(storeAccountBean.getAccount());
            loginUser.setName(storeAccountBean.getName());
            loginUser.setOpenId(storeAccountBean.getOpenId());
            loginUser.setType(2);
            LoginUserUtil.setBmLoginUser(loginUser);
        }
        return true;
    }




}
