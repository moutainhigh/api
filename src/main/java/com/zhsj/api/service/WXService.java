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
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.bean.result.Transfers;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.Arith;
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
    	transfers.setSpbill_create_ip(ip);
//    	transfers.setSpbill_create_ip("114.215.223.220");
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

    //交班统计
    public void sendStoreThift(String storeNo,ShiftBean bean){
        logger.info("#WXService.sendStoreThift# storeNo={}",storeNo);
        try {
            String storeThift = MtConfig.getProperty("STORE_SHIFT_MESSAGE", "");
            if(StringUtils.isEmpty(storeThift)){
            	 logger.info("#WXService.sendStoreThift# storeNo={} msg={}",storeNo,"没有模板消息");
                 return;
            }
            StoreBean storeBean = tbStoreDao.getStoreByNo(storeNo);
            if(storeBean == null || StringUtils.isEmpty(storeBean.getAppId())){
                logger.info("#WXService.sendStoreThift# fail appId is null storeNo={}",storeNo);
                return;
            }
            
            List<Long> accountIdList = tbStoreBindAccountDao.getAccountIdByStoreNo(storeNo);
            if(CollectionUtils.isEmpty(accountIdList)){
                return;
            }
            String roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            List<Long> idList = new ArrayList<>();
            List<Long> managerIdList = tbStoreAccountBindRoleDao.filterAccountIdByRole(Long.parseLong(roleId),accountIdList);
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
            
            String openIds = "";
            for(String openId:openIdList){
                if(StringUtils.isEmpty(openId)){
                    continue;
                }
                openIds += openId+",";
            }
//            +"\\n提现方式：微信钱包"
            storeThift = storeThift.replace("_first", " \"value\": \"\"");
            storeThift = storeThift.replace("_keyword1","\"value\": \" "+ bean.getName() + " 收银数据汇总\"");
            storeThift = storeThift.replace("_keyword2","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\"");
            String startT = bean.getStartTime().substring(11);
            String endT = bean.getEndTime().substring(11);
            String _remark = "\"value\": \""+"当班时间："+startT+"-"+endT+
            		"\\n营业总金额："+bean.getActualMoney()+
            		"\\n"+
					"\\n收银总金额："+bean.getTotalMoney()+
					"\\n收银总笔数： "+bean.getTotalNum()+
					"\\n退款总金额："+bean.getRefundMoney()+
					"\\n退款总笔数："+bean.getRefundNum()+
					"\\n平台减免金额："+bean.getOrgDisMoney()+
					"\\n平台减免笔数："+bean.getOrgDisNum()+
					"\\n商户减免金额："+bean.getStoreDisMoney()+
					"\\n商户减免笔数："+bean.getStoreDisNum()+
					"\"";
            storeThift = storeThift.replace("_remark",_remark);
            
            String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
          	 Map<String, String> parameters = new HashMap();
          	 parameters.put("appId", storeBean.getAppId());
          	 parameters.put("openIds", openIds);
          	 parameters.put("message", storeThift);
          	 parameters.put("url", "");
          	 String result = HttpClient.sendGet(url, parameters);
             logger.info("#WXService.sendStoreThift# result storeNo={},result={}",storeNo,result);
        }catch (Exception e){
            logger.error("#WXService.sendStoreThift# storeNo={}",storeNo,e);
        }
    }
    
    public static void main(String[] args) throws Exception {
    	ShiftBean bean = new ShiftBean();
    	bean.setStartTime(DateUtil.getCurrentTimeHaveHR().substring(11));
    	bean.setEndTime(DateUtil.getCurrentTimeHaveHR().substring(11));
    	bean.setTotalMoney(322);
//    	new WXService().sendStoreThift("1001", bean);
    	String storeThift = MtConfig.getProperty("STORE_SHIFT_MESSAGE", "");
    	storeThift = storeThift.replace("_first", " \"value\": \"\"");
        storeThift = storeThift.replace("_keyword1","\"value\": \" "+ bean.getName() + "收银员数据汇总\"");
        storeThift = storeThift.replace("_keyword2","\"value\": \""+DateUtil.getCurrentTimeHaveHR()+"\"");
        storeThift = storeThift.replace("_keyword3","\"value\": \""+bean.getStartTime()+"-"+bean.getEndTime()+"\"");
        String _remark = "\"value\": \""+"当班时间："+bean.getStartTime()+"-"+bean.getEndTime()+
        					"\\n收银总金额："+bean.getTotalMoney()+
        					"\\n收银总笔数： "+bean.getTotalNum()+
        					"\\n退款总金额："+bean.getRefundMoney()+
        					"\\n退款总笔数："+bean.getRefundNum()+
        					"\\n平台减免金额："+bean.getOrgDisMoney()+
        					"\\n平台减免笔数："+bean.getOrgDisNum()+
        					"\\n商户减免金额："+bean.getStoreDisMoney()+
        					"\\n商户减免笔数："+bean.getStoreDisNum()+
        					"\"";
        storeThift = storeThift.replace("_remark",_remark);
        
        String url = MtConfig.getProperty("OPEN_URL", "")+ "/sendMessage";
      	 Map<String, String> parameters = new HashMap();
      	 parameters.put("appId", "wx79bd044fd98536f4");
      	 parameters.put("openIds", "o5pmes_cN1AMrFptmwpDaNj6DXkI");
      	 parameters.put("message", storeThift);
      	 parameters.put("url", "");
      	 String result = HttpClient.sendGet(url, parameters);
      	 System.out.println(result);
    }

}
