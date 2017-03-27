package com.zhsj.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WXHttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(WXHttpUtils.class);
	
	public static String post(String url,String json) throws Exception{
		logger.info("#WXHttpUtils.post #url = {},#json = {}", url, json);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		
        FileInputStream instream = new FileInputStream(new File(MtConfig.getProperty("wx_cert","")));
        try {
			keyStore.load(instream, MtConfig.getProperty("mchid", "").toCharArray());
        } catch(Exception e){
        	logger.error("",e);
        	e.printStackTrace();
        }finally {
			instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
			        .loadKeyMaterial(keyStore, MtConfig.getProperty("mchid", "").toCharArray())
			        .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        
        HttpPost httpPost = new HttpPost(url);
        StringEntity sEntity = new StringEntity(json);
		httpPost.setEntity(sEntity);
		CloseableHttpResponse response = null;
		String callback = "";
		try {
				response = httpclient.execute(httpPost);
			
			HttpEntity entity = response.getEntity();  
		    int status = response.getStatusLine().getStatusCode();
		    logger.info("#WXHttpUtils.post #status= {}",status);
		    if(HttpStatus.SC_OK == status){
		    	System.out.println("success");
		    	logger.info("#WXHttpUtils.post #status = {},#success",status);
		    }else{
		    	logger.info("#WXHttpUtils.post #status = {},#fail",status);
		    }
		    if (entity != null) { 
		    	callback = EntityUtils.toString(entity, "UTF-8");
		        logger.info("#WXHttpUtils.post #Response content = {}", callback);
		    }  
		}  catch (ClientProtocolException e) {
			logger.info("WXHttpUtils #ClientProtocolException ",e);
		} catch (IOException e) {
			logger.info("WXHttpUtils #IOException ",e);
		}finally {  
		      if(response != null){
		    	  try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		      if(httpclient != null){
		    	  try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		      }
		      }
        }
		 return callback;
	}
}
