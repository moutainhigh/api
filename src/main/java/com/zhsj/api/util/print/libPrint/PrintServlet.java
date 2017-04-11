package com.zhsj.api.util.print.libPrint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.servlet.ServletContext;

import sun.misc.BASE64Encoder;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;



import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;

 
/**
 * Servlet implementation class MyServlet
 */
public class PrintServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// 处理Post方式提交的数据表单
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置Http响应的文档类型
		response.setContentType("text/html"); 
		// 设置响应采用的编码方式
		response.setCharacterEncoding("UTF-8"); 
		// 获取提交的表单中name文本框值
		String device_id = request.getParameter("id"); 
		System.out.println("打印机id = " + device_id);
		// 将字符编码转换为utf-8
		device_id = new String(device_id.getBytes("ISO-8859-1"), "utf-8"); 
		// 得到一个PrintWrite对象
		PrintWriter out = response.getWriter(); 

		String state = null;
		
		if(request.getParameter("printstate")!=null){
					
			try {
				
				
				
			} catch (Exception e) {

				System.out.println("云查询出错 请检查参数!");

			}
		   	
		}else if(request.getParameter("printdata")!=null){
					
			try {
				
				System.out.println("打印数据开始！");
				
				printParkingTicket(device_id,"REGO","2016-11-24",10);
			
			} catch (Exception e) {

				System.out.println("云查询出错 请检查参数!");
			}
		}
		out.flush(); // 强制性将当前缓存中的内容输出
		out.close(); // 关闭输出流，清除当前所有内容
	}

	public static void main(String[] args) {
//		System.out.println("查询状态开始！");
		String device_id = "1083";
//		try {
//			System.out.println("云打印!" + QueryState(device_id));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		System.out.println("打印数据开始！");
		
		try {
			printParkingTicket(device_id,"REGO","2016-11-24",10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String QueryState(String device_id) throws Exception {

		String result = "";
		String url = CloudPrinter.URL;
		long timestamp = System.currentTimeMillis() / 1000;
		String strTimeTamp = String.valueOf(timestamp);
		// 拼接成请求的url 状态
		 String str = info(CloudPrinter.ACTION + "=%s&" +
		 CloudPrinter.DEVICE_ID + "=%s&" + CloudPrinter.SECRET + "=%s&" +
		 CloudPrinter.TIMESTAMP + "=%s", CloudPrinter.STATE, device_id,
		 CloudPrinter.SECRETKEY, strTimeTamp);
		 System.out.println("str = " + str);
		 
		String sign = Tool.SHA1(str);
		
		 System.out.println("sign = " + sign);
		String newURL = url + str + CloudPrinter.SIGN + "=" + sign;
	   	
		 System.out.println("newURL = " + newURL);
		 
	   	Map<String, String> data = new HashMap<String, String>();

		data.put(CloudPrinter.ACTION, CloudPrinter.STATE);
		data.put(CloudPrinter.DEVICE_ID, device_id);
		data.put(CloudPrinter.SECRET, CloudPrinter.SECRETKEY);
		data.put(CloudPrinter.TIMESTAMP, strTimeTamp);
		data.put(CloudPrinter.SIGN, sign);
		
		try {
			
		    result = requestPost(data, url);
						
			System.out.println("result = " + result);
			
		} catch (Exception e) {
			System.err.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public static String requestPost(Map<String, String> maps, String url) throws Exception {

		System.out.println("requestOnePost url" + url);
		
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
			System.out.println("param" + param);
			
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
			System.err.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
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
				ex.printStackTrace();
			}
		}
		return result;
	}
		
	public static String requestPrintPost(String deviceId, byte[] content) throws Exception {

		
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		
		String url1 = CloudPrinter.URL;
		long timestamp = System.currentTimeMillis() / 1000;
		// 拼接成请求的url
		String str = info(CloudPrinter.ACTION + "=%s&" + CloudPrinter.DEVICE_ID + "=%s&" + CloudPrinter.SECRET + "=%s&" + CloudPrinter.TIMESTAMP + "=%d&", CloudPrinter.SEND, deviceId, CloudPrinter.SECRETKEY, timestamp);
		
		System.out.println("拼接成请求的url = " + str);
		
		try {
			
			String strEncodeBase64 = new BASE64Encoder().encode(content);
			
			System.out.println("strEncodeBase64 = " + strEncodeBase64);
			
			String sign = Tool.SHA1(str + strEncodeBase64);

			
			URL realUrl = new URL(url1 + str + CloudPrinter.SIGN + "=" + sign);
			
			System.out.println("realUrl = " + realUrl);
			
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
			System.err.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
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
				ex.printStackTrace();
			}
		}
		return result;
	}
	
		public static void printParkingTicket(String printerId, String merchantUser, String endDate, int faceValue) throws Exception {
			String title = CloudPrinter.PARKING_TICKET;
			String merchantName = "商户名称:" + merchantUser + "\r\n";
			String validDate = "有效期至:" + endDate + "\r\n";
			// 字符串 微信扫一扫可保存本券
			String tip = CloudPrinter.SWEEP;
			// url网址
			String url = CloudPrinter.PARKING_TICKET_URL;
			// 描述信息 注
			String description = CloudPrinter.DESCRIPTIONDESCRIPTION;

			StringBuffer content = new StringBuffer();
			content.append(merchantName).append(validDate);
			
			System.out.println("content= " + content);
			
			// 保存 byte数组
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			// 初始化打印机
			String initial = CloudPrinter.PRINTER_INIT;
			byte[] initByte = hexStringToBytes(initial);
			String printInitial = requestPrintPost(printerId, initByte);
			System.out.println("print01" + printInitial);
			
			// title设置格式
			String titleSetting = CloudPrinter.TITLESE_TTING;
			byte[] titleSettingByte = hexStringToBytes(titleSetting);
			byteBuffer.put(titleSettingByte);
			try {
				byte[] titleByte = title.getBytes(CloudPrinter.CHARSET);
				byteBuffer.put(titleByte);
				// 设置 商户名称 有效日期
				String contentSetting = CloudPrinter.CONTENT_SETTING;
				byte[] contentSettingByte = hexStringToBytes(contentSetting);
				byteBuffer.put(contentSettingByte);
				byte[] contentByte = content.toString().getBytes(CloudPrinter.CHARSET);
				byteBuffer.put(contentByte);
				// 设置券的价格
//				String faceValueStr = faceValue / 100.0 + "元\r\n";
//				byte[] faceValueByte = faceValueStr.getBytes(CloudPrinter.CHARSET);
//				byteBuffer.put(titleSettingByte);
//				byteBuffer.put(faceValueByte);
				// 设置二维码
				byte[] hexQR = getURLQRCode(url);
				byteBuffer.put(hexQR);
				// 设置微信扫一扫可保存本券
//				String tipSetting = CloudPrinter.COMPANY_TNAME_SETTING;
//				byte[] tipSettingByte = hexStringToBytes(tipSetting);
//				byte[] contentHex = tip.getBytes(CloudPrinter.CHARSET);
//				byteBuffer.put(tipSettingByte);
//				byteBuffer.put(contentHex);
//				// 设置备注注
//				byte[] content2Hex = description.getBytes(CloudPrinter.CHARSET);
//				byteBuffer.put(contentSettingByte);
//				byteBuffer.put(content2Hex);
				
				// 打印指令
				byte[] goPrint = {0x0d,0x0a};
				byteBuffer.put(goPrint);
				
				//存放实际打印的byte
				byte[] bs = new byte[byteBuffer.position()];
				byteBuffer.flip();
				byteBuffer.get(bs);
				
			 String print01 = requestPrintPost(printerId, bs);
			System.out.println("print01 = " + print01);
			} catch (UnsupportedEncodingException e) {
				
			}

		}
		// 打印二维码网址 1b6101 居中 1d5a02 二维码类型 QRcode 1b5a004c06 二维码大小
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
	    
	       // 本Servlet装配到容器后自动执行初始化方法
	 		public void init() throws ServletException {
	 			System.out.println("Init()执行了");
	 		}

	 		public static String info(String message, Object... parameter) {

	 			return String.format(message, parameter);
	 		}
}
