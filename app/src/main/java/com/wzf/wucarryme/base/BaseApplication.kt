package com.wzf.wucarryme.base

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.wzf.wucarryme.BuildConfig
import com.wzf.wucarryme.component.CrashHandler
import com.wzf.wucarryme.component.PLog

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate

import im.fir.sdk.FIR
import io.reactivex.plugins.RxJavaPlugins

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        CrashHandler.init(CrashHandler(applicationContext))
        if (!BuildConfig.DEBUG) {
            FIR.init(this)
        } else {
            Stetho.initializeWithDefaults(this)
        }
//        BlockCanary.install(this, AppBlockCanaryContext()).start()
        LeakCanary.install(this)
        RxJavaPlugins.setErrorHandler { throwable ->
            throwable.printStackTrace()
            PLog.e(throwable.toString())
        }
        /*
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        appCacheDir = if (applicationContext.externalCacheDir != null && existSDCard()) {
            applicationContext.externalCacheDir!!.toString()
        } else {
            applicationContext.cacheDir.toString()
        }
    }

    private fun existSDCard(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }

    companion object {

        var appCacheDir: String? = null
            private set
        var appContext: Context? = null
            private set

        // TODO: 16/8/1 这里的夜间模式 UI 有些没有适配好 暂时放弃夜间模式
        init {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
