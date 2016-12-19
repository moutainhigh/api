package com.zhsj.util;

import com.zhsj.service.AbcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lcg on 16/12/17.
 */
public class WXUtil {
    @Autowired
    private AbcService service;

    private String accessToken;

    public static String getWXAccessToken(){
        return "";
    }
}
