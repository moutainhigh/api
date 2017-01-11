package com.zhsj.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.dao.BmUserDao;
import com.zhsj.util.DateUtil;
import com.zhsj.util.MtConfig;
import com.zhsj.util.SSLUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class WXService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXService.class);

    //key=appId value[key-value],key secret, key token key expires
    public final static Map<String,Map<String,String>> TOKEN_MAP = Collections.synchronizedMap(new HashMap<String, Map<String, String>>());

    @Autowired
    private BmUserDao bmUserDao;

    public String getOpenId(String code){
        String openId = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                         .append(MtConfig.getProperty("weChat_appId","wx8651744246a92699"))
                         .append("&secret=")
                         .append(MtConfig.getProperty("weChat_secret","7d33f606a68a8473a4919e8ff772447e"))
                         .append("&code=")
                         .append(code)
                         .append("&grant_type=authorization_code");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            Map<String,String> map = JSON.parseObject(result, Map.class);
            openId = map.get("openid");
        }catch (Exception e){
            logger.error("#WXService.getUserByCode# code={},storeNo={},openId={},e={}",code,openId,e);
        }
        return openId;
    }

    public void getToken(String appId,String secret){
        String tokenJson = "";
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+
                         appId+"&secret="+secret;
            tokenJson = SSLUtil.getSSL(url);
            Map<String,Object> map = JSON.parseObject(tokenJson,Map.class);
            if(map.get("access_token") != null){
                Map<String,String> values = new HashMap();
                values.put("token",(String)map.get("access_token"));
                long time = DateUtil.unixTime();
                int expiresTime = (Integer)map.get("expires_in");
                time = time + expiresTime - 60*30;
                values.put("expires",time+"");
                values.put("secret",secret);
                WXService.TOKEN_MAP.put(appId,values);
            }
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String appId = "wx8651744246a92699";
        String secret = "7d33f606a68a8473a4919e8ff772447e";
        new WXService().getToken(appId, secret);
        String token = WXService.TOKEN_MAP.get(appId).get("token");
        System.out.println(token);
        String json = "{\n" +
                "    \"touser\": \"oFvcxwfZrQxlisYN4yIPbxmOT8KM\",\n" +
                "    \"template_id\": \"EzalgCDul_41sIFZ3jjw7B4UXuLAAZ5_0MJAlQzLG3o\",\n" +
                "    \"url\": \"http://weixin.qq.com/download\",\n" +
                "    \"topcolor\": \"#FF0000\",\n" +
                "    \"data\": {\n" +
                "        \"first\": {\n" +
                "            \"value\": \"您已支付成功订单\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword1\": {\n" +
                "            \"value\": \"街觅 10010\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword2\": {\n" +
                "            \"value\": \"支付成功\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword3\": {\n" +
                "            \"value\": \"消费\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword4\": {\n" +
                "            \"value\": \"人民币260.00元\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword5\": {\n" +
                "            \"value\": \"06月07日19时24分\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"remark\": {\n" +
                "            \"value\": \"6504.09\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        try {
            System.out.println(SSLUtil.postSSL(url, json));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }

}
