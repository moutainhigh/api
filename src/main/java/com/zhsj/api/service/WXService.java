package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.*;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.*;
import com.zhsj.api.task.WeChatToken;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.SSLUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class WXService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXService.class);

    @Autowired
    private TbUserDao bmUserDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    private TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TbStoreDao tbStoreDao;

    public String getOpenId(String code,String appId){
        String openId = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                         .append(appId)
                         .append("&secret=")
                         .append(MtConfig.getProperty(appId,""))
                         .append("&code=")
                         .append(code)
                         .append("&grant_type=authorization_code");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            Map<String,String> map = JSON.parseObject(result, Map.class);
            openId = map.get("openid");
        }catch (Exception e){
            logger.error("#WXService.getUserByCode# code={},appId={},e={}",code,appId,e);
        }
        return openId;
    }

    public String getToken(String appId,String secret){
    	logger.info("#WXService.getToken# appid={}.secrt={}#",appId,secret);
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+
                         appId+"&secret="+secret;
            String tokenJson = SSLUtil.getSSL(url);
            Map<String,String> map = JSON.parseObject(tokenJson, Map.class);
            logger.info("#WXService.getToken# result={}",map);
            if(map.get("access_token") != null){
                return map.get("access_token");
            }
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }
        return "";
    }


    public void getUserInfo(String openId,String appId){
    	logger.info("#WXService.getUserInfo# openId={},appId={}",openId,appId);
        try{
            String token = WeChatToken.TOKEN_MAP.get(appId);
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
            logger.error("#WXService.getUserInfo# openId={},appId={}",openId,appId,e);
        }


    }

    public WeixinUserBean getWeixinUser(String openId,String appId){
        logger.info("#WXService.getUserInfo# openId={},appId={}",openId,appId);
        WeixinUserBean weixinUserBean = null;
        try{
            String token = WeChatToken.TOKEN_MAP.get(appId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.weixin.qq.com/cgi-bin/user/info?access_token=")
                    .append(token)
                    .append("&openid=")
                    .append(openId)
                    .append("&lang=zh_CN");
            String result = SSLUtil.getSSL(stringBuilder.toString());
            weixinUserBean = JSON.parseObject(result, WeixinUserBean.class);
            logger.info("#WXService.getUserInfo# result openId={},userBean={}",openId,weixinUserBean);
        }catch (Exception e){
            logger.error("#WXService.getUserInfo# openId={},appId={}",openId,appId,e);
        }
        return weixinUserBean;

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
        sendMessageStore(orderBean);
        sendMessageUser(orderBean);
        
    }

    public void sendMessageUser(OrderBean orderBean){
        logger.info("#WXService.sendMessageUser# storeNo={},orderId={}",orderBean.getStoreNo(),orderBean.getOrderId());
        try {
            if(!"1".equals(orderBean.getPayMethod())){
                logger.info("#WXService.sendMessageUser# storeNo={},orderId={} msg={}",orderBean.getStoreNo(),orderBean.getOrderId(),"不是微信支付");
                return;
            }
            StoreBean storeBean = tbStoreDao.getStoreByNo(orderBean.getStoreNo());
            UserBean userBean = bmUserDao.getUserById(orderBean.getUserId());
            if(StringUtils.isEmpty(userBean.getOpenId())){
                logger.info("#WXService.sendMessageUser# storeNo={},orderId={} msg={}",orderBean.getStoreNo(),orderBean.getOrderId(),"微信openId出错");
                return;
            }
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
            String token = WeChatToken.TOKEN_MAP.get(appId);
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;

            String stroeMessage = MtConfig.getProperty("USER_MESSAGE", "");
            stroeMessage = stroeMessage.replace("_openId",userBean.getOpenId());
            stroeMessage = stroeMessage.replace("_url","");
            stroeMessage = stroeMessage.replace("_first", " \"value\": \"您已支付成功订单\"");
            stroeMessage = stroeMessage.replace("_keyword1","\"value\": \" "+ storeBean.getName() + "商家买单\"");
            stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+orderBean.getOrderId()+"\"");
            stroeMessage = stroeMessage.replace("_keyword3","\"value\": \""+orderBean.getPlanChargeAmount()+"\"");
            stroeMessage = stroeMessage.replace("_keyword4","\"value\": \""+orderBean.getActualChargeAmount()+"\"");
            stroeMessage = stroeMessage.replace("_keyword5", " \"value\": \""+DateUtil.getTime(orderBean.getCtime()*1000)+"\"");
            stroeMessage = stroeMessage.replace("_remark", " \"value\":\"有任何疑问咨询商家\"");
            String result = SSLUtil.postSSL(url, stroeMessage);
            logger.info("#WXService.sendMessageUser# result orderId={},result={}",orderBean.getOrderId(),result);

        }catch (Exception e){
            logger.error("#WXService.sendMessageUser# storeNo={},orderId={}",orderBean.getStoreNo(),orderBean.getOrderId(),e);
        }
    }

    public void sendMessageStore(OrderBean orderBean){
        logger.info("#WXService.sendMessageStore# storeNo={},orderId={}",orderBean.getStoreNo(),orderBean.getOrderId());
        try{
            StoreBean storeBean = tbStoreDao.getStoreByNo(orderBean.getStoreNo());
            if(storeBean == null || StringUtils.isEmpty(storeBean.getAppId())){
                logger.info("#WXService.sendMessageStore# fail appId is null storeNo={}",storeBean.getStoreNo());
                return;
            }
            List<Long> accountIdList = tbStoreBindAccountDao.getAccountIdByStoreNo(orderBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
            List<Long> idList = new ArrayList<>();
            List<Long> saleIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId),accountIdList);
            if(!CollectionUtils.isEmpty(saleIdList)){
            	idList.addAll(saleIdList);
            }
            roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            List<Long> managerIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountIdList);
            if(!CollectionUtils.isEmpty(managerIdList)){
            	idList.addAll(managerIdList);
            }
            if(CollectionUtils.isEmpty(idList)){
            	return;
            }
            List<String> openIdList = tbStoreAccountDao.getOpenIdByAccountId(idList);
            if(CollectionUtils.isEmpty(openIdList)){
                return;
            }
            String appId = storeBean.getAppId();
            String token = WeChatToken.TOKEN_MAP.get(appId);
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
            String stroeMessage = MtConfig.getProperty("STORE_MESSAGE", "");

            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                stroeMessage = stroeMessage.replace("_openId",openId);
                stroeMessage = stroeMessage.replace("_url","wwt.bj37du.com/api/shop/transactionOrder?auth=&id="+orderBean.getId());
                stroeMessage = stroeMessage.replace("_first", " \"value\": \"您好,您有一笔订单收款成功\"");
                stroeMessage = stroeMessage.replace("_keyword1","\"value\": \""+ orderBean.getActualChargeAmount()+"\\n优惠金额："+(orderBean.getPlanChargeAmount()-orderBean.getActualChargeAmount()) + "\"");
                stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+("1".equals(orderBean.getPayMethod())?"微信":"支付宝")+"\"");
                stroeMessage = stroeMessage.replace("_keyword3","\"value\": \" "+ DateUtil.getTime(orderBean.getCtime()*1000) + "\"");
                stroeMessage = stroeMessage.replace("_keyword4","\"value\": \" "+ orderBean.getOrderId() + "\"");
                stroeMessage = stroeMessage.replace("_remark", " \"value\": \"有任何疑问咨询公众号\"");
                logger.info("#WXService.sendMessageStore# url={},msg={}",url,stroeMessage);
                String result = SSLUtil.postSSL(url, stroeMessage);
                logger.info("#WXService.sendMessageStore# result orderId={},result={}",orderBean.getOrderId(),result);
            }
        }catch(Exception e){
            logger.error("#WXService.sendMessageStore# orderBean.orderId", orderBean.getOrderId());
        }
    }
    
    public void qrcode(String no){
    	String appId = "wx8651744246a92699";
        String secret = "7d33f606a68a8473a4919e8ff772447e";
    	String token = new WXService().getToken(appId, secret);
    	
    	String url = " https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
    	String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": "+no+"}}}";
    	
    	try {
			System.out.println(SSLUtil.postSSL(url, json));
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void createMenu(String json){
        try {
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
//            String token = WeChatToken.TOKEN_MAP.get(appId);
            String token = "M91DEhZrCW9mISaAJOebH94BSEFMSO1sqD6h2n6sCk3s8VuBnQCq6tWeN577UYZO2oDf6-lrx9tSfZCvl31W4KRnfjF8Ojag48rNIaIyB2OsmPai5ev9-rH3RhGr32wKRSZgAFADEL";//;
            String uri = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
            String result = SSLUtil.postSSL(uri, json);
            logger.info("#WXService.createMenu# result json={},result={}", json, result);
        }catch (Exception e){
            logger.error("#WXService.createMenu# json={}", json, e);
        }
    }

    public String getMenu(){
        String result = "";
        try {
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
//            String token = WeChatToken.TOKEN_MAP.get(appId);
            String token = "M91DEhZrCW9mISaAJOebH94BSEFMSO1sqD6h2n6sCk3s8VuBnQCq6tWeN577UYZO2oDf6-lrx9tSfZCvl31W4KRnfjF8Ojag48rNIaIyB2OsmPai5ev9-rH3RhGr32wKRSZgAFADEL";//;
            String uri = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+token;
            result = SSLUtil.getSSL(uri);
            logger.info("#WXService.createMenu# result result={}",result);
        }catch (Exception e){
            logger.error("#WXService.createMenu# e={}",e.getMessage(),e);
        }
        return result;
    }

    public String delMenu(){
        String result = "";
        try {
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
//            String token = WeChatToken.TOKEN_MAP.get(appId);
            String token = "M91DEhZrCW9mISaAJOebH94BSEFMSO1sqD6h2n6sCk3s8VuBnQCq6tWeN577UYZO2oDf6-lrx9tSfZCvl31W4KRnfjF8Ojag48rNIaIyB2OsmPai5ev9-rH3RhGr32wKRSZgAFADEL";//;
            String uri = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+token;
            result = SSLUtil.getSSL(uri);
            logger.info("#WXService.createMenu# result result={}",result);
        }catch (Exception e){
            logger.error("#WXService.createMenu# e={}",e.getMessage(),e);
        }
        return result;
    }

    public void testMessage(String openId,OrderBean orderBean,String token) throws IOException, NoSuchAlgorithmException, KeyStoreException, URISyntaxException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;

//        String stroeMessage = MtConfig.getProperty("STORE_MESSAGE", "");
//            stroeMessage = stroeMessage.replace("_openId",openId);
//            stroeMessage = stroeMessage.replace("_url","");
//            stroeMessage = stroeMessage.replace("_first", " \"value\": \"订单支付成功\"");
//            stroeMessage = stroeMessage.replace("_keyword1","\"value\": \" "+ orderBean.getOrderId() + "\"");
//            stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\"");
//            stroeMessage = stroeMessage.replace("_remark", " \"value\": \""+"应付"+orderBean.getPlanChargeAmount()+"实付"+orderBean.getActualChargeAmount()+"\"");

        String stroeMessage = MtConfig.getProperty("USER_MESSAGE", "");
            stroeMessage = stroeMessage.replace("_openId",openId);
            stroeMessage = stroeMessage.replace("_url","");
            stroeMessage = stroeMessage.replace("_first", " \"value\": \"您已支付成功订单\"");
            stroeMessage = stroeMessage.replace("_keyword1","\"value\": \" "+ orderBean.getOrderId() + "\"");
            stroeMessage = stroeMessage.replace("_keyword2","\"value\": \"支付成功\"");
            stroeMessage = stroeMessage.replace("_keyword3","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\"");
        stroeMessage = stroeMessage.replace("_keyword4","\"value\": \"街觅\"");
        stroeMessage = stroeMessage.replace("_keyword5", " \"value\": \""+"应付"+orderBean.getPlanChargeAmount()+"实付"+orderBean.getActualChargeAmount()+"\"");
                stroeMessage = stroeMessage.replace("_remark", " \"value\": \"kkkkk\"");


        String result = SSLUtil.postSSL(url, stroeMessage);
            logger.info(stroeMessage);
            logger.info("#WXService.sendSuccess# result orderId={},result={}",orderBean.getOrderId(),result);
    }

    public static void main(String[] args) throws Exception {
    	String openId="o5pmes_cN1AMrFptmwpDaNj6DXkI";
       String token = "_hYxvWw5Oa-ECafyOXmJvTfFMLp4vrBZXS79KQ0KgRohVUsoVmGTElcjeL0nxKLVUw_PFu_iKJW37EjEpU0Xu2OwV2c5rK342nAE2bntoW6CWj1loRAQR_ALOQhifQNIDXLfCJAHBP";
       String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
       String stroeMessage = MtConfig.getProperty("STORE_MESSAGE", "");
       OrderBean orderBean = new OrderBean();
     orderBean.setOrderId("33333");
     orderBean.setPlanChargeAmount(10.2);
     orderBean.setActualChargeAmount(9.2);
     orderBean.setCtime(4000);
       stroeMessage = stroeMessage.replace("_openId",openId);
       stroeMessage = stroeMessage.replace("_url","wwt.bj37du.com/api/shop/transactionOrder?auth=&id="+orderBean.getId());
       stroeMessage = stroeMessage.replace("_first", " \"value\": \"您好,您有一笔订单收款成功\"");
       stroeMessage = stroeMessage.replace("_keyword1","\"value\": \""+ orderBean.getActualChargeAmount()+"\\n优惠金额："+(orderBean.getPlanChargeAmount()-orderBean.getActualChargeAmount()) + "\"");
       stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+(orderBean.getPayType()==1?"微信":"支付宝")+"\"");
       stroeMessage = stroeMessage.replace("_keyword3","\"value\": \" "+ DateUtil.getTime(orderBean.getCtime()) + "\"");
       stroeMessage = stroeMessage.replace("_keyword4","\"value\": \" "+ orderBean.getOrderId() + "\"");
       stroeMessage = stroeMessage.replace("_remark", " \"value\": \"有任何疑问咨询公众号\"");
       logger.info("#WXService.sendMessageStore# url={},msg={}",url,stroeMessage);
       String result = SSLUtil.postSSL(url, stroeMessage);
       logger.info(result);
//       StoreBean storeBean = new StoreBean();
//       storeBean.setName("小林");
//        OrderBean orderBean = new OrderBean();
//        orderBean.setOrderId("33333");
//        orderBean.setPlanChargeAmount(10.2);
//        orderBean.setActualChargeAmount(9.2);
//        orderBean.setCtime(4000);
//        String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
//        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
//
//        String stroeMessage = MtConfig.getProperty("USER_MESSAGE", "");
//        stroeMessage = stroeMessage.replace("_openId",openId);
//        stroeMessage = stroeMessage.replace("_url","");
//        stroeMessage = stroeMessage.replace("_first", " \"value\": \"您已支付成功订单\"");
//        stroeMessage = stroeMessage.replace("_keyword1","\"value\": \" "+ storeBean.getName() + "商家买单\"");
//        stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+orderBean.getOrderId()+"\"");
//        stroeMessage = stroeMessage.replace("_keyword3","\"value\": \""+orderBean.getPlanChargeAmount()+"\"");
//        stroeMessage = stroeMessage.replace("_keyword4","\"value\": \""+orderBean.getActualChargeAmount()+"\"");
//        stroeMessage = stroeMessage.replace("_keyword5", " \"value\": \""+DateUtil.getTime(orderBean.getCtime())+"\"");
//        stroeMessage = stroeMessage.replace("_remark", " \"value\":\"有任何疑问咨询商家\"");
//        String result = SSLUtil.postSSL(url, stroeMessage);
//        new WXService().testMessage("oFvcxwfZrQxlisYN4yIPbxmOT8KM",orderBean,token);
    }

}
