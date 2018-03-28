package com.wzf.wucarryme.common.utils;

import java.io.Closeable;
import java.io.IOException;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class Util {
    private static final String CODE_TYPE_60 = "4353";
    private static final String CODE_TYPE_002 = "4614";
    private static final String CODE_TYPE_300 = "4621";

    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String safeText(String msg) {
        return TextUtils.isEmpty(msg) ? "" : msg;
    }

    /**
     * 获取类型,主板/中小/创/指数
     *
     * @param code 天气代码
     * @return 天气情况
     */
    public static String getCodeType(String code) {
        if (code.startsWith("300")) {
            return CODE_TYPE_300;
        } else if (code.startsWith("002")) {
            return CODE_TYPE_002;
        } else {
            return CODE_TYPE_60;
        }
    }

    /**
     * 匹配掉错误信息
     */
    public static String replaceCity(String city) {
        city = safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
        return city;
    }

    /**
     * 匹配掉无关信息
     */

    public static String replaceInfo(String city) {
        city = safeText(city).replace("API没有", "");
        return city;
    }

    /**
     * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
     */
    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void copyToClipboard(String info, Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        ToastUtil.showShort(String.format("[%s] 已经复制到剪切板啦( •̀ .̫ •́ )✧", info));
    }
}