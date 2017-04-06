package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.LoginUser;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreBalanceDetailBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.bean.result.QueryTransfers;
import com.zhsj.api.bean.result.Transfers;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.HttpsRequest;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.SSLUtil;
import com.zhsj.api.util.StoreUtils;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.login.LoginUserUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private TBStoreBalanceDetailsDao tbStoreBalanceDetailsDao;
    @Autowired
    private ShopService shopService;

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
            stroeMessage = stroeMessage.replace("_first", " \"value\": \"您好,您有一笔订单收款成功\"");
            stroeMessage = stroeMessage.replace("_keyword1","\"value\": \""+ orderBean.getActualChargeAmount()+"\\n优惠金额："+(orderBean.getPlanChargeAmount()-orderBean.getActualChargeAmount()) + "\"");
            stroeMessage = stroeMessage.replace("_keyword2","\"value\": \""+("1".equals(orderBean.getPayMethod())?"微信":"支付宝")+"\"");
            stroeMessage = stroeMessage.replace("_keyword3","\"value\": \" "+ DateUtil.getTime(orderBean.getCtime()*1000) + "\"");
            stroeMessage = stroeMessage.replace("_keyword4","\"value\": \" "+ orderBean.getOrderId() + "\"");
            stroeMessage = stroeMessage.replace("_remark", " \"value\": \"有任何疑问咨询公众号\"");
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
    
    /*
     * 转账(商户提现)
     * 接口调用规则：
◆ 给同一个实名用户付款，单笔单日限额2W/2W
◆ 给同一个非实名用户付款，单笔单日限额2000/2000
◆ 一个商户同一日付款总额限额100W
◆ 单笔最小金额默认为1元
◆ 每个用户每天最多可付款10次，可以在商户平台--API安全进行设置
◆ 给同一个用户付款时间间隔不得低于15秒
     */
    public Object transfers(double amount,String ip,String realname){
    	logger.info("#WXService.transfers #amount = {},ip = {},relaname={}",amount,ip,realname);
    	StoreBean storeBean = LoginUserUtil.getStore();
    	LoginUser loginUser = LoginUserUtil.getLoginUser();
    	List<Integer> roleId = tbStoreAccountBindRoleDao.getRoleIdByAccountId(loginUser.getId());
    	String rid = MtConfig.getProperty("STORE_MANAGER_ROLE", "0");
    	if(!roleId.contains(Integer.valueOf(rid))){//如果不包含5
    		return CommonResult.build(5, "没有权限提现");
    	}
    	String storeNo = storeBean.getStoreNo();
    	String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    	Transfers transfers = new Transfers();
    	transfers.setAmount(new Double(amount*100).intValue());//分
    	transfers.setOpenid(loginUser.getOpenId());
    	transfers.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
    	String orderNo = StoreUtils.getOrderNO(storeNo);
    	transfers.setPartner_trade_no(orderNo);
//    	transfers.setSpbill_create_ip(ip);
    	transfers.setSpbill_create_ip("114.215.223.220");
    	try {
			transfers.setRe_user_name(URLEncoder.encode(realname, "UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	StoreBalanceDetailBean storeBalanceDetailBean = new StoreBalanceDetailBean();
    	storeBalanceDetailBean.setStoreNo(storeNo);
    	storeBalanceDetailBean.setPaymentStatus(0);
    	storeBalanceDetailBean.setPrice(new BigDecimal(amount));
    	storeBalanceDetailBean.setPartnerTradeNo(orderNo);
    	storeBalanceDetailBean.setDescription("提现");
    	storeBalanceDetailBean.setType(2);
    	String result = "";
    	double price = tbStoreDao.getPriceByStoreNo(storeNo);
		try {
			int initAddId =tbStoreBalanceDetailsDao.insert(storeBalanceDetailBean);
			if(0 == initAddId){
				logger.info("#WXService.transfers #insert storeBalanceDetailBean fail,#storeBalanceDetailBean={}",storeBalanceDetailBean);
			}
			int initUpdateId = tbStoreDao.updatePriceByStoreNo(amount, storeNo, price, 1, 1);//type == 1提现 isPrice=1验证price 否则不验证
			if(0 == initUpdateId){
				logger.info("#WXService.transfers #initUpdateId store price fail");
			}
			logger.info("#WXService.transfers #amount = {},storeNo={},price={},type={},#url={}#transfers={}",amount,storeNo,price,1,url,transfers);
//			result = WXHttpUtils.post(url, transfers.toString());
			result = new HttpsRequest().sendPost(url, transfers.toString());
			Map<String,String> map = XMLBeanUtils.xmlToMap(result);
			logger.info("#WXServie.transfers #result_code = {} #map = {},result={}",map.get("return_code"),map,result);
			if("SUCCESS".equals(map.get("return_code"))){
				logger.info("#WXService.transfers #return_code={},#return_msg={},#mch_appid={},#mchid={},#device_info={},"
						+ "#nonce_str={},#result_code={},#err_code={},#err_code_des={}",map.get("return_code"),map.get("return_msg"),
						map.get("mch_appid"), map.get("mchid"), map.get("device_info"), map.get("nonce_str"), map.get("result_code"),
						map.get("err_code"), map.get("err_code_des"));
				if("SUCCESS".equals(map.get("result_code"))){
					 logger.info("#WXService.transfers #partner_trade_no={},#payment_no={},#payment_time={}",map.get("partner_trade_no"),
							 map.get("payment_no"),map.get("payment_time"));
					 CommonResult cr = (CommonResult) queryTransfersInfo(orderNo);
					 logger.info("queryResult----#code={},#msg={}",cr.getCode(),cr.getMsg());
					 if(cr.getCode() == 0){
						 if("SUCCESS".equals(cr.getMsg())){
							 storeBalanceDetailBean.setPaymentTime(new Long(
									 DateUtil.formatStringUnixTime(map.get("payment_time"), "yyyy-MM-dd HH:mm:ss")).intValue());
							 storeBalanceDetailBean.setPaymentStatus(1);
							 storeBalanceDetailBean.setPaymentNo(map.get("payment_no"));
							 int rightId = tbStoreBalanceDetailsDao.update(storeBalanceDetailBean);
							 logger.info("提现成功");
							 if(rightId == 0){
								 logger.info("#WXService.transfers 提现成功！更新数据失败");
							 }
							 this.sendGetCashMsg(map.get("payment_no"));
							 return  CommonResult.build(0, "提现成功");
						 }else if("PROCESSING".equals(cr.getMsg())){
							 logger.info("提现处理中");
							 //轮询0
							 return CommonResult.build(0, "提现处理中");
						 }
					 }else if(cr.getCode() == 1 || cr.getCode() == 2){
						  shopService.updateAdd(storeBalanceDetailBean, amount, storeNo, price);
						  tbStoreDao.updatePriceByStoreNo(amount, storeNo, price, 2, 2);
						  return CommonResult.build(1, "提现失败");
					 }
				}else{
					 shopService.updateAdd(storeBalanceDetailBean, amount, storeNo, price);
					 return CommonResult.build(1, "提现失败");
				}
			}else{
				shopService.updateAdd(storeBalanceDetailBean, amount, storeNo, price);
				return CommonResult.build(1, "提现失败");
			}
		} catch (Exception e) {
			logger.error("#WXService.transfers #amount={},ip = {}",amount,ip,e);
			shopService.updateAdd(storeBalanceDetailBean, amount, storeNo, price);
			return CommonResult.build(1, "提现失败");
		}
		return null;
    }
    /*
     * 查询企业付款
     */
    //orderId 为自己生成的订单号
    public Object queryTransfersInfo(String orderId){
    	
    	QueryTransfers queryTransfers = new QueryTransfers();
    	queryTransfers.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
    	queryTransfers.setPartner_trade_no(orderId);
    	String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
    	try {
			String result = new HttpsRequest().sendPost(url, queryTransfers.toString());
			Map<String, String> map = XMLBeanUtils.xmlToMap(result);
			logger.info("#WXService.queryTransfersInfo #map = {}",map);
			
			if("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))){
				logger.info("WXService.queryTransfersInfo #return_code={},#return_msg={},#partner_trade_no={},#mch_id={},#detail_id={},"
						+ "#reason={},#openid={},#transfer_name={},#payment_amount={},#transfer_time={},#desc={}",
						map.get("return_code"),map.get("return_msg"),map.get("partner_trade_no"),map.get("mch_id"),map.get("detail_id"),
						map.get("status"),map.get("reason"),map.get("openid"),map.get("transfer_name"),map.get("payment_amount"),
						map.get("transfer_time"),map.get("desc"));
				String status = map.get("status");
				logger.info("#status={}",status);
				if("SUCCESS".equals(status)){
					return CommonResult.build(0, "SUCCESS",map);
				}else if("PROCESSING".equals(status)){
					return CommonResult.build(0, "PROCESSING");
				}else{
					return CommonResult.build(2, "FAILED");
				}
			}
		} catch (Exception e) {
			logger.error("#WXService.queryTransfersInfo #orderId={}",orderId,e);
			return CommonResult.defaultError("error");
		}
    	return null;
    }

    
    public void sendPayCashMsg(StoreBean storeBean,int count,double price){
        logger.info("#WXService.sendPayCashMsg# storeNo={}",storeBean.getStoreNo());
        try {
            String payCashMsg = MtConfig.getProperty("PAY_CASH_MESSAGE", "");
            if(StringUtils.isEmpty(payCashMsg)){
            	 logger.info("#WXService.sendPayCashMsg# storeNo={} msg={}",storeBean.getStoreNo(),"没有模板消息");
                 return;
            }
            
            if(StringUtils.isEmpty(storeBean.getAppId())){
            	 logger.info("#WXService.sendPayCashMsg# storeNo={} msg={}",storeBean.getStoreNo(),"没有绑定接收人");
                 return;
            }
            
            List<Long> accountIdList = tbStoreBindAccountDao.getAccountIdByStoreNo(storeBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            String roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            List<Long> managerIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountIdList);
            if(CollectionUtils.isEmpty(managerIdList)){
            	 logger.info("#WXService.sendPayCashMsg# storeNo={} msg={}",storeBean.getStoreNo(),"没有绑定接收人");
                 return;
            }
            List<String> openIdList = tbStoreAccountDao.getOpenIdByAccountId(managerIdList);
            if(CollectionUtils.isEmpty(openIdList)){
                return;
            }
            String openIds = "";
            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                openIds += openId+",";
            }
            payCashMsg = payCashMsg.replace("_first", " \"value\": \"返现原因：平台优惠结算\"");
            payCashMsg = payCashMsg.replace("_keyword1","\"value\": \" "+ price + "\"");
            payCashMsg = payCashMsg.replace("_keyword2","\"value\": \""+count+"\"");
            payCashMsg = payCashMsg.replace("_keyword3","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\"");
            payCashMsg = payCashMsg.replace("_remark", " \"value\":\""+"请到公众号-商户后台-营销账户里查看详情"+"\"");
            
            String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
          	 Map<String, String> parameters = new HashMap();
          	 parameters.put("appId", storeBean.getAppId());
          	 parameters.put("openIds", openIds);
          	 parameters.put("message", payCashMsg);
          	 parameters.put("url", "");
          	 String result = HttpClient.sendGet(url, parameters);
             logger.info("#WXService.sendPayCashMsg# result storeNo={},result={}",storeBean.getStoreNo(),result);
        }catch (Exception e){
            logger.error("#WXService.sendPayCashMsg# storeNo={}",storeBean.getStoreNo(),e);
        }
    }
    
    public void sendGetCashMsg(String paymentNo){
        logger.info("#WXService.sendGetCashMsg# paymentNo={}",paymentNo);
        try {
            String getCashMsg = MtConfig.getProperty("GET_CASH_MESSAGE", "");
            if(StringUtils.isEmpty(getCashMsg)){
            	 logger.info("#WXService.getCashMsg# paymentNo={} msg={}",paymentNo,"没有模板消息");
                 return;
            }
            StoreBalanceDetailBean bean = tbStoreBalanceDetailsDao.getByPaymentNo(paymentNo);
            if(bean == null){
            	return;
            }
            
            StoreBean storeBean = tbStoreDao.getStoreByNo(bean.getStoreNo());
            if(StringUtils.isEmpty(storeBean.getAppId())){
            	 logger.info("#WXService.sendPayCashMsg# storeNo={} msg={}",storeBean.getStoreNo(),"没有绑定接收人");
                 return;
            }
            
            List<Long> accountIdList = tbStoreBindAccountDao.getAccountIdByStoreNo(storeBean.getStoreNo());
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            String roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            List<Long> managerIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId), accountIdList);
            if(CollectionUtils.isEmpty(managerIdList)){
            	 logger.info("#WXService.sendPayCashMsg# storeNo={} msg={}",storeBean.getStoreNo(),"没有绑定接收人");
                 return;
            }
            List<String> openIdList = tbStoreAccountDao.getOpenIdByAccountId(managerIdList);
            if(CollectionUtils.isEmpty(openIdList)){
                return;
            }
            String openIds = "";
            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                openIds += openId+",";
            }
            
            getCashMsg = getCashMsg.replace("_first", " \"value\": \"余额提现\"");
            getCashMsg = getCashMsg.replace("_keyword1","\"value\": \" "+ String.valueOf(bean.getPrice()) + "\"");
            getCashMsg = getCashMsg.replace("_keyword2","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\\n提现方式：微信钱包"+"\"");
            getCashMsg = getCashMsg.replace("_remark","\"value\": \""+"请到微信钱包查询到账资金，如有疑问请在公众号里反馈！"+"\"");
            
            String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
          	 Map<String, String> parameters = new HashMap();
          	 parameters.put("appId", storeBean.getAppId());
          	 parameters.put("openIds", openIds);
          	 parameters.put("message", getCashMsg);
          	 parameters.put("url", "");
          	 String result = HttpClient.sendGet(url, parameters);
             logger.info("#WXService.sendPayCashMsg# result paymentNo={},result={}",paymentNo,result);
        }catch (Exception e){
            logger.error("#WXService.sendPayCashMsg# paymentNo={}",paymentNo,e);
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
