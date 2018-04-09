package com.wzf.wucarryme.common.utils

import android.os.Build

object SdkUtil {

    fun sdkVersionGe(version: Int): Boolean {
        return Build.VERSION.SDK_INT >= version
    }

    fun sdkVersionEq(version: Int): Boolean {
        return Build.VERSION.SDK_INT == version
    }

    fun sdkVersionLt(version: Int): Boolean {
        return Build.VERSION.SDK_INT < version
    }

    fun sdkVersionGe19(): Boolean {
        return sdkVersionGe(19)
    }

    fun sdkVersionGe21(): Boolean {
        return sdkVersionGe(21)
    }
}