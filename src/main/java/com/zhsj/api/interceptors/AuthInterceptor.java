package com.zhsj.api.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhsj.api.service.ModuleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends AbstractInterceptor {

	@Autowired
	private ModuleService moduleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authURI = request.getParameter("authURI");
        String uri = request.getRequestURI();
        
        if(!StringUtils.isEmpty(authURI)){
        	uri = authURI;
        }
        boolean result = moduleService.authByURI(uri);
        if(!result){
        	sendFailResponse(response,"{\"code\":1,\"msg\":\"没有权限\"}");
        	return false;
        }
        return true;
    }




}
