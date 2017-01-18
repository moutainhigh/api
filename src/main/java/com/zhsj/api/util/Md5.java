package com.zhsj.api.util;

import java.security.MessageDigest;

/**
 * 
 * 项目名称：zhsjWeb   
 *
 * 类描述：md5加密
 * 类名称：com.zhsj.util.Md5     
 * 创建人：xulinchuang
 * 创建时间：2016年12月8日 上午9:37:36
 */
public class Md5 {

	/**
	 * 
	 * @Title: encrypt
	 * @Description: 加密
	 * @param initValue
	 * @return
	 */
	public static String encrypt(String initValue){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = initValue.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
	
	
	
    // 测试主函数  
    public static void main(String args[]) {  
        String s = "123456";  
        System.out.println("原始：" + s);  
        System.out.println("MD5后：" + encrypt(s));  
        
        
//        String a = xor("xulinchuang");
//		System.err.println(a);
//		System.err.println(xor(a));
  
    }  
}
