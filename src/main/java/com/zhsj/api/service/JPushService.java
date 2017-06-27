package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.jpush.PaySuccessBean;
import com.zhsj.api.bean.jpush.RefundBean;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.exception.ApiException;
import com.zhsj.api.retry.SimpleRetryTemplate;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MtConfig;


/**
 * Created by lcg on 16/12/29.
 */
@Service
public class JPushService {
    Logger logger = LoggerFactory.getLogger(JPushService.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private PrinterService printerService;
    
    private Map<Integer,String> JPUSH_MSG =  Collections.synchronizedMap(new HashMap<Integer,String>());
    
    public CommonResult sendSuccessMsg(String orderNo){
    	logger.info("#JPushService.sendSuccessMsg# orderNO={}",orderNo);
    	try{
    		printerService.printByOrder(orderNo);
    		
    		OrderBean orderBean = orderService.getByOrderId(orderNo);
    		if(orderBean == null){
    			return CommonResult.success("订单号不存在");
    		}
    		List<Long> accountIdsList = tbStoreBindAccountDao.getAccountIdByStoreNo(orderBean.getStoreNo());
    		if(CollectionUtils.isEmpty(accountIdsList)){
    			return CommonResult.success("没有用户");
    		}
    		
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getSignByIds(accountIdsList,1);
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.success("没有签到用户");
    		}
    		List<String> jIds = new ArrayList<>();
    		for(StoreAccountBean accountBean:accountBeans){
    			if(StringUtils.isNotEmpty(accountBean.getjId())){
    				if(orderBean.getAccountId() <= 0){
    					jIds.add(accountBean.getjId());
    				}else if(orderBean.getAccountId() == accountBean.getId()){
    					jIds.add(accountBean.getjId());
    				}
    			}
    		}
    		if(CollectionUtils.isEmpty(jIds)){
    			return CommonResult.success("没有注册用户");
    		}
    		int maxSize = 100;
    		int totalSize = jIds.size();
    		int totalPage = totalSize / maxSize + ((totalSize % maxSize) == 0 ? 0 : 1);
    		for (int i = 0; i < totalPage; i++) {
    			List<String> regIds = jIds.subList(i * maxSize,  Math.min(totalSize, (i + 1) * maxSize));
    			String json = toSuccessMsg(orderBean, regIds);
    			String result = sendPost("https://api.jpush.cn/v3/push", json);
    			logger.info("result="+result);
    			Map<String, String> map = JSON.parseObject(result, Map.class);
    			JPUSH_MSG.put((int)orderBean.getId(), map.get("msg_id"));
    			sendJPushMsg((int)orderBean.getId(),json);
    		}
    		
    		return CommonResult.success("");
    	}catch (Exception e) {
    		logger.error("#JPushService.sendSuccessMsg# orderNO={}",orderNo,e);
		}
    	return CommonResult.defaultError("系统出错");
    	
    }
    
    
    public CommonResult sendRefundMsg(String orderNo,long accountId){
    	logger.info("#JPushService.sendRefundMsg# orderNO={}",orderNo);
    	try{
//    		printerService.printByOrder(orderNo);
    		
    		OrderBean orderBean = orderService.getByOrderId(orderNo);
    		if(orderBean == null){
    			return CommonResult.success("订单号不存在");
    		}
    		List<Long> accountIdsList = tbStoreBindAccountDao.getAccountIdByStoreNo(orderBean.getStoreNo());
    		if(CollectionUtils.isEmpty(accountIdsList)){
    			return CommonResult.success("没有用户");
    		}
    		
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getSignByIds(accountIdsList,1);
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.success("没有签到用户");
    		}
    		List<String> jIds = new ArrayList<>();
    		for(StoreAccountBean accountBean:accountBeans){
    			if(accountBean.getId() == accountId){
    				jIds.add(accountBean.getjId());
    			}
    		}
    		if(CollectionUtils.isEmpty(jIds)){
    			return CommonResult.success("没有注册用户");
    		}
    		int maxSize = 100;
    		int totalSize = jIds.size();
    		int totalPage = totalSize / maxSize + ((totalSize % maxSize) == 0 ? 0 : 1);
    		for (int i = 0; i < totalPage; i++) {
    			List<String> regIds = jIds.subList(i * maxSize,  Math.min(totalSize, (i + 1) * maxSize));
    			String json = toRefundMsg(orderBean, regIds);
    			String result = sendPost("https://api.jpush.cn/v3/push", json);
    			logger.info("result="+result);
//    			Map<String, String> map = JSON.parseObject(result, Map.class);
//    			JPUSH_MSG.put((int)orderBean.getId(), map.get("msg_id"));
//    			sendJPushMsg((int)orderBean.getId(),json);
    		}
    		
    		return CommonResult.success("");
    	}catch (Exception e) {
    		logger.error("#JPushService.sendSuccessMsg# orderNO={}",orderNo,e);
		}
    	return CommonResult.defaultError("系统出错");
    	
    }

    
    private String toSuccessMsg(OrderBean bean,List<String> regIds){
    	String uri = MtConfig.getProperty("API_URL", "");
    	PaySuccessBean psBean = new PaySuccessBean().toBean(bean, "",uri);
    	
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("platform", "all");//推送平台
    	
    	//推送设备
    	JSONArray regArray = new JSONArray();
    	regArray.addAll(regIds);
    	JSONObject audience = new JSONObject();
    	audience.put("registration_id", regArray);
    	jsonObject.put("audience", audience);
    	
    	//ios以通知下发消息
    	JSONObject iosJson = new JSONObject();
    	iosJson.put("alert", psBean.getNt());
//    	iosJson.put("content-available", true);
    	iosJson.put("badge", 1);
    	iosJson.put("sound", "default");
    	iosJson.put("mutable-content", true);

    	JSONObject dataJson = new JSONObject();
    	dataJson.put("data", JSON.toJSON(psBean));
    	iosJson.put("extras", dataJson);
    	
    	JSONObject notification = new JSONObject();
    	notification.put("ios", iosJson);
    	
    	if("1110674590".equals(bean.getStoreNo())){
    		JSONObject androidJson = new JSONObject();
    		androidJson.put("alert", psBean.getNt());
    		notification.put("android", androidJson);
    	}
    	jsonObject.put("notification", notification);
    	
    	//自定义消息
    	JSONObject mess = new JSONObject();
    	mess.put("msg_content",JSON.toJSON(psBean));
    	jsonObject.put("message", mess);
    	
    	//可选项
    	JSONObject options = new JSONObject();
    	options.put("time_to_live", 10*60);
    	options.put("sendno", bean.getId());
    	String apns_production = MtConfig.getProperty("JG_IOS_APNS_PRODUCTION", "fasle");
    	options.put("apns_production", "true".equals(apns_production)?true:false);
    	options.put("apns_collapse_id", bean.getOrderId());
    	jsonObject.put("options", options);
    	return jsonObject.toJSONString();
    }
    
    private String toRefundMsg(OrderBean bean,List<String> regIds){
    	
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("platform", "all");
    	
    	JSONArray regArray = new JSONArray();
    	regArray.addAll(regIds);
    	JSONObject audience = new JSONObject();
    	audience.put("registration_id", regArray);
    	jsonObject.put("audience", audience);
    	
    	RefundBean psBean = new RefundBean().toBean(bean);
    	
    	JSONObject mess = new JSONObject();
    	mess.put("msg_content",JSON.toJSON(psBean));
    	jsonObject.put("message", mess);
    	
    	JSONObject options = new JSONObject();
    	options.put("time_to_live", 60*60);
    	options.put("sendno", bean.getId());
    	jsonObject.put("options", options);
    	return jsonObject.toJSONString();
    }
    
    /**
     * 通过Https往API post xml数据
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws Exception
     */
    private String sendPost(String url, String postData) throws Exception {
    	int socketTimeout = 10000; //连接超时时间，默认10秒
        int connectTimeout = 30000; //传输超时时间，默认30秒
    	CloseableHttpClient httpClient = HttpClients.custom().build();
    	//根据默认超时限制初始化requestConfig
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

        String result = null;
        HttpPost httpPost = new HttpPost(url);
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postData, "UTF-8");
        httpPost.addHeader("Content-Type", "application/json");
        String JG_APP_KEY = MtConfig.getProperty("JG_APP_KEY", "");
        String JG_MASTER_SECRET = MtConfig.getProperty("JG_MASTER_SECRET", "");
        httpPost.addHeader("Authorization", "Basic "+Base64.encode((JG_APP_KEY+":"+JG_MASTER_SECRET).getBytes("UTF-8")).toString());
        httpPost.setEntity(postEntity);
        //设置请求器的配置
        httpPost.setConfig(requestConfig);

        logger.info("executing request" + httpPost.getRequestLine());

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.abort();
        }

        return result;
    }
    
    /**
     * 通过Https往API post xml数据
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws Exception
     */
    private String sendGet(String url) throws Exception {
    	int socketTimeout = 10000; //连接超时时间，默认10秒
        int connectTimeout = 30000; //传输超时时间，默认30秒
    	CloseableHttpClient httpClient = HttpClients.custom().build();
    	//根据默认超时限制初始化requestConfig
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        String JG_APP_KEY = MtConfig.getProperty("JG_APP_KEY", "");
        String JG_MASTER_SECRET = MtConfig.getProperty("JG_MASTER_SECRET", "");
        httpGet.addHeader("Authorization", "Basic "+Base64.encode((JG_APP_KEY+":"+JG_MASTER_SECRET).getBytes("UTF-8")).toString());
      
        //设置请求器的配置
        httpGet.setConfig(requestConfig);
        logger.info("executing request" + httpGet.getRequestLine());
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	httpGet.abort();
        }

        return result;
    }
    
    public interface ResultListener {
        public void onConnectionPoolTimeoutError();

    }
    
    private void sendJPushMsg(final int sendNo,final String json) throws Exception{
    	Integer num = new SimpleRetryTemplate<Integer>() {
			@Override
			public Integer invoke() throws Exception {
				String msg_id = JPUSH_MSG.get(sendNo);
				String url = "https://report.jpush.cn/v3/received?msg_ids="+msg_id;
				String result = new JPushService().sendGet(url);
    	    	JSONArray jsonArray = JSON.parseArray(result);
    	    	for(int i=0;i<jsonArray.size();i++){
    	    		String jmsg = jsonArray.get(i).toString();
    	    		Map<String, Object> getMap = JSON.parseObject(jmsg,Map.class);
    	    		if(getMap.get("android_received") == null && getMap.get("ios_msg_received")==null){
    	    			logger.info("json="+json);
    					result = sendPost("https://api.jpush.cn/v3/push", json);
    	    			logger.info("result="+result);
    	    			Map<String, String> map = JSON.parseObject(result, Map.class);
    	    			JPUSH_MSG.put(sendNo, map.get("msg_id"));
    	    			throw new ApiException(1002, "极光推送失败");
    	    		}
    	    	}
				return 0;
			}
		}.withDefaultTimeoutPolicy().executeWithRetry(30000L);
    }
    
    public static void main(String[] args) throws Exception {
//		new JPushService().sendSuccessMsg("18071adc033cab91e3e");
    	List list = new ArrayList<>();
//    	list.add("191e35f7e07307e7858");
//    	list.add("191e35f7e073dd9e2f3");
    	list.add("18071adc033cab91e3e");
//    	list.add("140fe1da9e9a73f88cc");
//    	list.add("191e35f7e073064cff9");
    	
    	
    	OrderBean orderBean = new OrderBean();
    	orderBean.setId(1);
    	orderBean.setOrderId("15");
    	orderBean.setCtime(1497234339);
    	orderBean.setPayMethod("1");
    	orderBean.setActualChargeAmount(0.04);
    	orderBean.setPlanChargeAmount(0.12);
    	orderBean.setStatus(1);
    	orderBean.setStoreNo("1110674590");
    	
    	String json = new JPushService().toSuccessMsg(orderBean, list);
    	System.out.println(json);
//		String result = sendPost("https://api.jpush.cn/v3/push", json);
//    	String json = "{\"message\":{\"msg_content\":{\"nt\":\"你有一笔0.01元订单支付成功\",\"time\":\"2017-05-25 10:16\",\"cmd\":1,\"no\":\"10001170525369841513\",\"am\":\"0.01\",\"pt\":\"微信\",\"pm\":\"0.01\",\"st\":\"成功\",\"code\":\"671\",\"url\":\"http://wwt.bj37du.com/api/10001170525369841513\",\"qr\":\"qrcode\"}},\"platform\":\"all\",\"audience\":{\"registration_id\":[\"18071adc033cab91e3e\"]},\"options\":{\"time_to_live\":1800,\"sendno\":\"33\"}}";
    	String resultString = new JPushService().sendPost("https://api.jpush.cn/v3/push", json);
    	System.out.println(resultString);
    	Map<String, String> map = JSON.parseObject(resultString, Map.class);
//    	System.out.println(map.get("msg_id"));
    	String url = "https://report.jpush.cn/v3/received?msg_ids="+map.get("msg_id");
    	String result = new JPushService().sendGet(url);
    	System.out.println(result);
    	JSONArray jsonArray = JSON.parseArray(result);
    	for(int i=0;i<jsonArray.size();i++){
    		String jmsg = jsonArray.get(i).toString();
    		Map<String, Object> getMap = JSON.parseObject(jmsg,Map.class);
    		if(getMap.get("android_received") == null && getMap.get("ios_msg_received")==null){
    			throw new ApiException(1002, "极光推送失败");
    		}else{
    			System.out.print(result);
    		}
    	}
		
	}
    
    
}

