package com.github.sofaid.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robik on 12/29/16.
 */

public class StringUtil {
    public static String getOrDefault(String str, String defaultStr) {
        if (str == null || str.trim().isEmpty()) {
            return defaultStr;
        }
        return str;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }

    public static String integerListToCsv(List<Integer> integerList) {
        String s = "";
        for (int i = 0; i < integerList.size(); i++) {
            s += integerList.get(i);
            if (i < integerList.size() - 1) {
                s += ",";
            }
        }
        return s;
    }

    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
