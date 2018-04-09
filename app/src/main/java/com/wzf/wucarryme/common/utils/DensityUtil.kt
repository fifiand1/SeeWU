package com.wzf.wucarryme.common.utils

import android.content.res.Resources
import android.support.annotation.DimenRes
import com.wzf.wucarryme.base.BaseApplication

object DensityUtil {

    private val sRes = Resources.getSystem()
    private val sDensityDpi = sRes.displayMetrics.densityDpi
    private val sScaledDensity = sRes.displayMetrics.scaledDensity

    fun dp2px(value: Float): Int {
        val scale = sDensityDpi.toFloat()
        return (value * (scale / 160) + 0.5f).toInt()
    }

    fun px2dp(value: Float): Int {
        val scale = sDensityDpi.toFloat()
        return (value * 160 / scale + 0.5f).toInt()
    }

    fun sp2px(value: Float): Int {
        val spValue = value * sScaledDensity
        return (spValue + 0.5f).toInt()
    }

    fun px2sp(value: Float): Int {
        val scale = sScaledDensity
        return (value / scale + 0.5f).toInt()
    }

    fun dimenPixelSize(@DimenRes id: Int): Int {
        return BaseApplication.appContext!!.getResources().getDimensionPixelSize(id)
    }
}