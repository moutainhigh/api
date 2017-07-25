package com.zhsj.api.util.print;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.result.ShiftBean;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.DateUtil;

import sun.misc.BASE64Encoder;

public class PrinterUtil {
     static Logger logger = LoggerFactory.getLogger(PrinterUtil.class);
	
	//查询状态
	//deviceId 设备id  secertKey 秘钥
	public static String queryState(String device_id,String secertKey) throws Exception {

		String result = "";
		String url = CloudPrinter.URL;
		long timestamp = System.currentTimeMillis() / 1000;
		String strTimeTamp = String.valueOf(timestamp);
		// 拼接成请求的url 状态
		String str = info(CloudPrinter.ACTION + "=%s&" +
		CloudPrinter.DEVICE_ID + "=%s&" + CloudPrinter.SECRET + "=%s&" +
		CloudPrinter.TIMESTAMP + "=%s&", CloudPrinter.STATE, device_id,
		secertKey, strTimeTamp);
		String sign = Tool.SHA1(str);
	   	Map<String, String> data = new HashMap<String, String>();
		data.put(CloudPrinter.ACTION, CloudPrinter.STATE);
		data.put(CloudPrinter.DEVICE_ID, device_id);
		data.put(CloudPrinter.SECRET, secertKey);
		data.put(CloudPrinter.TIMESTAMP, strTimeTamp);
		data.put(CloudPrinter.SIGN, sign);
		try {
		    result = requestPost(data, url);
		} catch (Exception e) {
			logger.error("#PrinterUtil.queryState# e",e);
		}
		return result;
	}
	
	//字符串处理
	public static String info(String message, Object... parameter) {
		return String.format(message, parameter);
	}
	//查询状态(发送请求)
	public static String requestPost(Map<String, String> maps, String url) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			
			HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;  
			httpUrlConnection.setRequestMethod("POST");
			// 设置通用的请求属性
			httpUrlConnection.setRequestProperty("accept", "*/*");
			httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setUseCaches(false);  
			
			 httpUrlConnection.connect();  
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(httpUrlConnection.getOutputStream());	
			
