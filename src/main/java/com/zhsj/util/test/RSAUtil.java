package com.zhsj.util.test;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
	
	public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrMEUyoDVSIsKq+p6n/Hiq1/4ncbSRUnrHHUdIgtlZldvD/WHfgLLiNP6+iFA23Pcy"
			+"sPqkqk06pYSQQQ3IDfByBmbZsWJoW0A15G0V4UT9SIYEypLTMpWIZGGaVgcdH7xIXQyYM3n1zisleGmQ3xhz+UEKFePYTzd6QSnLrQ7X+wIDAQAB";
	
	public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKswRTKgNVIiwqr6nqf8eKrX/idxtJFSescdR0iC2VmV28P9Yd+AsuI0"
			+"/r6IUDbc9zKw+qSqTTqlhJBBDcgN8HIGZtmxYmhbQDXkbRXhRP1IhgTKktMylYhkYZpWBx0fvEhdDJgzefXOKyV4aZDfGHP5QQoV49hPN3pBKcutD"
			+"tf7AgMBAAECgYEAkuFkzg5GEcY/bwKANLRcOIIA0IkwG+w/6i4F5oJWyRXarSvv2RwM4CgGVyaQZLJs6zKMq9JvVx2DQ15926OSJGZgcbLVuEvaIQ"
			+"0RHhgSHuEA9TI/UF0cB/nBTEhmHB/9d9LdfcIHHULMA2KBxrQ5aQMVf55596I/H5XMk3WPqpECQQD7pdgwy6FxCtZpckz+/G6bua9y3YY6QdB1SCO"
			+"yGiN92C7s7JZbIPIacpCo88OaHVNMeaHIaemZ1J5kgfFJZthTAkEAriYyfyOq0DX/Q4llTGVFZ3LLpr+8sv9bhnlKvr0u/4JM2qV/RKo3/6vu5oAZ"
			+"Vcnwku06RoWQYG4nm/iNWnDsuQJBANq8r4zKk8v0xabmqbLCrUpDPxQzJiehaZdoW07Gla8fqLpcc91GZ+tBRij1qkJd1zgUSeIw3dLPE9b9dER/V"
			+"u0CQHECGIKogo/aems5HaMVlJQsyjrK8Yi2bI4252ofGgB0bar0+kF0tCIVUFIDjm09PgAaMVbSFX59/PYZtKCb5wECQGpJpw+/rnIIILZyeqpX7L"
			+"EgFbtri88XLn0I1K4UrUsVGv3bB1Hx0Cj2kZT3CQI3uKHLUXNnwYMN3XHv9soepuY=";
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	
	public static String encodeBase64String(byte[] bytes) {
        return org.apache.commons.codec.binary
                .Base64
                .encodeBase64String(bytes)
                .replace("\r", "")
                .replace("\n", "");
    }

    public static byte[] decodeBase64(String str) {
        return org.apache.commons.codec.binary
                .Base64
                .decodeBase64(str);
    }

    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLength = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		// 对数据分段加密
		while(inputLength-offSet>0){
			if(inputLength-offSet>MAX_ENCRYPT_BLOCK){
				cache=cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			}else{
				cache=cipher.doFinal(data, offSet, inputLength-offSet);
			}
			out.write(cache, 0, cache.length);
			offSet+=MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		int inputLength = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		// 对数据分段加密
		while(inputLength-offSet>0){
			if(inputLength-offSet>MAX_ENCRYPT_BLOCK){
				cache=cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			}else{
				cache=cipher.doFinal(data, offSet, inputLength-offSet);
			}
			out.write(cache, 0, cache.length);
			offSet+=MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int inputLength = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		// 对数据分段加密
		while(inputLength-offSet>0){
			if(inputLength-offSet>MAX_DECRYPT_BLOCK){
				cache=cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
			}else{
				cache=cipher.doFinal(data, offSet, inputLength-offSet);
			}
			out.write(cache, 0, cache.length);
			offSet+=MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		int inputLength = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		// 对数据分段加密
		while(inputLength-offSet>0){
			if(inputLength-offSet>MAX_DECRYPT_BLOCK){
				cache=cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
			}else{
				cache=cipher.doFinal(data, offSet, inputLength-offSet);
			}
			out.write(cache, 0, cache.length);
			offSet+=MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	public static String sign(byte[] data, String key) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(data);
		return encodeBase64String(signature.sign());
	}

	public static boolean verify(byte[] data, String key, String sign) throws Exception
	{
		byte[] keyBytes = decodeBase64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(decodeBase64(sign));
	}

//	public static void main(String[] args){
//		try{
//			String inputStr = "{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}";
//			byte[] inputData = inputStr.getBytes("UTF-8");
//			byte[] encryptedData = encryptByPublicKey(inputData, publicKey);
//			byte[] decryptedData = decryptByPrivateKey(encryptedData, privateKey);
//			String outputStr = new String(decryptedData,"UTF-8");
//			System.out.println(outputStr);
//
//
//			String inputStr2 = "{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}"+
//					"{\"data\":{\"bindPhone\":\"186****5920\",}{\"id\":6163,\"is3rdShipping\":0,\"businessDesc\":\",\"self_service\":3,\"poiName\":\"美团体验店望京店测试\"},"
//					+"{\"id\":260021,\"is3rdShipping\":0,\"businessDesc\":\"\",\"poiName\":\"商家端QA测试专用店－线上\"}";
//			byte[] inputData2 = inputStr2.getBytes("UTF-8");
//			byte[] encryptedData2 = encryptByPrivateKey(inputData2, privateKey);
//			byte[] decryptedData2 = decryptByPublicKey(encryptedData2, publicKey);
//			String outputStr2 = new String(decryptedData2, "UTF-8");
//			System.out.println(outputStr2);
//
//			String testStr = "瞅啥瞅！";
//			byte[] inputData3 = testStr.getBytes("UTF-8");
//			String sig = sign(inputData3, privateKey);
//			System.out.println(verify(inputData3, publicKey, sig));
//
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
}
