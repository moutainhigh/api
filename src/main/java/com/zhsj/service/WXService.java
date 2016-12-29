package com.zhsj.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.util.SSLUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class WXService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXService.class);

    public String getOpenId(String code,String appId,String secret){
        String openId = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                         .append(appId)
                         .append("&secret=")
                         .append(secret)
                         .append("&code=")
                         .append(code)
                         .append("&grant_type=authorization_code");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            Map<String,String> map = JSON.parseObject(result, Map.class);
            openId = map.get("openid");
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }
        return openId;
    }

}
