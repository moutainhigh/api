package com.zhsj.api.util.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Merchant
{
	private static final Logger logger = LoggerFactory.getLogger(HttpsClientUtil.class);
	public static void main(String[] args)
	{
		/**
		 * 
		 * 使用前请先修改StaticConfig中的证书路径配置
		 */
//		StaticConfig.REQ_URL = "https://115.28.58.174:6789/merSettled.do";
		//商户入驻
//		mersettled();
		//商户信息修改
		updateMerchantByPaykey();
	}
	
	public static void mersettled() {
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("reg_contact_tel", "18601345718"); //商户手机号
        reqData.put("legal_person", "王军");//法定代表人姓名
        reqData.put("legal_person_id", "14232219840929001X"); //法定代表人身份证号
        reqData.put("mer_email", "289220440@qq.com");//商户联系邮箱
        reqData.put("filed1", "18601345718"); //入驻商户的客服电话
        reqData.put("agent_no", "95272016121410000062aaaaaa");//此字符串由民生提供
        reqData.put("wx_business_type", "277"); //商户营业类别
        reqData.put("ali_business_type", "2016062900190196"); //支付宝口碑类目
        reqData.put("mer_name", "万物通科技(北京)有限公司"); //商户全称
        reqData.put("wx_rate", "0.5");   //微信费率     示例:0.5，代表和该入驻商户签约千分之5的费率      
        reqData.put("ali_rate", "0.5");   //支付宝费率     示例:0.5，代表和该入驻商户签约千分之5的费率       
        reqData.put("sa_name", "王军");   //结算账户名称       G
        reqData.put("sa_num", "6216920053824716");   //结算账户账号     
        reqData.put("sa_bank_name", "中国民生银行");   //结算账户银行 
        reqData.put("sa_bank_type", "00");   //结算账户类型(对公=01)(对私=00)
		reqData.put("settlement_type", "T1");   //商户结算类型D0:实时清算,T1:隔天清算 若不传，默认D0清算
        reqData.put("user_pid", "2088421759224451");//支付宝ISV的pid
        reqData.put("mer_short_name", "简称");//商户简称

	try
	{
		String signString = CertUtil.reqSign(MapUtil.coverMap2String(reqData), StaticConfig.CERT_PATH_P12,
				StaticConfig.CERT_JKS_P12_PASSWORD);
		reqData.put("sign", new String(signString)); // 签名后的字符串
		String stringData = MapUtil.getRequestParamString(reqData);

		String reqBase64 = new String(CertUtil.base64Encode(stringData.getBytes("UTF-8")));
		String rspBase64 = HttpsClientUtil.httpsPost(StaticConfig.REQ_URL, reqBase64);
		String rspData = new String(CertUtil.base64Decode(rspBase64.getBytes("UTF-8")));
		System.out.println(rspData);
		Map<String, String> rspMap = MapUtil.convertResultStringToMap(rspData);
		if (CertUtil.attestation(rspMap))
		{
			logger.info("验签成功");
			String respCode = rspMap.get("result_code");
			System.out.println("result_code：" + respCode);
		} else
		{
			System.out.println("验签失败");
		}
	} catch (Exception e)
	{
		e.printStackTrace();
	}
	}
	
	public static void updateMerchantByPaykey() {
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("paykey", "3eb3bf9ba2994b57929fc2193c1dc4e7"); //修改商户的paykey
        reqData.put("agent_no", "95272016121410000062");//代理商编号
        reqData.put("wx_rate", "0.25"); //微信费率
        reqData.put("ali_rate", "0.25");//支付宝费率
        reqData.put("settlement_type", "T1");//商户结算周期
	try
	{
		String signString = CertUtil.reqSign(MapUtil.coverMap2String(reqData), StaticConfig.CERT_PATH_P12,
				StaticConfig.CERT_JKS_P12_PASSWORD);
		reqData.put("sign", new String(signString)); // 签名后的字符串
		String stringData = MapUtil.getRequestParamString(reqData);

		String reqBase64 = new String(CertUtil.base64Encode(stringData.getBytes("UTF-8")));
		String rspBase64 = HttpsClientUtil.httpsPost(StaticConfig.REQ_URL, reqBase64);
		String rspData = new String(CertUtil.base64Decode(rspBase64.getBytes("UTF-8")));
		//将返回结果转换为map
		Map<String, String> rspMap = MapUtil.convertResultStringToMap(rspData);
		if (CertUtil.attestation(rspMap))
		{
			logger.info("验签成功");
			String respCode = rspMap.get("result_code");
			System.out.println("result_code：" + respCode);
		} else
		{
			logger.info("验签失败");
		}
	} catch (Exception e)
	{
		e.printStackTrace();
	}
	}
	
}
