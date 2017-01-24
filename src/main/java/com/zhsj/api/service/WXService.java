package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.TBAccountDao;
import com.zhsj.api.dao.TBStoreAccountBindRoleDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.dao.TbUserDao;
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
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
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
    private TBAccountDao tbAccountDao;

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

    public String getToken(String appId,String secret){
    	logger.info("#WXService.getToken# appi={}.secrt={}#",appId,secret);
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


    public void getUserInfo(String openId){
    	logger.info("#WXService.getUserInfo# openId={}",openId);
        try{
            String appId = MtConfig.getProperty("weChat_appId","wx8651744246a92699");
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
        try{
            List<Long> accountIdList = tbStoreBindAccountDao.getAccountIdByStoreNo(orderBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            String roleId = MtConfig.getProperty("RECEIVE_SUCCESS_MESSAGE_ROLE", "");
            accountIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId),accountIdList);
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            List<String> openIdList = tbAccountDao.getOpenIdByAccountId(accountIdList);
            if(CollectionUtils.isEmpty(openIdList)){
                return;
            }
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");
            String token = WeChatToken.TOKEN_MAP.get(appId);
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                String json = "{\n" +
                        "    \"touser\": \""+openId+"\",\n" +
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
                String result = SSLUtil.postSSL(url, json);
                logger.info("#WXService.sendSuccess# result orderId={},result={}",orderBean.getOrderId(),result);
            }
        }catch(Exception e){
        	logger.error("#WXService.sendSuccess# orderBean.orderId",orderBean.getOrderId());
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

    public static void main(String[] args) throws Exception {
//        String appId = "wx8651744246a92699";
//        String secret = "7d33f606a68a8473a4919e8ff772447e";
//      new WXService().getToken(appId, secret);
    	new WXService().qrcode("ddd");
//        String token = WXService.TOKEN_MAP.get(appId).get("token");
//        System.out.println( WXService.TOKEN_MAP.get(appId));
//        new WXService().getUserInfo("oFvcxwZfj20QNzdpagGb1uDbhQUk");
//        Thread.sleep(1000*60*2);
//        new WXService().getUserInfo("oFvcxwZfj20QNzdpagGb1uDbhQUk");
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
