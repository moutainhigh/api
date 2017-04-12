package com.zhsj.api.interceptors;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.util.WebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 处理API接口没有处理的异常情况，统一返回结果
 * 
 * @author Warren
 * @since 2016-01-13
 */
@ControllerAdvice
public class ApiExceptionHandler {  
  
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    
    //private List<Class<? extends Exception>> excepitons = Arrays.asList(HttpRequestMethodNotSupportedException.class);

    @ExceptionHandler(Exception.class)
    public void handleUnexpectedServerError(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	String url = request.getRequestURI();
    	String ua = request.getHeader("user-agent");
    	String head = JSON.toJSONString(WebUtils.getHeaders(request));
    	String param = WebUtils.getParams(request);
    	logger.error("Api request exception ua={} url={} head={} param={}", ua, url, head, param, ex);
//
//    	// 记录不支持请求类型日志, 没有进入拦截器
//		if (ex instanceof HttpRequestMethodNotSupportedException) {
//			AccessProcesser.preInitArgs(request);
//		}
    	sendFailResponse(response);
//        logger.error("e={}", ex.getMessage(),ex);
    }
    
    private void sendFailResponse(HttpServletResponse response) {
    	String html = "<html><body>";
    	html+="<h2>出错了,请重新操作</h2>";
    	html+="</body></html>";
    			
    	
    	WebUtils.writeHtml(response, html);
    }
    
}  


