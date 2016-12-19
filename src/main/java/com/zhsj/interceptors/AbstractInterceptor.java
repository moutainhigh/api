package com.zhsj.interceptors;

import com.zhsj.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽象拦截器，提供子类调用方法
 * 
 * @author Warren
 * @since Aug 19, 2016
 */
public abstract class AbstractInterceptor extends HandlerInterceptorAdapter {
	
	protected final Logger logger = LoggerFactory.getLogger(AbstractInterceptor.class);



	/**
	 * 返回失败JSON数据
	 * @param response
	 * @param errorCode
	 */
//	protected void sendFailResponse(HttpServletResponse response, ErrorCode errorCode) {
//		DataResponse result = new DataResponse(errorCode);
//		recordKick2Ros(result);
//
//		String json = result.toJson().toJSONString();
//		// 记录access日志
//		AccessProcesser.postResultArgs(json, ClassUtils.getShortName(getClass())+".preHandle()", 0, result.getCode());
//
//		WebUtils.writeJson(response, json);
//	}

	/**
	 * 跳转到error页面
	 * @param response
	 * @param errorCode
	 */
//	protected void sendFailResponsePage(HttpServletResponse response, ErrorCode errorCode) {
//		DataResponse result = new DataResponse(errorCode);
//		recordKick2Ros(result);
//
//		String html = buildErrorPage(result.getCode(), result.getMsg());
//		// 记录access日志
//    	AccessProcesser.postResultArgs(html, ClassUtils.getShortName(getClass())+"preHandle()", 0, result.getCode());
//
//    	WebUtils.writeHtml(response, html);
//	}

	/**
	 * 构造错误提示页面
	 * @param code
	 * @param msg
	 * @return
	 */
	private String buildErrorPage(int code, String msg) {
		StringBuilder sBuilder = new StringBuilder();
		return sBuilder.append("<!DOCTYPE html>").append("<html lang=\"zh-CN\">").append("<head>")
				.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
				.append("<title>错误</title>")
				.append("</head><body><div style=\"padding:2em 1em;font-size: 14pt;line-height: 20pt;\">")
				.append(StringUtils.isEmpty(msg) ? "页面加载失败" : msg).append("(错误码:").append(code).append(")")
				.append("</div></body></html>").toString();
	}

//	private void recordKick2Ros(ErrorCode result) {
//		CommonParams params = ApiContext.getCommonParams();
//		int code = result.getCode();
//		if(code != 0) {  //用户被踢,记录ROS埋点
//			reportService.reportRiderBuried(params.getMtUserId(), params.getMtUserToken(), params.getUuid(), params.getAppType(), params.getAppVersion(),
//					params.getOsType(), params.getRequestId(), BmRiderBuriedOperationEnum.KICK_OFF.getValue(), String.valueOf(code), params.getClientIp());
//		}
//	}

	/**
	 * 获取公共必填参数，记录未传参数
	 * @param request 请求对象
	 * @param name 参数名
	 * @param defVal 默认值
	 * @return 参数值
	 */
	protected String getParameter(HttpServletRequest request, String name, String defVal) {
		String val = null;
		if(name.equals("requestId")) {
			val = WebUtils.getRequestId(request);
		}else {
			val = request.getParameter(name);
		}
		// 轻量检查
		if(StringUtils.isBlank(val)) {
			val = defVal;
		}
		return val;
	}
}
