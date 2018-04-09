package com.wzf.wucarryme.component

import android.util.Log
import com.wzf.wucarryme.BuildConfig
import com.wzf.wucarryme.base.BaseApplication
import com.wzf.wucarryme.common.utils.TimeUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object PLog {
    private val isDebug = BuildConfig.DEBUG
    private val PATH = BaseApplication.appCacheDir
    private val PLOG_FILE_NAME = "log.txt"

    /**
     * 是否写入日志文件
     */
    val PLOG_WRITE_TO_FILE = true

    /**
     * @return 当前的类名(simpleName)
     */
    private// 剔除匿名内部类名
    val className: String
        get() {

            var result: String
            val thisMethodStack = Thread.currentThread().stackTrace[2]
            result = thisMethodStack.className
            val lastIndex = result.lastIndexOf(".")
            result = result.substring(lastIndex + 1)

            val i = result.indexOf("$")

            return if (i == -1) result else result.substring(0, i)
        }

    /**
     * 错误信息
     */
    fun e(TAG: String, msg: String) {
        Log.e(TAG, log(msg))
        if (PLOG_WRITE_TO_FILE) {
            writeLogtoFile("e", TAG, msg)
        }
    }

    /**
     * 警告信息
     */
    fun w(TAG: String, msg: String) {
        if (isDebug) {
            Log.w(TAG, log(msg))
            if (PLOG_WRITE_TO_FILE) {
                writeLogtoFile("w", TAG, msg)
            }
        }
    }

    /**
     * 调试信息
     */

    fun d(TAG: String, msg: String) {
        if (isDebug) {

            Log.d(TAG, log(msg))
            if (PLOG_WRITE_TO_FILE) {
                writeLogtoFile("d", TAG, msg)
            }
        }
    }

    /**
     * 提示信息
     */
    fun i(TAG: String, msg: String) {
        if (isDebug) {
            Log.i(TAG, log(msg))
            if (PLOG_WRITE_TO_FILE) {
                writeLogtoFile("i", TAG, msg)
            }
        }
    }

    fun e(msg: String) {
        e(className, msg)
    }

    fun w(msg: String) {
        w(className, msg)
    }

    fun d(msg: String) {
        d(className, msg)
    }

    fun i(msg: String) {
        i(className, msg)
    }

    /**
     * 写入日志到文件中
     */
    private fun writeLogtoFile(mylogtype: String, tag: String, msg: String) {
        isExist(PATH)
        //isDel();
        val needWriteMessage = ("\r\n"
                + TimeUtil.nowMDHMSTime
                + "\r\n"
                + mylogtype
                + "    "
                + tag
                + "\r\n"
                + msg)
        val file = File(PATH, PLOG_FILE_NAME)
        try {
            val filerWriter = FileWriter(file, true)
            val bufWriter = BufferedWriter(filerWriter)
            bufWriter.write(needWriteMessage)
            bufWriter.newLine()
            bufWriter.close()
            filerWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 删除日志文件
     */
    fun delFile() {

        val file = File(PATH, PLOG_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * 判断文件夹是否存在,如果不存在则创建文件夹
     */
    fun isExist(path: String?) {
        val file = File(path!!)
        if (!file.exists()) {
            try {
                file.mkdirs()
            } catch (e: Exception) {
                PLog.e(e.message!!)
            }

        }
    }

    /**
     * 打印 Log 行数位置
     */
    private fun log(message: String): String {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[5]
        var className = targetElement.className
        className = className.substring(className.lastIndexOf('.') + 1) + ".java"
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) lineNumber = 0
        return "($className:$lineNumber) $message"
    }
}
