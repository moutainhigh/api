package com.zhsj.api.util.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.aspectj.weaver.ast.Var;

import com.zhsj.api.util.print.libPrint.CloudPrinter;

public class Demo {
	/*
	 * 1Byte = 	8 bits
	 * 1kb = 1024Byte
	 * 1MB = 1024Kb
	 */
    static String url = "http://weixin.qq.com/r/lUyOlhXEYEgfrVH19xmH";
	public static void main(String[] args) throws Exception{
		String device_id = "1083";
		String secertKey = "rego-yn-20150826";
//		System.err.println(PrinterUtil.queryState(device_id, secertKey));
		
		byte[] content = getContent();
		int len = 0;
		for(Byte b:content){
			System.err.println(b);
			len ++;
		}
		System.err.println("------------------------------");
		System.err.println(len);
//		System.err.println(len*8/1024);
//		String result  =PrinterUtil.requestPrintPost(device_id, 
//				secertKey, content );
//		System.err.println(result);
		
//		String initial = CloudPrinter.PRINTER_INIT;
//		byte[] initByte = PrinterUtil.hexStringToBytes(initial);
//		String printInitial = PrinterUtil.requestPrintPost(device_id,secertKey, initByte);
//		System.out.println("print01" + printInitial);
//		getC(device_id,secertKey,"测试","2017-04-11",1000);
	}
	
	
	
	public static byte[] getContent() throws Exception{
		//1、保存byte数组
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		//2、title设置格式
		String tilteSetting = CloudPrinter.TITLESE_TTING;
		byte[] titleSettingByte = PrinterUtil.hexStringToBytes(tilteSetting);
		byteBuffer.put(titleSettingByte);
		//3、title设置
		String title = "智慧商街小票testDemo\r\n\r\n";
		byte[] titleByte = title.getBytes(CloudPrinter.CHARSET);
		byteBuffer.put(titleByte);
		//content 设置格式
		String contentSetting = CloudPrinter.CONTENT_SETTING;
		byte[] contentSettingByte = PrinterUtil.hexStringToBytes(contentSetting);
		byteBuffer.put(contentSettingByte);
		//content 设置
		StringBuffer contentBuffer = new StringBuffer();
		String c_no = "编号:OPENTM401668887\r\n";
		String c_title = "标题:收银小票提醒\r\n";
		String c_industry = "行业:IT科技-互联网|电子商务\r\n";
		String c_example = "内容示例:您好,这是您的消费电子小票\r\n";
		String c_store_name = "商店名称:小小花便利店\r\n";
		String c_store_no = "单号:12845662645\r\n";
		String c_pay_type = "支付方式:现金\r\n";
		String c_sum = "合计:88元\r\n";
		String c_date = "时间:2017年4月11日  12:12:56\r\n";
		String c_thinks = "感谢您的惠顾!";
		contentBuffer.append(c_no).append(c_title)
		.append(c_industry).append(c_example).append(c_store_name)
		.append(c_store_no).append(c_pay_type).append(c_sum).append(c_date).append(c_thinks);
		byte[] contentByte = contentBuffer.toString().getBytes(CloudPrinter.CHARSET);
		byteBuffer.put(contentByte);
//		二维码
		byte[] hexQR = PrinterUtil.getURLQRCode(url);
		byteBuffer.put(hexQR);
//		// 设置微信扫一扫可保存本券
//		String tipSetting = CloudPrinter.COMPANY_TNAME_SETTING;
//		byte[] tipSettingByte = PrinterUtil.hexStringToBytes(tipSetting);
//		// 字符串 微信扫一扫可保存本券
//		String tip = CloudPrinter.SWEEP;
//		byte[] contentHex = tip.getBytes(CloudPrinter.CHARSET);
//		byteBuffer.put(tipSettingByte);
//		byteBuffer.put(contentHex);
		//打印指令
		byte[] goPrint = {0x0d,0x0a};
		byteBuffer.put(goPrint);
		//切刀指令
//		byte[] qdSetting = PrinterUtil.hexStringToBytes("1d560065");
//		byteBuffer.put(qdSetting);
		//存放实际打印的byte
		byte[] bs = new byte[byteBuffer.position()];
		byteBuffer.flip();
		byteBuffer.get(bs);
		return bs;
	}
	
	
	public static byte[] File2byte(String filePath)  
    {  
        byte[] buffer = null;  
        try  
        {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }catch (FileNotFoundException e){  
            e.printStackTrace();  
        }catch (IOException e){  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
	
	
	
	public static void getC(String printerId,String secretKey, String merchantUser, String endDate, int faceValue) throws Exception{
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
		byte[] initByte = PrinterUtil.hexStringToBytes(initial);
		String printInitial = PrinterUtil.requestPrintPost(printerId,secretKey, initByte);
		System.out.println("print01" + printInitial);
		
		// title设置格式
		String titleSetting = CloudPrinter.TITLESE_TTING;
		byte[] titleSettingByte = PrinterUtil.hexStringToBytes(titleSetting);
		byteBuffer.put(titleSettingByte);
		try {
			byte[] titleByte = title.getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(titleByte);
			// 设置 商户名称 有效日期
			String contentSetting = CloudPrinter.CONTENT_SETTING;
			byte[] contentSettingByte = PrinterUtil.hexStringToBytes(contentSetting);
			byteBuffer.put(contentSettingByte);
			byte[] contentByte = content.toString().getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(contentByte);
			// 设置券的价格
			String faceValueStr = faceValue / 100.0 + "元\r\n";
			byte[] faceValueByte = faceValueStr.getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(titleSettingByte);
			byteBuffer.put(faceValueByte);
			// 设置二维码
			byte[] hexQR = PrinterUtil.getURLQRCode(url);
			byteBuffer.put(hexQR);
			// 设置微信扫一扫可保存本券
			String tipSetting = CloudPrinter.COMPANY_TNAME_SETTING;
			byte[] tipSettingByte = PrinterUtil.hexStringToBytes(tipSetting);
			byte[] contentHex = tip.getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(tipSettingByte);
			byteBuffer.put(contentHex);
			// 设置备注注
			byte[] content2Hex = description.getBytes(CloudPrinter.CHARSET);
			byteBuffer.put(contentSettingByte);
			byteBuffer.put(content2Hex);
			
			// 打印指令
			byte[] goPrint = {0x0d,0x0a};
			byteBuffer.put(goPrint);
			
			//存放实际打印的byte
			byte[] bs = new byte[byteBuffer.position()];
			byteBuffer.flip();
			byteBuffer.get(bs);
			String print01 = PrinterUtil.requestPrintPost(printerId,secretKey, bs);
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}
