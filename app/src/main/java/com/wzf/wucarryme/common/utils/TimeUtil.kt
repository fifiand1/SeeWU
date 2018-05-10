package com.wzf.wucarryme.common.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    private val TAG = TimeUtil::class.java.simpleName

    var simpleDateFormat = SimpleDateFormat("M月d日", Locale.SIMPLIFIED_CHINESE)
    var simpleDateFormat2 = SimpleDateFormat("M月d日 HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
    var simpleDateFormat3 = SimpleDateFormat("M月d日HH:mm", Locale.SIMPLIFIED_CHINESE)

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    val nowYueRi: String
        @SuppressLint("SimpleDateFormat")
        get() = simpleDateFormat.format(Date())

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    val nowYMDHMSTime: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val mDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss")
            return mDateFormat.format(Date())
        }

    /**
     * MM-dd HH:mm:ss
     */
    val nowMDHMSTime: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val mDateFormat = SimpleDateFormat(
                    "MM-dd HH:mm:ss")
            return mDateFormat.format(Date())
        }

    /**
     * MM-dd
     */
    val nowYMD: String
        @SuppressLint("SimpleDateFormat")
        get() {

            val mDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd")
            return mDateFormat.format(Date())
        }


    val isKP: Boolean
        get() {
            val calendar = Calendar.getInstance(Locale.CHINA)
            calendar.time = Date()
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
                return false
            }
            val format = SimpleDateFormat("HHmm", Locale.CHINA)
            val startAM = "0910"
            val endAM = "1135"
            val startPM = "1255"
            val endPM = "1505"
            val dateStartAM: Date
            val dateEndAM: Date
            val dateStartPM: Date
            val dateEndPM: Date

            try {
                dateStartAM = format.parse(startAM)
                dateEndAM = format.parse(endAM)
                dateStartPM = format.parse(startPM)
                dateEndPM = format.parse(endPM)

                val date = format.parse(format.format(Date()))
                val isKP = date.after(dateStartAM) && date.before(dateEndAM) || date.after(dateStartPM) && date.before(dateEndPM)
//                val methodName = Thread.currentThread().stackTrace[3].methodName//java
                val methodName = Thread.currentThread().stackTrace[3].className//java

                LogUtil.d(TAG, "[" + methodName + "] " + format.format(date) + " isKP: " + isKP)
                return isKP
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return false
        }

    /**
     * yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    fun getYMD(date: Date): String {

        val mDateFormat = SimpleDateFormat(
                "yyyy-MM-dd")
        return mDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getMD(date: Date): String {

        val mDateFormat = SimpleDateFormat(
                "MM-dd")
        return mDateFormat.format(date)
    }

    fun isSP(isNooning: Boolean): Boolean {
        val format = SimpleDateFormat("HHmm", Locale.CHINA)
        val startAM = "0915"
        val endAM = "1130"
        val startPM = "1300"
        val endPM = "1500"
        val dateStartAM: Date
        val dateEndAM: Date
        val dateStartPM: Date
        val dateEndPM: Date

        try {
            dateStartAM = format.parse(startAM)
            dateEndAM = format.parse(endAM)
            dateStartPM = format.parse(startPM)
            dateEndPM = format.parse(endPM)

            val date = format.parse(format.format(Date()))
            val isSP: Boolean
            if (isNooning) {
                isSP = date.after(dateEndAM) && date.before(dateStartPM)
            } else {
                isSP = date.after(dateEndPM)
            }
            LogUtil.d(TAG, format.format(date) + " isSP " + isNooning + ": " + isSP)

            return isSP
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    @Throws(Exception::class)
    fun dayForWeek(pTime: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(pTime)
        val dayForWeek: Int
        var week = ""
        dayForWeek = c.get(Calendar.DAY_OF_WEEK)
        when (dayForWeek) {
            1 -> week = "星期日"
            2 -> week = "星期一"
            3 -> week = "星期二"
            4 -> week = "星期三"
            5 -> week = "星期四"
            6 -> week = "星期五"
            7 -> week = "星期六"
        }
        return week
    }
}
