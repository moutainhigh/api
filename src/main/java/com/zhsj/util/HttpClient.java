package com.zhsj.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;




public class HttpClient {
	/**
	 * 发送GET请求
	 *
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendGet(String url, Map<String, String> parameters) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		StringBuffer sb = new StringBuffer();// 存储参数
		String params = "";// 编码之后的参数
		try {
			// 编码请求参数
			if (parameters.size() == 1) {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8"));
				}
				params = sb.toString();
			} else {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8"))
							.append("&");
				}
				String temp_params = sb.toString();
				params = temp_params.substring(0, temp_params.length() - 1);
			}
			String full_url = url + "?" + params;
			System.out.println(full_url);
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(full_url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 建立实际的连接
			httpConn.connect();
			// 响应头部获取
			Map<String, List<String>> headers = httpConn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : headers.keySet()) {
				System.out.println(key + "\t：\t" + headers.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应,并设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发送POST请求
	 *
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendPost(String url, Map<String, Object> parameters) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		String params = "";// 编码之后的参数
		try {
			// 编码请求参数
			if (parameters.size() == 1) {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name).toString(), "UTF-8"));
				}
				params = sb.toString();
			} else {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name).toString(), "UTF-8"))
							.append("&");
				}
				String temp_params = sb.toString();
				params = temp_params.substring(0, temp_params.length() - 1);
			}
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取参数签名
	 * @param paramMap  签名参数
	 * @param paySecret 签名密钥
	 * @return
	 */
	public static String  getSign (Map<String , Object> paramMap , String paySecret){
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Object> m : smap.entrySet()) {
			Object value = m.getValue();
			if (value != null && StringUtils.isNotBlank(String.valueOf(value))){
				stringBuffer.append(m.getKey()).append("=").append(m.getValue()).append("&");
			}
		}
		stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

		String argPreSign = stringBuffer.append("&paySecret=").append(paySecret).toString();
		String signStr = MD5Util.encode(argPreSign).toUpperCase();

