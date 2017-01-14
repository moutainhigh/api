package com.zhsj.api.util.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * User: kangking2013
 * Date: 14-5-21
 * Time: 下午6:01
 * <p/>
 * util里不要放业务相关的东西
 */
public class CookieUtils {

    private static final int ONE_DAY = 24 * 60 * 60 + 2 * 60 * 60;

    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        try {
            Cookie[] cookies = request.getCookies();
            String value = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        value = URLDecoder.decode(cookie.getValue(), "utf-8");
                        return value;
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("Can not get cookie value for "+cookieName+" , maybe not set.", e);
        }
        return null;
    }

    public static void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue, int expiry, String path) {
        try {

            if (cookieValue != null){
                cookieValue = java.net.URLEncoder.encode(cookieValue,"utf-8");
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(expiry);
            cookie.setPath(path);
            response.addCookie(cookie);

        } catch (Exception e) {
        	logger.error("Can not set cookie value "+cookieName+"="+cookieValue, e);
        }

    }

    public static void deleteCookie(HttpServletResponse response, String cookieName, String path) {
        if (cookieName == null) {
            return;
        }

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void setCookie(String cookieName, String cookieValue,HttpServletResponse response) {

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(ONE_DAY);
        cookie.setPath("/");
        cookie.setValue(cookieValue);
        response.addCookie(cookie);

    }

    private static Cookie getCookie(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
