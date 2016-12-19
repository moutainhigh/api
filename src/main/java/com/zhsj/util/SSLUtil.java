package com.zhsj.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpGet get = new HttpGet();
        get.setURI(new URI(url));
        CloseableHttpResponse response = httpClient.execute(get);
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            LOG.error("getSSL failed# url:({})", url, e);
        } finally {
            response.close();
        }
        return result;
    }

    /**
     * 创建SSL HTTP客户端
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() throws KeyStoreException, NoSuchAlgorithmException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            LOG.error("KeyManagementException error.", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException error.", e);
        } catch (KeyStoreException e) {
            LOG.error("KeyStoreException error.", e);
        }
        return HttpClients.createDefault();
    }
}
