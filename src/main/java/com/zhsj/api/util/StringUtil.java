package com.zhsj.api.util;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by lcg on 16/6/15.
 */
public class StringUtil {
    private StringUtil() {
    }

    public static String list2SqlString(List<?> list) {
        if(CollectionUtils.isEmpty(list)) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            int i = 0;

            for(int n = list.size(); i < n; ++i) {
                Object obj = list.get(i);
                if(obj instanceof Integer) {
                    out.append(obj + ",");
                } else if(obj instanceof Long) {
                    out.append(obj + ",");
                } else {
                    out.append("\'" + obj.toString() + "\',");
                }
            }

            return out.substring(0, out.length() - 1);
        }
    }

}