			// 设置请求属性
			String param = "";
			if (maps != null && maps.size() > 0) {
				Iterator<String> ite = maps.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();// key
					String value = maps.get(key);
					param += key + "=" + value + "&";
				}
				param = param.substring(0, param.length() - 1);
			}
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("#PrinterUtil.requestPost# e",e);
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
	
	public static String request(String deviceId, String secertKey,OrderBean orderBean, String cashierName){
		return request(deviceId, secertKey, orderBean, cashierName,"","");
	}	
	
	public static String request(String deviceId, String secertKey,OrderBean orderBean, String cashierName,String qr,String desc){
		logger.info("#PrinterUtil.request# deviceId={},secertKey={},orderBean={},cashierName={},qr={},desc={}",
				deviceId,secertKey,orderBean,cashierName,qr,desc);
		try{
			//1、保存byte数组
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			
			//2、title设置格式
			String tilteSetting = CloudPrinter.TITLESE_TTING;
			byte[] titleSettingByte = PrinterUtil.hexStringToBytes(tilteSetting);
			byteBuffer.put(titleSettingByte);
			//3、title设置
			String title = orderBean.getStoreName()+"\r\n\r\n";
			byte[] titleByte;
			titleByte = title.getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(titleByte);
			
			//content 设置格式
			String contentSetting = CloudPrinter.CONTENT_SETTING;
			byte[] contentSettingByte = PrinterUtil.hexStringToBytes(contentSetting);
			byteBuffer.put(contentSettingByte);
//			//content 设置
			StringBuffer contentBuffer = new StringBuffer();
			String split = "-------------------------------\r\n";
			String c_no = "单号:"+orderBean.getOrderId()+"\r\n"+split;
			String payMethod  = "支付方式:"+("1".equals(orderBean.getPayMethod())?"微信":"支付宝")+"\r\n";
			String plan = "应收金额:"+orderBean.getPlanChargeAmount()+"\r\n";
			String discount = "抵扣金额:"+Arith.sub(orderBean.getPlanChargeAmount(), orderBean.getActualChargeAmount())+"\r\n";
			String actual = "实际金额:"+orderBean.getActualChargeAmount()+"\r\n";
			String ctime = "交易时间:"+DateUtil.getTime(orderBean.getCtime()*1000)+"\r\n";
			contentBuffer.append(c_no).append(payMethod)
			.append(plan).append(discount).append(actual)
			.append(split).append(ctime);
			if(StringUtils.isEmpty(cashierName)){
				cashierName = "  --";
			}
			String cashier = "收银员:"+cashierName+"\r\n";
			contentBuffer.append(split).append(cashier);
			byte[] contentByte;
			contentByte = contentBuffer.toString().getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(contentByte);
			
			//
			if(StringUtils.isNotEmpty(qr)){
				// 设置二维码
				byteBuffer.put(titleSettingByte);
				byte[] hexQR = PrinterUtil.getURLQRCode(qr);
				byteBuffer.put(hexQR);
			}
			if(StringUtils.isNotEmpty(desc)){
				//底部内容
				String bottomSetting = CloudPrinter.BOTTOM_TTING;
				byte[] bottomSettingByte = PrinterUtil.hexStringToBytes(bottomSetting);
				byteBuffer.put(bottomSettingByte);
				StringBuffer bottomContent = new StringBuffer();
				bottomContent.append(desc);
				byteBuffer.put(bottomContent.toString().getBytes(CloudPrinter.CHARSET));
			}
			//换行
			byteBuffer.put(contentSettingByte);
			byteBuffer.put("\r\n\r\n\r\n".toString().getBytes(CloudPrinter.CHARSET));
			
			//打印指令
			byte[] goPrint = {0x0d,0x0a};
			byteBuffer.put(goPrint);
			//存放实际打印的byte
			byte[] bs = new byte[byteBuffer.position()];
			byteBuffer.flip();
			byteBuffer.get(bs);
			String result = requestPrintPost(deviceId, secertKey, bs);
			logger.info("#request# result = {}", result);
			return result;
		}catch (Exception e) {
			logger.error("#PrinterUtil.request# deviceId={},secertKey={},orderBean={},cashierName={},qr={},desc={}",
							deviceId,secertKey,orderBean,cashierName,qr,desc,e);
		}
		return null;
	}	

	public static String requestByShift(String deviceId, String secertKey,ShiftBean shiftBean){
		logger.info("#PrinterUtil.requestByShift# deviceid={}",deviceId);
		try{
			//1、保存byte数组
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			//2、title设置格式
			//content 设置格式
			String contentSetting = CloudPrinter.CONTENT_SETTING;
			byte[] contentSettingByte = PrinterUtil.hexStringToBytes(contentSetting);
			byteBuffer.put(contentSettingByte);
			//	//content 设置
			StringBuffer contentBuffer = new StringBuffer();
			contentBuffer.append("收银员: "+shiftBean.getName()+"\r\n");
			contentBuffer.append("营业款总额: "+shiftBean.getActualMoney()+"\r\n");
			contentBuffer.append("订单金额: "+shiftBean.getTotalMoney()+"\r\n");
			contentBuffer.append("支付笔数: "+shiftBean.getTotalNum()+"\r\n");
			contentBuffer.append("退款金额: "+shiftBean.getRefundMoney()+"\r\n");
			contentBuffer.append("退款笔数: "+shiftBean.getRefundNum()+"\r\n");
			contentBuffer.append("平台减免: "+shiftBean.getOrgDisMoney()+"\r\n");
			contentBuffer.append("商家减免: "+shiftBean.getStoreDisMoney()+"\r\n");
			contentBuffer.append("-------------------------------\r\n");
			
			if(shiftBean.getDisplayWX() == 1){
				contentBuffer.append("微信订单金额: "+shiftBean.getPlanMoneyWX()+"\r\n");
				contentBuffer.append("实收金额: "+shiftBean.getActualMoneyWX()+"\r\n");
				contentBuffer.append("平台优惠: "+shiftBean.getOrgDisMoneyWX()+"\r\n");
				contentBuffer.append("商家优惠: "+shiftBean.getStoreDisMoneyWX()+"\r\n");
				contentBuffer.append("退款金额: "+shiftBean.getRefundWX()+"\r\n");
//				contentBuffer.append("-------------------------------\r\n");
				contentBuffer.append("\r\n");
			}
			if(shiftBean.getDisplayAli() == 1){
				contentBuffer.append("支付宝订单金额: "+shiftBean.getPlanMoneyAli()+"\r\n");
				contentBuffer.append( "实收金额: "+shiftBean.getActualMoneyAli()+"\r\n");
				contentBuffer.append("平台优惠: "+shiftBean.getOrgDisMoneyAli()+"\r\n");
				contentBuffer.append("商家优惠: "+shiftBean.getStoreDisMoneyAli()+"\r\n");
				contentBuffer.append("退款金额: "+shiftBean.getRefundAli()+"\r\n");
//				contentBuffer.append("-------------------------------\r\n");
			}
			if(shiftBean.getDisplayUCard() == 1){
				contentBuffer.append("\r\n");
				contentBuffer.append("银联卡订单金额: "+shiftBean.getPlanMoneyUCard()+"\r\n");
				contentBuffer.append("实收金额: "+shiftBean.getActualMoneyUCard()+"\r\n");
				contentBuffer.append("平台优惠: "+shiftBean.getOrgDisMoneyUCard()+"\r\n");
				contentBuffer.append("商家优惠: "+shiftBean.getStoreDisMoneyUCard()+"\r\n");
				contentBuffer.append("退款金额: "+shiftBean.getRefundWX()+"\r\n");
				contentBuffer.append("\r\n");
			}
			contentBuffer.append("-------------------------------\r\n");
			contentBuffer.append("起始时间: "+shiftBean.getStartTime()+"\r\n");
			contentBuffer.append("结束时间: "+shiftBean.getEndTime()+"\r\n");
			contentBuffer.append("\r\n\r\n");
			byte[] contentByte;
			
			contentByte = contentBuffer.toString().getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(contentByte);
			// 设置二维码
			//	byteBuffer.put(titleSettingByte);
			//	String url = "http://www.zhihuishangjie.com/";
			//	byte[] hexQR = PrinterUtil.getURLQRCode(url);
			//	byteBuffer.put(hexQR);
			//打印指令
			byte[] goPrint = {0x0d,0x0a};
			byteBuffer.put(goPrint);
			//存放实际打印的byte
			byte[] bs = new byte[byteBuffer.position()];
			byteBuffer.flip();
			byteBuffer.get(bs);
			String result = requestPrintPost(deviceId, secertKey, bs);
			logger.info("#request# result = {}", result);
			return result;
		}catch (Exception e) {
			logger.error("#PrinterUtil.requestByShift# deviceid={}",deviceId,e);
		}
	    return null;
	}	
	
	
