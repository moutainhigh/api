package com.zhsj.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by lcg on 16/12/29.
 */
public class Test {

    public static void main(String[] args){
        String str = "{\"access_token\":\"EdTWv9QQkZ-Nmnkvlt9W9oM8tJriMQvk_f7j_tsiXpTnQO9-Mm-IMnLrAUDDU4gyLbx-04BgPg24sTu24bXKg-I-sNDuPmrBoQ0f3vg-xl8\",\"expires_in\":7200,\"refresh_token\":\"e_tWcYvlwBAmFhh_pblOGjLNzyz8iTXZIrfgrsraTI-d8zh9l6R-ZaEMutjGFa-jBnFGXvoI7H0s8Y85UpnrasaU_vWhagHre4A_QELMlD8\",\"openid\":\"ofbvZvycdbsQSIAkXzwBJ_vm1Ogg\",\"scope\":\"snsapi_base\"} ";

        Map<String,String> map = JSON.parseObject(str, Map.class);
        System.out.println(map.get("openid"));

    }
}