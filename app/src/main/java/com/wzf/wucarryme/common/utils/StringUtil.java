package com.wzf.wucarryme.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.annotation.NonNull;

/**
 * @author wzf
 * @date 2018/3/22
 */

public class StringUtil {

    public static String banKuai(String str) {

        str = str.replaceAll("类", "");
        str = str.replaceAll("概念", "");
        str = str.replaceAll("房地产", "地产");
        str = str.replaceAll("次新", "新");
        int index = str.indexOf("股");
        if (index > -1) {
            str = str.substring(0, index);
        }
        return str;
    }

    public static String getBlogTime(String p) {
        String blogTime = "";
        int endIndex = p.contains(" ") ? p.indexOf(" ") : p.indexOf(160);
        blogTime = p.substring(0, endIndex);
        return blogTime;
    }

    public static List<String> commaStr2List(String commaStr) {
        return Arrays.asList(commaStr.split(","));
    }

    public static String list2commaStr(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i)).append(",");
        }
        if (result.toString().endsWith(",")) {
            result = new StringBuilder(result.substring(0, result.length() - 1));
        }
        return result.toString();
    }

    @NonNull
    public static String formatCNName(String cn_name) {
        cn_name = cn_name.trim();
        if (cn_name.length() > 4) {
            cn_name = cn_name.substring(0, 4);
            if (cn_name.endsWith("等")) {
                cn_name = cn_name.substring(0, 3);
            }
        }
        return cn_name;
    }

    public static String genLike(String colName, String bankuai) {
        List<String> list = new ArrayList<>();
        for(int i=0;(i+2)<=bankuai.length();i+=2) {
            list.add(bankuai.substring(i, i + 2));
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String like = list.get(i);
            result.append(" ").append(colName).append(" like '%").append(like).append("%'");
            if (i < list.size() - 1) {
                result.append(" or");
            }
        }
        return result.toString();
    }
}
