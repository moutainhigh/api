package com.zhsj.api.util.test;

import com.zhsj.api.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lcg on 16/12/17.
 */
public class WXUtil {
    @Autowired
    private BaseService service;

    private String accessToken;

    public static String getWXAccessToken(){
        return "";
    }
}
