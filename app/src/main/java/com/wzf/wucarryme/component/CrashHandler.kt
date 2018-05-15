package com.wzf.wucarryme.component

import android.content.Context
import java.io.PrintWriter
import java.io.StringWriter

class CrashHandler(context: Context) : Thread.UncaughtExceptionHandler {

    private var mContext: Context? = null

    private val TAG = CrashHandler::class.java!!.getSimpleName()

    init {
        this.mContext = context
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        println(ex.toString())
        PLog.e(TAG, ex.toString())
        PLog.e(TAG, collectCrashDeviceInfo())
        PLog.e(TAG, getCrashInfo(ex))

        // TODO: 2018/3/22 崩溃后数据处理
        // T崩溃后自动初始化数据
        //        SharedPreferenceUtil.getInstance().setCityName("北京");
        //        OrmLite.getInstance().deleteDatabase();
        // 调用系统错误机制
        mDefaultHandler!!.uncaughtException(thread, ex)
    }

    /**
     * 得到程序崩溃的详细信息
     */
    private fun getCrashInfo(ex: Throwable): String {
        val result = StringWriter()
        val printWriter = PrintWriter(result)
        ex.stackTrace = ex.stackTrace
        ex.printStackTrace(printWriter)
        return result.toString()
    }

    /**
     * 收集程序崩溃的设备信息
     */
    private fun collectCrashDeviceInfo(): String {

        val model = android.os.Build.MODEL
        val androidVersion = android.os.Build.VERSION.RELEASE
        val manufacturer = android.os.Build.MANUFACTURER

        return "$model  $androidVersion  $manufacturer"
    }

    companion object {

        private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

        /**
         * 初始化,设置该CrashHandler为程序的默认处理器
         */
        fun init(crashHandler: CrashHandler) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        }
    }
}
