package com.zhsj.api.util.fuyou;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhsj.api.util.MtConfig;


public class Utils {
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static Map<String, String> paraFilter(Map<String, String> map) {
        Map<String, String> result = new HashMap<>();
        if (map == null || map.size() <= 0) {
            return result;
        }
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (key.equalsIgnoreCase("sign") || (key.length() >= 8 && key.substring(0, 8).equalsIgnoreCase("reserved"))) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static String createLinkString(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            if (i == keys.size() - 1) {
                //拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String getSign(Map<String, String> map) throws InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Map<String, String> mapNew = paraFilter(map);
        String preSignStr = createLinkString(mapNew);
        logger.info("==============================待签名字符串==============================\r\n" + preSignStr);
        String privateKey = MtConfig.getProperty("FUYOU_PRIVATE_KEY", "");
        String sign = Sign.sign(preSignStr, privateKey);
        sign = sign.replace("\r\n", "");

        logger.info("==============================签名字符串==============================\r\n" + sign);

        return sign;
    }

    public static Boolean verifySign(Map<String, String> map, String sign) throws Exception {
        Map<String, String> mapNew = paraFilter(map);
        String preSignStr = createLinkString(mapNew);
        return Sign.verify(preSignStr.getBytes(), MtConfig.getProperty("FUYOU_PUBLIC_KEY", ""), sign);
    }

}
