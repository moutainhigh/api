package com.zhsj.service;

import com.zhsj.util.SSLUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx8651744246a92699&secret=7d33f606a68a8473a4919e8ff772447e&code="+code+"&grant_type=authorization_code";
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
        AbcService abcService = new AbcService();
//        System.out.println(abcService.abc());
        String token = "FnGbTVWamObKS8FdcRofrtLtK9Hv51PPbEGywRKx81iDlsJxQt0HlPUtzXe2fse9n2RiYhTv8ky-6vneefTYID_f9cMtVpx0GA4EhjzN72Nu35adMU-Oxx5sdONSRCqrMYZfAGAPBC";
        System.out.println(abcService.getUserInfo(token, "oFvcxwdPHqk7HaHqSxrdSMg5lAKk"));
    }
}
