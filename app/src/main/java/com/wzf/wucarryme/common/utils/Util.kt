package com.wzf.wucarryme.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import java.io.Closeable
import java.io.IOException

object Util {
    private val CODE_TYPE_60 = "4353"
    private val CODE_TYPE_002 = "4614"
    private val CODE_TYPE_300 = "4621"

    /**
     * 只关注是否联网
     */
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun safeText(msg: String?): String {
        return if (TextUtils.isEmpty(msg)) "" else msg!!
    }

    /**
     * 获取类型,主板/中小/创/指数
     *
     * @param code 天气代码
     * @return 天气情况
     */
    fun getCodeType(code: String): String {
        return if (code.startsWith("300")) {
            CODE_TYPE_300
        } else if (code.startsWith("002")) {
            CODE_TYPE_002
        } else {
            CODE_TYPE_60
        }
    }

    /**
     * 匹配掉错误信息
     */
    fun replaceCity(city: String): String {
        var city = city
        city = safeText(city).replace("(?:省|市|自治区|特别行政区|地区|盟)".toRegex(), "")
        return city
    }

    /**
     * 匹配掉无关信息
     */

    fun replaceInfo(city: String?): String {
        var city = city
        city = safeText(city).replace("API没有", "")
        return city
    }

    /**
     * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
     */
    fun closeQuietly(closeable: Closeable?) {
        if (null != closeable) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun copyToClipboard(info: String, context: Context) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("msg", info)
        manager.primaryClip = clipData
        ToastUtil.showShort(String.format("[%s] 已经复制到剪切板啦( •̀ .̫ •́ )✧", info))
    }
}