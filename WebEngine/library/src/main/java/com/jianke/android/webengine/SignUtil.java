package com.jianke.android.webengine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by season on 16/7/15.
 */
public class SignUtil {

    @NonNull
    public static String genSign(Map<String, String> params, String keyName, String keyValue){

        String result = "";

        Map<String, String> signMap = new HashMap<>(params);
        signMap.put(keyName, keyValue);

        String signString = constructSignString(signMap);

        if (!StringUtil.isEmpty(signString)){
            result = MD5.getMessageDigest(signString.getBytes());
        }

        return result;
    }

    @NonNull
    private static String constructSignString(Map<String, String> map){
        TreeSet<String> names = new TreeSet<>();

        for (String name : map.keySet()){
            names.add(name);
        }

        StringBuffer buffer = new StringBuffer();

        for (String name: names) {
            buffer.append(name).append("=").append(map.get(name)).append("&");
        }

        if (buffer.length() > 0){
            // 删除最后一个符号 "&"
            buffer.deleteCharAt(buffer.length() - 1);
        }

        return buffer.toString();
    }
}
