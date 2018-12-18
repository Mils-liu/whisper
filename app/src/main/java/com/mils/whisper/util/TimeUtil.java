package com.mils.whisper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class TimeUtil {

    private static String TAG = "TimeUtil";

    //避免重复点击
    public static boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    //获取当前的年
    public static String getCurrentYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year + "";
    }

    //获取当前的月
    public static String getCurrentMonth() {

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        if (IntUtil.sizeOfInt(month) == 1) {
            return "0" + month;
        } else {
            return month + "";
        }

    }

    //获取当前的日
    public static String getCurrentDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (IntUtil.sizeOfInt(day) == 1) {
            return "0" + day;
        } else {
            return day + "";
        }
    }

    //获取当前的时
    public static String getCurrentHour() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        if (IntUtil.sizeOfInt(hour) == 1) {
            return "0" + hour;
        } else {
            return hour + "";
        }
    }

    //获取当前的分
    public static String getCurrentMin() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        if (IntUtil.sizeOfInt(minute) == 1) {
            return "0" + minute;
        } else {
            return minute + "";
        }
    }

    //获取当前的秒
    public static String getCurrentSecond() {
        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
        if (IntUtil.sizeOfInt(second) == 1) {
            return "0" + second;
        } else {
            return second + "";
        }
    }

    /**
     * 获取系统当前日期时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        return date;
    }

    public static String getPeriodTime(String dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case "0": // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case "1": // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case "2": // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case "3": // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case "4": // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
// System.out.println(df.format(c.getTime()));
                break;
            case "5": // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
            case "6": // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
            case "7": // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
// System.out.println(df.format(c.getTime()));
                break;
            default:
                break;
        }
        String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(c.getTime());
        return str;
    }


    /**
     * 获取阶段日期
     *
     * @param dateType
     * @author Yangtse
     */
    //使用方法 char datetype = '7';
    public static String getPeriodDate(String dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour = 0; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case "0": // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case "1": // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case "2": // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case "3": // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case "4": // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case "5": // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case "6": // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case "7": // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            default:
                break;
        }
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder strForwardDate = new StringBuilder().append(mYear).append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(
                (mDay < 10) ? "0" + mDay : mDay);

        String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(c.getTime());

        return str;
        //return c.getTimeInMillis();
    }


    /**
     * xx年xx月xx日xx时xx分转成yyyy-MM-dd HH:mm:ss
     *
     * @param yearStr
     * @param dateStr
     * @param hourStr
     * @param minuteStr
     * @return
     */
    public static String strTimeToDateFormat(String yearStr, String dateStr, String hourStr, String minuteStr, String secondStr) {

        return yearStr.replace("年", "-") + dateStr.replace("月", "-").replace("日", " ")
                + hourStr + ":" + minuteStr + ":" + secondStr;
    }

    public static String strTimeToDateFormat(String yearStr, String dateStr) {

        return yearStr.replace("年", "-") + dateStr.replace("月", "-").replace("日", "");
    }/*

    *//**
     * 比较时间，参数是日期，返回值是布尔类型
     *
     * @param date
     * @param date2
     * @param tag   date是否可以等于date2.
     * @return true:
     *//*
    public static boolean compareWithTime(Date date, Date date2, boolean tag) {
        MyLog.log(TAG, "date1:" + date.toString());
        MyLog.log(TAG, "date2:" + date2.toString());


        if (date != null && date2 != null) {
            if (tag) {
                if (date.getTime() <= date2.getTime()) {
                    MyLog.log(TAG, "result:true");
                    return true;
                } else {
                    MyLog.log(TAG, "result:false");
                    return false;
                }
            } else {
                if (date.getTime() < date2.getTime()) {
                    MyLog.log(TAG, "result:true");
                    return true;
                } else {
                    MyLog.log(TAG, "result:false");
                    return false;
                }
            }
        } else {
            MyLog.log(TAG, "result:false");
            return false;
        }

    }*/

    /**
     * 返回结果：
     * time1>time2   time1在time2之后，返回1
     * =返回0，
     * time1<time2   返回-1
     *
     * @param time1 格式： 2017-09-09
     * @param time2
     * @return
     */
    public static int compareCalendar(String time1, String time2) {
        //time1
        String year1 = time1.split("-")[0];
        String month1 = time1.split("-")[1];
        String day1 = time1.split("-")[2].split(" ")[0];

        Date date1 = new Date(Integer.parseInt(year1), Integer.parseInt(month1), Integer.parseInt(day1));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //time2
        String year2 = time2.split("-")[0];
        String month2 = time2.split("-")[1];
        String day2 = time2.split("-")[2].split(" ")[0];

        Date date2 = new Date(Integer.parseInt(year2), Integer.parseInt(month2), Integer.parseInt(day2));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int result = calendar1.compareTo(calendar2);
        return result;
    }


    /**
     * 比较日期
     */
    public static int compareTime(String time1, String time2) {
        //time1
        String year1 = time1.split("-")[0];
        String month1 = time1.split("-")[1];
        String day1 = time1.split("-")[2].split(" ")[0];
        String hour1 = time1.split(" ")[1].split(":")[0];
        String minute1 = time1.split(" ")[1].split(":")[1];
        String second1 = time1.split(" ")[1].split(":")[2];

        Date date1 = new Date(Integer.parseInt(year1), Integer.parseInt(month1), Integer.parseInt(day1), Integer.parseInt(hour1), Integer.parseInt(minute1), Integer.parseInt(second1));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //time2
        String year2 = time2.split("-")[0];
        String month2 = time2.split("-")[1];
        String day2 = time2.split("-")[2].split(" ")[0];
        String hour2 = time2.split(" ")[1].split(":")[0];
        String minute2 = time2.split(" ")[1].split(":")[1];
        String second2 = time2.split(" ")[1].split(":")[2];

        Date date2 = new Date(Integer.parseInt(year2), Integer.parseInt(month2), Integer.parseInt(day2), Integer.parseInt(hour2), Integer.parseInt(minute2), Integer.parseInt(second2));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int result = calendar1.compareTo(calendar2);
        return result;
    }


    /**
     * 2019-09-08 10:09:01
     *
     * @param str
     * @return
     */

    public static Date stringToDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        //string-date
        if (str == null) {
            return null;
        } else {
            try {

                date = df.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
    }

    public static String dateToString(Date date) {
        //date-string
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            return "";
        } else {
            String fmtStr = df.format(date);
            return fmtStr;
        }

    }
}
