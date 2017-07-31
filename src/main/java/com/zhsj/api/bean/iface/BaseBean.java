package com.zhsj.api.bean.iface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import com.zhsj.api.util.wft.MD5;

public abstract class BaseBean {
	
	public abstract Map<String,Object> toMap();
	
	 public String sign(){
		Map<String,Object> map = this.toMap();
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result = result.substring(0, result.length()-1);
        System.out.println(result);
        result = MD5.MD5Encode(result);
        return result;
	 }
	 
	 public abstract boolean checkNull();
	 
}