public static String requestPrintPost(String deviceId, String secertKey, byte[] content) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		String url1 = CloudPrinter.URL;
		long timestamp = System.currentTimeMillis() / 1000;
		// 拼接成请求的url
		String str = info(CloudPrinter.ACTION + "=%s&" + CloudPrinter.DEVICE_ID + "=%s&" 
		+ CloudPrinter.SECRET + "=%s&" + CloudPrinter.TIMESTAMP + "=%d&",
		CloudPrinter.SEND, deviceId,secertKey, timestamp);
		try {
			String strEncodeBase64 = new BASE64Encoder().encode(content);
			String sign = Tool.SHA1(str + strEncodeBase64);
			URL realUrl = new URL(url1 + str + CloudPrinter.SIGN + "=" + sign);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;  
			httpUrlConnection.setRequestMethod("POST");
			// 设置通用的请求属性
			httpUrlConnection.setRequestProperty("accept", "*/*");
			httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			httpUrlConnection.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setUseCaches(false);  
			 httpUrlConnection.connect();  
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(httpUrlConnection.getOutputStream());			
			// 发送请求参数
			out.print(strEncodeBase64);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("#PrinterUtil.requestPrintPost# e",e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error("#PrinterUtil.requestPrintPost# ex",ex);
			}
		}
		return result;
	}


        //打印二维码网址 1b6101 居中 1d5a02 二维码类型 QRcode 1b5a004c06 二维码大小
		public static byte[] getURLQRCode(String url) {

			String urlHex = toHexString(url);
			StringBuffer cmdQR = new StringBuffer(CloudPrinter.QRCODE_SETTING);
			cmdQR.append(Integer.toHexString(url.length())).append("00").append(urlHex);
			String cmdQRHex = cmdQR.toString();
			byte[] hexQR = hexStringToBytes(cmdQRHex);
			return hexQR;
		}
	
	
	 // 将字符串转成十六进制
    public static String toHexString(String str){
    	// 根据默认编码获取字节数组
    	byte[] bytes = str.getBytes();
    	StringBuilder sb = new StringBuilder(bytes.length *2);
    	// 将字节数组中每个字节拆解成2位16进制整数
    	for(int i = 0;i < bytes.length; i++){
    		
    		sb.append(CloudPrinter.HEXSTRING.charAt((bytes[i] & 0xf0) >> 4));
    		sb.append(CloudPrinter.HEXSTRING.charAt((bytes[i] & 0x0f) << 0));
    		
    	}
    	return sb.toString();
    }
    
    private static int charToByte(char c){
    	return (byte) "0123456789ABCDEF".indexOf(c);
    }
	
	// 将十六进制字符串转换为字节数组
    public static byte[] hexStringToBytes(String hexString){
    	if(hexString == null || hexString.equals("")){
    		return null;
    	}
    	hexString = hexString.toUpperCase();
    	int length = hexString.length() / 2;
    	char[] hexChars = hexString.toCharArray();
    	byte[] d = new byte[length];
    	for(int i = 0; i < length; i++){
    		int pos = i * 2;
    		d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    	}
    	return d;
    }
}
