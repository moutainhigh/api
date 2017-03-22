package com.zhsj.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
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
//		json = new String(json.getBytes("utf-8"),"utf-8");
		System.setProperty("javax.net.ssl.trustStore", MtConfig.getProperty("wx_cert",""));
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		TrustManager tm = new X509TrustManager() {      
            public X509Certificate[] getAcceptedIssuers() {      
                return null;      
            }      
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}  
        }; 
        FileInputStream instream = new FileInputStream(new File(MtConfig.getProperty("wx_cert","")));
        try {
			keyStore.load(instream, MtConfig.getProperty("mchid", "").toCharArray());
        } finally {
			instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
			        .loadKeyMaterial(keyStore, MtConfig.getProperty("mchid", "").toCharArray())
			        .build();
        sslcontext.init(null, new TrustManager[] { tm }, null);      
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
        StringEntity sEntity = new StringEntity(json,ContentType.APPLICATION_XML);
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
