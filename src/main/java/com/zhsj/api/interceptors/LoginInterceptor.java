package com.zhsj.api.interceptors;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.service.AccountService;
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



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String auth = request.getParameter("auth");
        if(StringUtils.isEmpty(auth)){
            return false;
        }
        AccountBean accountBean = null;
        String type = auth.substring(0, 2);
        if("11".equals(type)){
            //为openID
//            if()
            String openId = auth.substring(2);
            accountBean = accountService.getByOpenId(openId);
        }
        if (accountBean == null){
            return false;
        }
        LoginUserUtil.setBmLoginUser(accountBean);
        return true;
    }




}
