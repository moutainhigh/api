package com.zhsj.api.util.print.libPrint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;  
import sun.misc.*; 

public class Tool {

	public static String md5(String data) throws NoSuchAlgorithmException {
		
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(data.getBytes());

        StringBuffer buf = new StringBuffer();

        byte[] bits = md.digest();

        for(int i=0;i<bits.length;i++){

            int a = bits[i];

            if(a<0) a+=256;

            if(a<16) buf.append("0");

            buf.append(Integer.toHexString(a));

        }

        return buf.toString();

    }
public static String sha1(String data) {

//        MessageDigest md = MessageDigest.getInstance("SHA1");

//        md.update(data.getBytes());

        StringBuffer buf = new StringBuffer();

        byte[] bits = data.getBytes();
        for(int i=0;i<bits.length;i++){
            int a = bits[i];
            if(a<0) a+=256;
            if(a<16) buf.append("0");

            buf.append(Integer.toHexString(a));

        }

        return buf.toString();

    }

/**
* @param decript 要加密的字符串
* @return 加密的字符串
* SHA1加密
*/
public final static String SHA1(String decript) {
try {
    MessageDigest digest = java.security.MessageDigest
            .getInstance("SHA-1");
    digest.update(decript.getBytes());
    byte messageDigest[] = digest.digest();
    // Create Hex String
    StringBuffer hexString = new StringBuffer();
    // 字节数组转换为 十六进制 数
    for (int i = 0; i < messageDigest.length; i++) {
        String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
        if (shaHex.length() < 2) {
            hexString.append(0);
        }
        hexString.append(shaHex);
    }
    return hexString.toString();

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
return "";
}

// Base64加密  
public static String getBase64(String str) {  
byte[] b = null;  
String s = null;  
try {  
    b = str.getBytes("utf-8");  
} catch (UnsupportedEncodingException e) {  
    e.printStackTrace();  
}  
if (b != null) {  
    s = new BASE64Encoder().encode(b);  
}  
return s;  
}  

// Base64解密
public static String getFromBase64(String s){

byte[] b = null;
String result = null;
if(s != null){
	BASE64Decoder decoder = new BASE64Decoder();
	
	try{
		b = decoder.decodeBuffer(s);
		result = new String(b,"utf-8");
		
	}catch(Exception e){
		e.printStackTrace();
	}
}
return result;
}


	
}
