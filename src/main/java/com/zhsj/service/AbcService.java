package com.zhsj.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.util.SSLUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class AbcService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbcService.class);

    public String abc(){
        String tokenJson = "";
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx31626c1d3ab34e12&secret=045a93654391302593eb3fcdeecefc54";
            tokenJson = SSLUtil.getSSL(url);
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

        return tokenJson;
    }

    public String getOpenId(String code){
        String tokenJson = "";
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx31626c1d3ab34e12&secret=045a93654391302593eb3fcdeecefc54&code="+code+"&grant_type=authorization_code";
            tokenJson += url;
            tokenJson += SSLUtil.getSSL(url);
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

        return tokenJson;
    }

    public String getUserInfo(String token,String openId){
        String tokenJson = "";
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
            tokenJson += url;
            tokenJson += SSLUtil.getSSL(url);
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

        return tokenJson;
    }



    public static void main(String[] args){
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);ids.add(4);
        ids.add(5);
        ids.add(6);
        ids.add(7);
        ids.add(8);

        int offset = 8;
        int size = 0;
        ids = ids.subList(offset,offset+size);
        System.out.println("===========================");
        System.out.println(ids);
        System.out.println("===========================");

    }
}
