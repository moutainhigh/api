package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.TbUserDao;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.SSLUtil;
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
    public static Map<String,Map<String,String>> TOKEN_MAP = Collections.synchronizedMap(new HashMap<String, Map<String, String>>());

    @Autowired
    private TbUserDao bmUserDao;
    @Autowired
    private OrderService orderService;

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

    public Map<String,String> getToken(String appId,String secret){
    	logger.info("#WXService.getToken# appi={}.secrt={}#",appId,secret);
        Map<String,String> resultMap = null;
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+
                         appId+"&secret="+secret;
            String tokenJson = SSLUtil.getSSL(url);
            Map<String,Object> map = JSON.parseObject(tokenJson, Map.class);
            if(map.get("access_token") != null){
                resultMap = new HashMap();
                resultMap.put("token",(String)map.get("access_token"));
                long time = DateUtil.unixTime();
                int expiresTime = (Integer)map.get("expires_in");
                time = time + expiresTime - 60*20;
                resultMap.put("expires",time+"");
                resultMap.put("secret",secret);
            }
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }
        return resultMap;
    }

    public String getTokenByAppId(String appId,String secrte){
    	logger.info("#WXService.getTokenByAppId# appi={}.secrt={}#",appId,secrte);
        try {
            Map<String,String> map = WXService.TOKEN_MAP.get(appId);
            if(map == null){
                map = getToken(appId, secrte);
                WXService.TOKEN_MAP.put(appId,map);
            }
            long time = Long.parseLong(map.get("expires"));
            long curTime = DateUtil.unixTime();
            if(curTime > time){
                map = getToken(appId, secrte);
            }
            map = WXService.TOKEN_MAP.get(appId);
            String token = map.get("token");
            logger.info("#WXService.getTokenByAppId# result appi={}.secrt={} token={}",appId,secrte,token);
            return token;
          }catch (Exception e){
            logger.error("#WXService.getTokenByAppId# appId={},secrte={}",appId,secrte,e);
        }
        return "";
    }

    public void getUserInfo(String openId){
    	logger.info("#WXService.getUserInfo# openId={}",openId);
        try{
            String appId = MtConfig.getProperty("weChat_appId","wx8651744246a92699");
            String secret = MtConfig.getProperty("weChat_secret", "7d33f606a68a8473a4919e8ff772447e");
            String token = getTokenByAppId(appId, secret);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/cgi-bin/user/info?access_token=")
                    .append(token)
                    .append("&openid=")
                    .append(openId)
                    .append("&lang=zh_CN");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            WeixinUserBean weixinUserBean = JSON.parseObject(result, WeixinUserBean.class);
            bmUserDao.updateUserInfoByOpenId(weixinUserBean);
            logger.info("#WXService.getUserInfo# result openId={},userBean={}",openId,weixinUserBean);
        }catch (Exception e){
            logger.error("#WXService.getUserInfo# openId={}",openId,e);
        }


    }

    public void sendSuccess(String orderNo){
        logger.info("#WXService.sendSuccess# orderNo={}",orderNo);
        OrderBean orderBean = orderService.getByOrderId(orderNo);
        if(orderBean != null){
            this.sendSuccess(orderBean);
        }
    }

    public void sendSuccess(OrderBean orderBean){
        logger.info("#WXService.sendSuccess# storeNo={},orderId={}",orderBean.getStoreNo(),orderBean.getOrderId());
        String appId = MtConfig.getProperty("weChat_appId","wx8651744246a92699");
        String secret = MtConfig.getProperty("weChat_secret", "7d33f606a68a8473a4919e8ff772447e");
        String token = getTokenByAppId(appId, secret);
        String json = "{\n" +
                "    \"touser\": \"oFvcxwfZrQxlisYN4yIPbxmOT8KM\",\n" +
                "    \"template_id\": \"8saP99-JcHJMl8D-RD54OJLPaz9OtNGlSi8tbz8Xrvo\",\n" +
                "    \"url\": \"\",\n" +
                "    \"topcolor\": \"#FF0000\",\n" +
                "    \"data\": {\n" +
                "        \"first\": {\n" +
                "            \"value\": \"订单支付成功\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword1\": {\n" +
                "            \"value\": \" "+ orderBean.getOrderId() + "\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword2\": {\n" +
                "            \"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"remark\": {\n" +
                "            \"value\": \""+"应付"+orderBean.getPlanChargeAmount()+"实付"+orderBean.getActualChargeAmount()+"\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        try {
            SSLUtil.postSSL(url, json);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String appId = "wx8651744246a92699";
        String secret = "7d33f606a68a8473a4919e8ff772447e";
        new WXService().getToken(appId, secret);
//        String token = WXService.TOKEN_MAP.get(appId).get("token");
//        System.out.println( WXService.TOKEN_MAP.get(appId));
        new WXService().getUserInfo("oFvcxwZfj20QNzdpagGb1uDbhQUk");
        Thread.sleep(1000*60*2);
        new WXService().getUserInfo("oFvcxwZfj20QNzdpagGb1uDbhQUk");
//        OrderBean bean = new OrderBean();
//        bean.setOrderId("20170114195124247SN0ba482a1d");
//        bean.setActualChargeAmount(0.1);
//        bean.setPlanChargeAmount(0.2);
//        new WXService().sendSuccess(bean);
//        String json = "{\n" +
//                "    \"touser\": \"oFvcxwfZrQxlisYN4yIPbxmOT8KM\",\n" +
//                "    \"template_id\": \"EzalgCDul_41sIFZ3jjw7B4UXuLAAZ5_0MJAlQzLG3o\",\n" +
//                "    \"url\": \"http://weixin.qq.com/download\",\n" +
//                "    \"topcolor\": \"#FF0000\",\n" +
//                "    \"data\": {\n" +
//                "        \"first\": {\n" +
//                "            \"value\": \"您已支付成功订单\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"keyword1\": {\n" +
//                "            \"value\": \"街觅 10010\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"keyword2\": {\n" +
//                "            \"value\": \"支付成功\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"keyword3\": {\n" +
//                "            \"value\": \"消费\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"keyword4\": {\n" +
//                "            \"value\": \"人民币260.00元\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"keyword5\": {\n" +
//                "            \"value\": \"06月07日19时24分\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        },\n" +
//                "        \"remark\": {\n" +
//                "            \"value\": \"6504.09\",\n" +
//                "            \"color\": \"#173177\"\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
//        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
//        try {
//            System.out.println(SSLUtil.postSSL(url, json));
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ;
    }

}
