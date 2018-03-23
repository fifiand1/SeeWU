package com.wzf.wucarryme.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;

public class TimeUtil {
    private static final String TAG = TimeUtil.class.getSimpleName();

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M月d日");
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("M月d日 HH:mm:ss");
    public static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("M月d日HH:mm");

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYueRi() {
        return simpleDateFormat.format(new Date());
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMDHMSTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    /**
     * MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowMDHMSTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    /**
     * MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMD() {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
        return mDateFormat.format(new Date());
    }

    /**
     * yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYMD(Date date) {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
        return mDateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getMD(Date date) {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd");
        return mDateFormat.format(date);
    }


    public static boolean isKP() {
        SimpleDateFormat format = new SimpleDateFormat("HHmm", Locale.CHINA);
        String startAM = "0915", endAM = "1130", startPM = "1300", endPM = "1500";
        Date dateStartAM, dateEndAM, dateStartPM, dateEndPM;

        try {
            dateStartAM = format.parse(startAM);
            dateEndAM = format.parse(endAM);
            dateStartPM = format.parse(startPM);
            dateEndPM = format.parse(endPM);

            Date date = format.parse(format.format(new Date()));
            boolean isKP = (date.after(dateStartAM) && date.before(dateEndAM))
                || (date.after(dateStartPM) && date.before(dateEndPM));
            LogUtil.d(TAG, format.format(date) + " isKP: " + isKP);
            return isKP;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }
}
