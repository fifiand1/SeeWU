package com.wzf.wucarryme.common.utils

import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import com.wzf.wucarryme.base.BaseApplication

class SharedPreferenceUtil private constructor() {

    private val mPrefs: SharedPreferences

    // 设置当前小时
    var currentHour: Int
        get() = mPrefs.getInt(HOUR, 0)
        set(h) = mPrefs.edit().putInt(HOUR, h).apply()

    // 图标种类相关
    var iconType: Int
        get() = mPrefs.getInt(CHANGE_ICONS, 0)
        set(type) = mPrefs.edit().putInt(CHANGE_ICONS, type).apply()

    // 自动更新时间 hours
    var autoUpdate: Int
        get() = mPrefs.getInt(AUTO_UPDATE, 3)
        set(t) = mPrefs.edit().putInt(AUTO_UPDATE, t).apply()

    // 博客刷新时间 minute
    var blogAutoRefresh: Int
        get() = mPrefs.getInt(AUTO_REFRESH_BLOG, 1)
        set(t) = mPrefs.edit().putInt(AUTO_REFRESH_BLOG, t).apply()

    // 自选刷新时间 second
    var stockAutoRefresh: Int
        get() = mPrefs.getInt(AUTO_REFRESH_STOCK, 30)
        set(t) = mPrefs.edit().putInt(AUTO_REFRESH_STOCK, t).apply()

    //当前城市
    var cityName: String
        get() = mPrefs.getString(CITY_NAME, "北京")
        set(name) = mPrefs.edit().putString(CITY_NAME, name).apply()

//    //自定义预警
//    var alertStockName: String
//        get(name) = mPrefs.getString(ALERT_STOCK_NAME+name, null)
//        set(price) = mPrefs.edit().putString(ALERT_STOCK_NAME, price).apply()

    //  通知栏模式 默认为常驻
    var notificationModel: Int
        get() = mPrefs.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT)
        set(t) = mPrefs.edit().putInt(NOTIFICATION_MODEL, t).apply()

    // 首页 Item 动画效果 默认关闭

    var mainAnim: Boolean
        get() = mPrefs.getBoolean(ANIM_START, false)
        set(b) = mPrefs.edit().putBoolean(ANIM_START, b).apply()

    val watcherSwitch: Boolean
        get() = mPrefs.getBoolean(WATCHER, false)

    private object SPHolder {
        internal val sInstance = SharedPreferenceUtil()
    }

    init {
        mPrefs = BaseApplication.appContext!!.getSharedPreferences("setting", Context.MODE_PRIVATE)
    }

    fun putInt(key: String, value: Int): SharedPreferenceUtil {
        mPrefs.edit().putInt(key, value).apply()
        return this
    }

    fun getInt(key: String, defValue: Int): Int {
        return mPrefs.getInt(key, defValue)
    }

    fun putString(key: String, value: String): SharedPreferenceUtil {
        mPrefs.edit().putString(key, value).apply()
        return this
    }

    fun getString(key: String, defValue: String): String {
        return mPrefs.getString(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean): SharedPreferenceUtil {
        mPrefs.edit().putBoolean(key, value).apply()
        return this
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mPrefs.getBoolean(key, defValue)
    }

    fun setWatcherSwitcher(b: Boolean) {
        mPrefs.edit().putBoolean(WATCHER, b).apply()
    }

    companion object {

        val CITY_NAME = "city_name" //选择城市
        val ALERT_STOCK_NAME = "alert_stock_name" //选择城市
        val HOUR = "current_hour" //当前小时

        val CHANGE_ICONS = "change_icons" //切换图标
        val CLEAR_CACHE = "clear_cache" //清空缓存
        val AUTO_UPDATE = "change_update_time" //自动更新时长
        val AUTO_REFRESH_BLOG = "change_refresh_time" //博客刷新间隔
        val AUTO_REFRESH_STOCK = "self-select_stock_refresh_time" //自选刷新间隔
        val NOTIFICATION_MODEL = "notification_model"
        val ANIM_START = "animation_start"
        val WATCHER = "watcher"

        private val defaultCodes = "1A0001,2A01,399006"
        private val defaultCodeTypes = "4352,4608,4608"

        var ONE_HOUR = 1000 * 60 * 60

        val instance: SharedPreferenceUtil
            get() = SPHolder.sInstance
    }
}
