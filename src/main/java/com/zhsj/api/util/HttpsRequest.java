package com.zhsj.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsRequest {
	public interface ResultListener {


        public void onConnectionPoolTimeoutError();

    }

    private static Logger log = LoggerFactory.getLogger(HttpsRequest.class);
    //表示请求器是否已经做了初始化工作
    private boolean hasInit = false;
    //连接超时时间，默认10秒
    private int socketTimeout = 10000;
    //传输超时时间，默认30秒
    private int connectTimeout = 30000;
    //请求器的配置
    private RequestConfig requestConfig;

    //HTTP请求器
    private CloseableHttpClient httpClient;

    public HttpsRequest() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        init();
    }

    private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(MtConfig.getProperty("wx_cert","")));//加载本地的证书进行https加密传输
        try {
            keyStore.load(instream,  MtConfig.getProperty("mchid", "").toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, MtConfig.getProperty("mchid", "").toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

        hasInit = true;
    }

    /**
     * 通过Https往API post xml数据
     *
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    public String sendPost(String url, String postDataXML) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        if (!hasInit) {
            init();
        }

        String result = null;

        HttpPost httpPost = new HttpPost(url);

//        //解决XStream对出现双下划线的bug
//        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
//
//        //将要提交给API的数据对象转换成XML格式数据Post给API
//        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);


        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //设置请求器的配置
        httpPost.setConfig(requestConfig);

        log.info("executing request" + httpPost.getRequestLine());

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
     * 设置连接超时时间
     *
     * @param socketTimeout 连接时长，默认10秒
     */
    public void setSocketTimeout(int socketTimeout) {
        socketTimeout = socketTimeout;
        resetRequestConfig();
    }

    /**
     * 设置传输超时时间
     *
     * @param connectTimeout 传输时长，默认30秒
     */
    public void setConnectTimeout(int connectTimeout) {
        connectTimeout = connectTimeout;
        resetRequestConfig();
    }

    private void resetRequestConfig(){
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
    }

    /**
     * 允许商户自己做更高级更复杂的请求器配置
     *
     * @param requestConfig 设置HttpsRequest的请求器配置
     */
    public void setRequestConfig(RequestConfig requestConfig) {
        requestConfig = requestConfig;
    }
    
    public static void main(String[] args) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException{
    	String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		String postDataXML = "<xml><sign><![CDATA[AA613A1D072D8C62E5DA5F93D686350D]]></sign><amount><![CDATA[100]]></amount><desc><![CDATA[%E6%B5%8B%E8%AF%95%E6%8F%90%E7%8E%B0]]></desc><partner_trade_no><![CDATA[101170324514001824]]></partner_trade_no><mchid><![CDATA[1273081001]]></mchid><spbill_create_ip><![CDATA[192.168.100.105]]></spbill_create_ip><check_name><![CDATA[OPTION_CHECK]]></check_name><openid><![CDATA[o5pmes7HP9w6OhjgjBpc5tTWL8Bs]]></openid><mch_appid><![CDATA[wx79bd044fd98536f4]]></mch_appid><nonce_str><![CDATA[eae1cbf786ae4eb4b1a9ed83256176e7]]></nonce_str><re_user_name><![CDATA[%E5%88%98%E5%BB%BA%E8%8B%B1]]></re_user_name></xml>";
		postDataXML = "<xml><sign><![CDATA[B1540B5084A3501C20772D0BCA3626E9]]></sign><amount><![CDATA[100]]></amount><desc><![CDATA[提现测试]]></desc><partner_trade_no><![CDATA[28377075170326369232897]]></partner_trade_no><mchid><![CDATA[1273081001]]></mchid><spbill_create_ip><![CDATA[114.215.223.220]]></spbill_create_ip><check_name><![CDATA[OPTION_CHECK]]></check_name><openid><![CDATA[o5pmesyob9P9Otj-jl-U3ETnArlY]]></openid><mch_appid><![CDATA[wx79bd044fd98536f4]]></mch_appid><nonce_str><![CDATA[d23c086dfa6545c7a374a7cffbb08751]]></nonce_str><re_user_name><![CDATA[%E8%AE%B8%E6%9E%97%E5%88%9B]]></re_user_name></xml>";
    	System.out.println(new HttpsRequest().sendPost(url, postDataXML));
    }
}
