package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.SSLUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.lang.String;
import java.util.ArrayList;
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
       	 String url = MtConfig.getProperty("OPEN_URL", "")+ "/getOpenId";
       	 Map<String, String> parameters = new HashMap();
       	 parameters.put("appId", appId);
       	 parameters.put("code", code);
       	 String result = HttpClient.sendGet(url, parameters);
       	 Map<String,Object> map = JSON.parseObject(result, Map.class);
      	 int rcode = (Integer)map.get("code");
      	 if(rcode == 0){
      		 openId = (String)map.get("data");
      	 }
       }catch (Exception e){
           logger.error("#WXService.getUserByCode# code={},appId={},e={}",code,appId,e);
       }
       return openId;
    }
    
    public WeixinUserBean getWeixinUser(String openId,String appId){
        logger.info("#WXService.getWeixinUser# openId={},appId={}",openId,appId);
        WeixinUserBean weixinUserBean = null;
        try{
        	 String url = MtConfig.getProperty("OPEN_URL", "")+ "/getWeChatUserInfo";
           	 Map<String, String> parameters = new HashMap();
           	 parameters.put("appId", appId);
           	 parameters.put("openId", openId);
           	 String result = HttpClient.sendGet(url, parameters);
           	 Map<String,Object> map = JSON.parseObject(result, Map.class);
          	 int rcode = (Integer)map.get("code");
          	 if(rcode == 0){
          		JSONObject json = (JSONObject)map.get("data");
          		weixinUserBean = JSON.parseObject(json.toJSONString(),WeixinUserBean.class);
          		if(weixinUserBean.getErrcode() == 0){
          			return weixinUserBean;
          		}
          	 }
        }catch (Exception e){
            logger.error("#WXService.getWeixinUser# openId={},appId={}",openId,appId,e);
        }
        return null;

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
            if(StringUtils.isEmpty(userBean.getOpenId()) || userBean.getSubscribe() == 0){
                logger.info("#WXService.sendMessageUser# storeNo={},orderId={} msg={}",orderBean.getStoreNo(),orderBean.getOrderId(),"微信openId出错或是用户没有关注公众号");
                return;
            }
            String appId = MtConfig.getProperty("weChat_appId", "wx8651744246a92699");

            String stroeMessage = MtConfig.getProperty("USER_MESSAGE", "");
            stroeMessage = stroeMessage.replace("_first", " \"value\": \"您已支付成功订单\"");
            stroeMessage = stroeMessage.replace("_keyword1","\"value\": \" "+ storeBean.getName() + "商家买单\"");
            stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+orderBean.getOrderId()+"\"");
            stroeMessage = stroeMessage.replace("_keyword3","\"value\": \""+orderBean.getPlanChargeAmount()+"\"");
            stroeMessage = stroeMessage.replace("_keyword4","\"value\": \""+orderBean.getActualChargeAmount()+"\"");
            stroeMessage = stroeMessage.replace("_keyword5", " \"value\": \""+DateUtil.getTime(orderBean.getCtime()*1000)+"\"");
            stroeMessage = stroeMessage.replace("_remark", " \"value\":\"有任何疑问咨询商家\"");
            
            String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
          	 Map<String, String> parameters = new HashMap();
          	 parameters.put("appId", appId);
          	 parameters.put("openIds", userBean.getOpenId());
          	 parameters.put("message", stroeMessage);
          	 parameters.put("url", "");
          	 
          	 String result = HttpClient.sendGet(url, parameters);
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
            
            String stroeMessage = MtConfig.getProperty("STORE_MESSAGE", "");

            String openIds = "";
            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                openIds += openId+",";
            }
            
             String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
         	 Map<String, String> parameters = new HashMap();
         	 parameters.put("appId", appId);
         	 parameters.put("openIds", openIds);
         	 parameters.put("message", stroeMessage);
         	 parameters.put("url", "wwt.bj37du.com/api/shop/transactionOrder?auth=&id="+orderBean.getId());
         	 String result = HttpClient.sendGet(url, parameters);
         	logger.info("#WXService.sendMessageStore# result orderId={},result={}",orderBean.getOrderId(),result);
        }catch(Exception e){
            logger.error("#WXService.sendMessageStore# orderBean.orderId", orderBean.getOrderId());
        }
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