		return signStr;
	}

	/**
	 * 验证商户签名
	 * @param paramMap  签名参数
	 * @param paySecret 签名私钥
	 * @param signStr   原始签名密文
	 * @return
	 */
	public static boolean isRightSign(Map<String , Object> paramMap , String paySecret ,String signStr){

		if (StringUtils.isBlank(signStr)){
			return false;
		}

		String sign = getSign(paramMap, paySecret);
		if(signStr.equals(sign)){
			return true;
		}else{
			return false;
		}
	}

	public static void ALI_Test()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("auth_code", "286133760533666735");//打开手机支付宝app，点击“付款”，请将那一串数字填入
		parameters.put("order_price", "0.01"); //支付费用单位：元，支付一分钱
		parameters.put("pay_way_code", "ALIF2F");//当面付标识
		//入驻商户号+yyyyMMddHHmmss
		parameters.put("order_no", "139" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		parameters.put("paykey", "01b0e7c141634a1e8ed4c0bd3c51bb58");//此字符串由民生提供，作为商户的唯一标识
		parameters.put("product_name", "条码支付测试"); //产品名称
		parameters.put("order_ip", "210.230.54.222"); //发起交易的服务器地址
		parameters.put("remark", "测测"); //附件内容
//        parameters.put("orderPeriod", "5");
//
//        parameters.put("returnUrl", "http://");
//        parameters.put("notifyUrl", "http://");

		parameters.put("field1", "139"); //可不填，建议填写支付宝入驻商户户号，由民生提供
		parameters.put("field2", "大笨钟"); //可不填，建议填写代理商名称
		parameters.put("field3", "扩展字段c");
		parameters.put("field4", "扩展字段d");
		parameters.put("field5", "扩展字段e");
		/////签名及生成请求API的方法///
		String paySec = "6244125c3c0c44a599feb9ebd513ea95";
		String sign = getSign(parameters, paySec);////此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);

		String postUrl = Url+"/qthd-pay-web-gateway/f2fPay/scanningPay";
		String result = sendPost(postUrl, parameters);
		System.out.println(result);
	}

	public static void WX_Test(String auth_code)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("auth_code", auth_code);//打开手机微信，点击“我”-“钱包”-“付款”，请将那一串数字填入
		parameters.put("order_price", "0.01"); //支付费用单位：元，支付一分钱
		parameters.put("pay_way_code", "WXF2F");//当面付标识
		parameters.put("order_no", "cmbc13979965_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); //入驻商户号+yyyyMMddHHmmss   cmbc13979965_20160901115143
		parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		parameters.put("paykey", payKey);//此字符串由民生提供，作为商户的唯一标识
		parameters.put("product_name", "微信扫码测试"); //产品名称
		parameters.put("order_ip", "210.230.54.222"); //发起交易的服务器地址
		parameters.put("remark", "微信主扫测试"); //附件内容
//        parameters.put("orderPeriod", "5");

		parameters.put("field1", "13979965"); //可不填，建议填写支付宝入驻商户户号，由民生提供
		parameters.put("field2", "大笨钟"); //可不填，建议填写代理商名称
		parameters.put("field3", "扩展字段c");
		parameters.put("field4", "扩展字段d");
		parameters.put("field5", "扩展字段e");
		/////签名及生成请求API的方法///
		String sign = getSign(parameters, paySec);////此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);


		String postUrl = Url+"/qthd-pay-web-gateway/f2fPay/scanningPay";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	//微信公众号支付
	public static void WxPrePay_Test()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("order_price", "0.01"); //支付费用单位：元，支付一分钱
		parameters.put("pay_way_code", "WXF2F");//当面付标识
		parameters.put("order_no", "cmbc13979965" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); //编码规则：入驻商户号+yyyyMMddHHmmss
		parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		parameters.put("paykey", payKey);//此字符串由民生提供，作为商户的唯一标识
		parameters.put("product_name", "微信公众号支付测试"); //产品名称，商户根据自己的需求填写
		parameters.put("order_ip", "210.230.54.222"); //发起交易的服务器地址
		parameters.put("remark", "微信主扫测试"); //附件内容
		parameters.put("order_period", "5");   //必填
		parameters.put("notify_url", "http://lijia.liu.con/pay/notify/minsheng/jspay_notify.do");//必填，请填写通知地址
		parameters.put("field1", "oOmQTwHs3o-VS40wnE8wtLm0vkdU"); //必填，公众号获取到的某人的openid，具体的一个人    oOmQTwHs3o-VS40wnE8wtLm0vkdU //oOmQTwN0Ay63eVyJc25zTQc_M5vE
		parameters.put("field2", "大笨钟"); //可不填，建议填写代理商名称
		parameters.put("field3", "扩展字段c");
		parameters.put("field4", "扩展字段d");
		parameters.put("field5", "扩展字段e");

		String sign = getSign(parameters, paySec);////此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);

		String postUrl = Url+"/qthd-pay-web-gateway/scanPay/wxPrePay";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	public static void queryOrder_Test(String orderNo)
	{   //查订单
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paykey",payKey);
		parameters.put("order_no",orderNo);  //请自己试试自己发起的订单
		/////签名///
		String sign = getSign(parameters, paySec);//
		parameters.put("sign",sign);
		String postUrl = Url+"/qthd-pay-web-gateway/scanPay/QueryReturnJson";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	public static void refund_Test(String orderNo)
	{   //退款
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paykey",payKey);//01b0e7c141634a1e8ed4c0bd3c51bb58
		parameters.put("sub_app_id","wx4c5dfa66e54d8866");  //商家的微信公众号，必须和入驻二级商户号对应
		parameters.put("order_no",orderNo);  //请自己试试自己发起的订单
		parameters.put("device_info",3625);  //目前保留，后期会用
		//入驻商户号+yyyyMMddHHmmss  商户发起的退款订单号
		parameters.put("out_refund_no","refund13979965_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));  //请自己试试自己发起的订单
		parameters.put("order_price","0.01");  //订单总金额 单位：圆
		parameters.put("refund_fee","0.01");  //订单总金额
		parameters.put("op_user_id",1);  //操作员id,目前保留，后期会用
		parameters.put("pay_way_code","WXF2F");  //订单的支付方式 WXF2F
		/////签名///
		String sign = getSign(parameters, paySec);//此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);
		String postUrl = Url+"/qthd-pay-web-gateway/scanPay/Refund";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	public static void QueryRefund_Test(String out_refund_no )
	{   //查询退款订单
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("paykey",payKey);//此字符串由民生提供，作为商户的唯一标识
		parameters.put("out_refund_no",out_refund_no);  //请自己试试自己发起的订单
		parameters.put("device_info",3625);  //目前保留，后期会用
		parameters.put("pay_way_code","WXF2F");  //订单的支付方式 WXF2F
		/////签名///
		String sign = getSign(parameters, paySec);//
		parameters.put("sign",sign);
		String postUrl = Url+"/qthd-pay-web-gateway/scanPay/QueryRefund";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	//微信公众号支付
	public static void WxNativePay_Test()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("order_price", "0.01"); //支付费用单位：元，支付一分钱
		parameters.put("pay_way_code", "WXF2F");//当面付标识
		parameters.put("order_no", "cmbc13979965_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); ////入驻商户号+yyyyMMddHHmmss
		parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		parameters.put("paykey", payKey);//此字符串由民生提供，作为商户的唯一标识
		parameters.put("product_name", "微信公众号支付测试"); //产品名称，商户根据自己的需求填写
		parameters.put("order_ip", "210.230.54.222"); //发起交易的服务器地址
		parameters.put("remark", "微信主扫测试"); //附件内容
		parameters.put("order_period", "5");   //必填
		parameters.put("notify_url", "http://lijia.liu.con/pay/notify/minsheng/jspay_notify.do");//必填，请填写通知地址
		parameters.put("field1", "eeee"); //可不填
		parameters.put("field2", "大笨钟"); //可不填，建议填写代理商名称
		parameters.put("field3", "扩展字段c");
		parameters.put("field4", "扩展字段d");
		parameters.put("field5", "扩展字段e");

		String sign = getSign(parameters, paySec);////paySec 此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);

		String postUrl = Url+"/qthd-pay-web-gateway/scanPay/wxNativePay";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	/*此二字段由民生提供给各个商户*/
	static String  payKey = "85a6c4e20bf54505bea8e75bc870d587";//此字符串由民生提供，作为商户的唯一标识
	static String  paySec = "60f811a8b472495fa6c656c507f44cdc";//此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
	static String Url = "http://115.159.235.109:8208";

	public static void merchant()
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("reg_contact_tel", "18601345718"); //商户手机号
		parameters.put("legal_person", "王军");//法定代表人姓名
		parameters.put("legal_person_id", "14232219840929001X"); //法定代表人身份证号
		parameters.put("mer_email", "289220440@qq.com");//商户联系邮箱
		parameters.put("filed1", "18601345718"); //入驻商户的客服电话
//		parameters.put("paykey", "01b0e7c141634a1e8ed4c0bd3c51bb58");//此字符串由民生提供，作为商户的唯一标识
		parameters.put("wx_business_type", "111"); //商户营业类别
		parameters.put("ali_business_type", "2015062600009243"); //支付宝口碑类目2015062600009243
		parameters.put("mer_name", "智慧商街"); //商户全称
		parameters.put("wx_rate", "0.5");   //微信费率     示例:0.5，代表和该入驻商户签约千分之5的费率
		parameters.put("ali_rate", "0.5");   //支付宝费率     示例:0.5，代表和该入驻商户签约千分之5的费率
		parameters.put("sa_name", "王军");   //结算账户名称
		parameters.put("sa_num", "6216920053824716");   //结算账户账号
		parameters.put("sa_bank_name", "中国民生银行");   //结算账户银行
		parameters.put("sa_bank_type", "01");   //结算账户类型(对公=01)(对私=00)
		parameters.put("settlement_type", "D0");   //商户结算类型D0:实时清算,T1:隔天清算,T0:当天清算  若不传，默认D0清算

		String sign = getSign(parameters, "6244125c3c0c44a599feb9ebd513ea95");////paySec 此字符串由民生提供，用作代理商投递信息加密用，请妥善保管，请只放在服务器端
		parameters.put("sign",sign);

//        String postUrl = Url+"/qthd-pay-web-gateway/scanPay/wxNativePay";
		String postUrl = "http://115.28.58.174:8208/CBMSOnLine/merSettled.do";
		String result = sendPost(postUrl ,  parameters);
		System.out.println(result);
	}

	/* 主函数，测试请求 */
	public static void main(String[] args) {
//		merchant();
//		String method = "/CBMSOnLine/merSettled.do";
//		String reg_contact_tel = "18601345718" ;//商户手机号（唯一标识） 32
//		String legal_person = "王军" ;//法定代表人姓名  32
//		String legal_person_id = "14232219840929001X"; //法定代表人身份证号码 32
//		String mer_email = "289220440@qq.com"; //商户联系邮箱 20
//		String filed1 = "18601345718";//入驻商户的客服电话 20
//
//		String wx_business_type= ""; //商户营业类别，见附件1（示例：17）否（若有微信支付业务，必填 50
//		String ali_business_type = "" ;// 支付宝口碑类目，见附件2（示例：2016031800154367）否（若有支付宝支付业务，必填） 30
//		String mer_name = "智慧商街"; //商户全称 50
//
//		String wx_rate = "0.5" ;//微信费率（示例:0.5，代表和该入驻商户签约千分之5的费率）否（若有微信支付业务，必填） 200
//
//		String ali_rate = "0.5" ;// 支付宝费率（示例:0.5，代表和该入驻商户签约千分之5的费率）否（若开通支付宝业务必填） 500
//
//		String sa_name = "王军";//结算账户名称 500
//		String sa_num = "6216920053824716";//结算账户账号 500
//		String sa_bank_name = "中国民生银行";//结算账户银行，参照银行对照表（示例：工商银行）500
//		String sa_bank_type = "";//结算账户类型 500
//		String paykey = "";//平台分配（该paykey是平台分配代理商的paykey）500
//		String sign =	"";//	签名字段 32
//		String settlement_type=	"";//商户结算周期及模式：T1：工作日第二天到账（一笔）；T0：工作日当天到账（多笔）；D0：自然日当天到账（逐笔）否（默认D0） 10


		// createSgin_Test();
		// 说明:请在以下函数中正确填写由民生提供的参数
		// payKey : 标识商户唯一性
		// paySec ： 对投递数据加密
		// postUrl ： 投送的地址
		// authCode 此字段从支付宝或微信中获取
		// ---------测试函数---------------

		//支付宝
		// ali商户主扫
		//ZuSao_Ali_Wx_Test("289701156653117266", "ALIF2F");
		//动态二维码
		//NativePay_Ali_Wx_Test("ALIF2F");
		//门店固定二维码
//		AliFixPay_Test("2088102926587215");
		// 订单查询
		//queryOrder_Test("cmbc20161025112252");
		//查询清算信息
		//querySettlement_Test("cmbc13920161001012326");
		//退款
		//refund_Test("cmbc20161018003553","ALIF2F");
		// 退款订单查询
		//QueryRefund_Test("refund13979965_20160923215511","ALIF2F");


		//微信
		//ZuSao_Ali_Wx_Test("130124931319605432", "WXF2F");
		//说明:请在以下函数中正确填写由民生提供的参数
		//payKey    : 标识商户唯一性
		//paySec    ： 对投递数据加密
		//postUrl   ： 投送的地址
		//authCode  此字段从支付宝或微信中获取
		//---------测试函数---------------
		//ali商户主扫
		//ALI_Test();

		//wx商户主扫
		//WX_Test("130196521175151625");

		//wx公众号被扫
//		WxPrePay_Test();

		//native
		WxNativePay_Test();

		//订单查询
//		 queryOrder_Test("cmbc13979965_20161225224522");

		//退款测试
		//refund_Test("cmbc13979965_20160905013649");

		//退款订单查询
		//QueryRefund_Test("refund13979965_20160905014034");

	}
}
