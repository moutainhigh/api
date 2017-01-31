package com.zhsj.api.util.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpsClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpsClientUtil.class);
	/**
	 * 公共提交方法 HTTPS
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(String reqUrl, String data) throws Exception {
		URL url = new URL(reqUrl);
		System.setProperty("javax.net.ssl.trustStore", StaticConfig.JSSECACERTS_PATH); //jssecacerts安全认证
		System.setProperty("javax.net.ssl.keyStore", StaticConfig.CERT_PATH_JKS);// 商户自身的JKS证书
		System.setProperty("javax.net.ssl.keyStorePassword", StaticConfig.CERT_JKS_P12_PASSWORD); // 商户自身的JKS证书密码
		System.setProperty("javax.net.ssl.keyStoreType", "jks");
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
				logger.error("post ResponseCode=" + connection.getResponseCode());
				logger.error("post ResponseCode=" + connection.getResponseMessage());
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

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
}
