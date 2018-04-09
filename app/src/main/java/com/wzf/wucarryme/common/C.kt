package com.wzf.wucarryme.common

import com.wzf.wucarryme.base.BaseApplication
import java.io.File

object C {

    val API_TOKEN = "BuildConfig.FirToken"
    val KEY = "BuildConfig.WeatherKey"// 和风天气 key

    val MULTI_CHECK = "multi_check"

    val ORM_NAME = "wu.db"

    val UNKNOWN_CITY = "unknown city"

    val NET_CACHE = BaseApplication.appCacheDir + File.separator + "NetCache"
}
