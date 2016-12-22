package com.zhsj.util;

public class StaticConfig
{
	/** 签名. */
	public static final String PARAMS_SIGN = "sign";
	/** 连接 */
	public static final String EQUAL = "=";
	/** 拼接 */
	public static final String AMPERSAND = "&";
	/** 统一使用UTF-8编码 */
	public static final String ENCODING = "UTF-8";
	/** 加密方式：MD5withRSA */
	public static final String ENCRY_MODE_MD5RSA = "MD5withRSA";
	/** 请求地址 */
	public static String REQ_URL = "";
	
	/*------------------静态资源只适合单证书使用----------------------*/
	/** 公钥路径 */
	public static String PUBLIC_KEY_CER_PATH = "";
	/** jssecacerts证书路径 */
	public static String JSSECACERTS_PATH = "";
	/** JKS证书路径 */
	public static String CERT_PATH_JKS = "";
	/** P12证书密码 */
	public static String CERT_PATH_P12 = "";
	/** JKS和P12证书的密码, 密码是统一的 */
	public static String CERT_JKS_P12_PASSWORD = "";
	static {
		REQ_URL = "https://115.28.58.174:6789/updateMerchantByPaykey.do";
		PUBLIC_KEY_CER_PATH = "D:/zhengshu/col_test.cer";
		JSSECACERTS_PATH = "D:/zhsj_worksapce/zhengshu/jssecacerts";
		CERT_PATH_JKS = "D:/zhsj_worksapce/zhengshu/bjwwt@95272016121410000062.jks";
		CERT_PATH_P12 = "D:/zhsj_worksapce/zhengshu/bjwwt@95272016121410000062.p12";
		CERT_JKS_P12_PASSWORD = "bjwwt123456";
	}
}
