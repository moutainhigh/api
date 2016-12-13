package com.zhsj.interceptors;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: yaoliuqing
 * Time: 15/3/31 下午7:45
 */
@Component
public class LoginInterceptor extends AbstractInterceptor {

    public static final int USER_INVALID_TOKEN_CODE = 10004;                // token无效


    public static final String SYS_AUTH_FAILED_MSG          =   "认证失败";




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }




}
