package com.zhsj.api.util.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertUtil
{
	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	/**
	 * 请求签名
	 * 
	 * @param reqData
	 *            请求数据
	 * @param certPath
	 *            证书路径
	 * @param password
	 *            密码
	 * @return 签名后并且Base64编码后的字符串
	 */
	public static String reqSign(String reqData, String certPath, String password) throws Exception
	{
		PrivateKey privateKey = getPrivatekey(certPath, password);
		Signature signature = Signature.getInstance(StaticConfig.ENCRY_MODE_MD5RSA);
		signature.initSign(privateKey);
		signature.update(reqData.getBytes("UTF-8"));
		return new String(base64Encode(signature.sign()));
	}

	/**
	 * 获取私钥信息
	 * 
	 * @param certPath
	 *            私钥证书路径
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivatekey(String certPath, String password) throws Exception
	{
		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream fis = new FileInputStream(certPath);

		char[] nPassword = null;
		if (StringUtils.isBlank(password))
			nPassword = null;
		else
			nPassword = password.toCharArray();
		ks.load(fis, nPassword);
		fis.close();

		Enumeration<?> enum1 = ks.aliases();
		String keyAlias = null;
		if (enum1.hasMoreElements())
		{
			keyAlias = (String) enum1.nextElement();
		}
		PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
		return prikey;
	}

	/**
	 * 对COL返回的信息进行验签
	 * @param rspMap
	 * @return
	 */
	public static boolean attestation(Map<String, String> rspMap)
	{
		try
		{
			if(StringUtils.isBlank(rspMap.get("sign")))
				return false;
			
			InputStream streamCert = new FileInputStream(StaticConfig.PUBLIC_KEY_CER_PATH);
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Certificate cert = (Certificate) factory.generateCertificate(streamCert);
			Signature signature = Signature.getInstance(StaticConfig.ENCRY_MODE_MD5RSA);
			signature.initVerify(cert.getPublicKey());
			signature.update(MapUtil.coverMap2String(rspMap).getBytes("UTF-8"));
			return signature.verify(base64Decode(rspMap.get("sign").getBytes()));
		} catch (Exception e)
		{
			logger.info("验签时发生异常：e={}" , e.getMessage(),e);
			return false;
		}
	}

	/**
	 * BASE64解码
	 * 
	 * @param inputByte
	 *            待解码数据
	 * @return 解码后的数据
	 * @throws IOException
	 */
	public static byte[] base64Decode(byte[] inputByte) throws IOException
	{
		return Base64.decodeBase64(inputByte);
	}

	/**
	 * BASE64编码
	 * 
	 * @param inputByte
	 *            待编码数据
	 * @return 解码后的数据
	 * @throws IOException
	 */
	public static byte[] base64Encode(byte[] inputByte) throws IOException
	{
		return Base64.encodeBase64(inputByte);
	}

}
