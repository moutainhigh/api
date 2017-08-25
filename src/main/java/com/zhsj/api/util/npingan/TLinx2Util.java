package com.zhsj.api.util.npingan;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TLinx2Util {
    /**
     * 签名
     * @param postMap
     * @return
     */
    public static String sign(Map<String, String> postMap) {
        String sortStr = null;
        String sign    = null;
        try {
            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);
            /**
             * 2 sha1加密(小写)
             */
            String sha1 = TLinxSHA1.SHA1(sortStr);
            /**
             * 3 md5加密(小写)
             */
            sign = MD5.MD5Encode(sha1).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
    
    public static String sign(Map<String, String> postMap, String privatekey) {
        String sortStr = null;
        String sign = null;
        try {
            /**
             * 1 A~z排序
             */
            sortStr = TLinxUtil.sort(postMap);
            System.out.println("=======排序后的明文：" + sortStr);

            /**
             * 2 sha1加密(小写)
             */
            String sha1 = TLinxSHA1.SHA1(sortStr);

            System.out.println("=======sha1：" + sha1);

            /**
             * 3 RSA加密(小写),二进制转十六进制
             */
            sign = TLinxRSACoder.sign(sha1.getBytes("utf-8"), privatekey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    
    public static String signRSA(Map<String, String> postMap,String privateKey) {
        String sortStr = null;
        String sign    = null;

        try {

            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);
            sign = TLinxRSACoder.sign(sortStr.getBytes("utf-8"), privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    /**
     * 验签
     * @param respObject
     * @return
     */
    public static Boolean verifySign(JSONObject respObject,String openKey) {
        String respSign = respObject.get("sign").toString();

        respObject.remove("sign");    // 删除sign节点
        respObject.put("open_key", openKey);
        
        Map<String,String> map = JSON.parseObject(respObject.toJSONString(),Map.class);

        String veriSign = sign(map);    // 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名

        if (respSign.equals(veriSign)) {
            System.out.println("=====验签成功=====");

            return true;
        } else {
            System.out.println("=====验签失败=====");
        }

        return false;
    }

    /**
     * 签名
     * @param postmap
     */
    public static void handleSign(TreeMap<String, String> postmap,String openKey) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key", openKey);

        // 签名
        String sign = sign(veriDataMap);

        postmap.put("sign", sign);
    }
    
    /**
     * 签名
     * @param postmap
     */
    public static void handleSignRSA(TreeMap<String, String> postmap,String openKey,String privateKey) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key", openKey);

        // 签名
        String sign = signRSA(veriDataMap,privateKey);

        postmap.put("sign", sign);
    }
    
    /**
     * 签名
     * @param postmap
     */
    public static void handleSha1Sign(TreeMap<String, String> postmap,String privatekey) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        String sign = sign(veriDataMap, privatekey);
        // 签名
        postmap.put("sign", sign);
    }
    

    /**
     * 请求接口
     * @param postmap
     * @return      响应字符串
     */
    public static String handlePost(TreeMap<String, String> postmap, String url) {

        if (url.contains("https")) {
            return HttpsUtil.httpMethodPost(url, postmap, "UTF-8");
        } else {
            return HttpUtil.httpMethodPost(url, postmap, "UTF-8");
        }
    }

    /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {

    }
}
