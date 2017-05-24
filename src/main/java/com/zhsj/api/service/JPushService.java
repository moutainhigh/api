package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.jpush.PaySuccessBean;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.util.CommonResult;
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
    
    public CommonResult sendSuccessMsg(String orderNo){
    	logger.info("#JPushService.sendSuccessMsg# orderNO={}",orderNo);
    	try{
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
    				jIds.add(accountBean.getjId());
    			}
    		}
//    		List<String> jIds = new ArrayList<>();
//    		jIds.add("18071adc033ca81f2b5");
////    		jIds.add("140fe1da9e9a7c6187b");
    		
    		
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
    			logger.info("json="+json);
    			logger.info("result="+result);
    		}
    		return CommonResult.success("");
    	}catch (Exception e) {
    		logger.error("#JPushService.sendSuccessMsg# orderNO={}",orderNo,e);
		}
    	return CommonResult.defaultError("系统出错");
    	
    }

    
    private String toSuccessMsg(OrderBean bean,List<String> regIds){
    	
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("platform", "all");
    	
    	JSONArray regArray = new JSONArray();
    	regArray.addAll(regIds);
    	JSONObject audience = new JSONObject();
    	audience.put("registration_id", regArray);
    	jsonObject.put("audience", audience);
    	
//    	JSONObject noti = new JSONObject();
//    	noti.put("alert","Hello, JPush!333333333"+DateUtil.getCurrentTime());
//    	jsonObject.put("notification",noti );
    	String uri = MtConfig.getProperty("API_URL", "");
    	PaySuccessBean psBean = new PaySuccessBean().toBean(bean, "qrcode",uri);
    	
    	JSONObject mess = new JSONObject();
    	mess.put("msg_content",JSON.toJSON(psBean));
    	jsonObject.put("message", mess);
    	
    	JSONObject options = new JSONObject();
    	options.put("time_to_live", 30*60);
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
        httpPost.addHeader("Authorization", "Basic "+Base64.encode("976bb4cdb7452b6003ff5d89:ecfcf4526cc0444be0ffb89e".getBytes("UTF-8")).toString());
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
        
//        HttpPost httpPost = new HttpPost(url);
//        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
//        StringEntity postEntity = new StringEntity(postData, "UTF-8");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", "Basic "+Base64.encode("976bb4cdb7452b6003ff5d89:ecfcf4526cc0444be0ffb89e".getBytes("UTF-8")).toString());
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
    
    public static void main(String[] args) throws Exception {
		new JPushService().sendSuccessMsg("2017020518333807cSN0f1846a10");
//    	String url = "https://report.jpush.cn/v3/received?msg_ids=4269690627";
//    	System.out.println(new JPushService().sendGet(url));
    	
	}
    
    
}

