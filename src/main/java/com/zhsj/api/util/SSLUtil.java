package com.zhsj.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by lcg on 16/12/16.
 */
public class SSLUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SSLUtil.class);

    /**
     * SSL 的 GET请求
     *
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getSSL(String url) throws KeyStoreException, NoSuchAlgorithmException, URISyntaxException, IOException {
        URL urll = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urll.openConnection();
        connection.setDoOutput(true); // true for POST, false for GET
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setConnectTimeout(180000);// 连接超时时间
        connection.setReadTimeout(180000);
        OutputStream outputStream = null;
        StringBuilder respData = new StringBuilder();
        try {
            connection.connect();

            // //提交数据完成后，收取数据
            if (connection.getResponseCode() == 200) {
                // 读取post之后的返回值
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    respData.append(line + "\r\n");
                }
                in.close();
            } else {
                LOG.error("#SSLUtil.httpsPost# post ResponseCode={},ResponseMessage={}" ,connection.getResponseCode(),connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                connection.disconnect(); // 断开连接
            } catch (Exception e) {
                throw e;
            }
        }
        return respData.toString();
    }

    public static String postSSL(String url,String json) throws KeyStoreException, NoSuchAlgorithmException, URISyntaxException, IOException {
        URL urll = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urll.openConnection();
        connection.setDoOutput(true); // true for POST, false for GET
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setConnectTimeout(180000);// 连接超时时间
        connection.setReadTimeout(180000);
        OutputStream outputStream = null;
        StringBuilder respData = new StringBuilder();
        try {
            outputStream = connection.getOutputStream();
            outputStream.write(json.getBytes("utf-8"));
            outputStream.flush();

            // //提交数据完成后，收取数据
            if (connection.getResponseCode() == 200) {
                // 读取post之后的返回值
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    respData.append(line + "\r\n");
                }
                in.close();
            } else {
                LOG.error("#SSLUtil.httpsPost# post ResponseCode={},ResponseMessage={}" ,connection.getResponseCode(),connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                connection.disconnect(); // 断开连接
            } catch (Exception e) {
                throw e;
            }
        }
        return respData.toString();
    }

    /**
     * 公共提交方法 HTTPS
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String httpsPost(String reqUrl, String data) throws Exception {
    	System.out.println("==============="+reqUrl+"===="+data);
        URL url = new URL(reqUrl);
//        System.setProperty("javax.net.ssl.trustStore", MtConfig.getProperty("JSSECACERTS_PATH","")); //jssecacerts安全认证
//        System.setProperty("javax.net.ssl.keyStore", MtConfig.getProperty("CERT_PATH_JKS",""));// 商户自身的JKS证书
//        System.setProperty("javax.net.ssl.keyStorePassword", MtConfig.getProperty("CERT_JKS_P12_PASSWORD","")); // 商户自身的JKS证书密码
//        System.setProperty("javax.net.ssl.keyStoreType", "jks");
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
        connection.setDoOutput(true); // true for POST, false for GET
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setConnectTimeout(180000);// 连接超时时间
        connection.setReadTimeout(180000);

        OutputStream outputStream = null;
        StringBuilder respData = new StringBuilder();
        try {
            outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes("utf-8"));
            outputStream.flush();

            // //提交数据完成后，收取数据
            if (connection.getResponseCode() == 200) {
                // 读取post之后的返回值
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    respData.append(line + "\r\n");
                }
                in.close();
            } else {
                LOG.error("#SSLUtil.httpsPost# post ResponseCode={},ResponseMessage={}" ,connection.getResponseCode(),connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                connection.disconnect(); // 断开连接
            } catch (Exception e) {
                throw e;
            }
        }
        return respData.toString();
    }
    
    
}
