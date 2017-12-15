package com.haxi.mh.utils;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.haxi.mh.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间工具类
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 */
public class TimeUtil {

    public static final int DAY = 1;
    public static final int HOUR = 2;
    public static final int MIN = 3;
    public static final int SEC = 4;


    public static String getTimeForPlayer(long current) {
        current /= 1000;
        int currentInt = (int) current;
        int minute = currentInt / 60;
        int hour = currentInt / 60;
        int second = currentInt % 60;
        minute %= 60;
        if (hour == 0) {
            return String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d", minute, second);
        }
        return String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * 设置Button倒数读秒
     *
     * @param btn  button
     * @param time 时间(秒)
     */
    public static CountDownTimer setButtonCoolingDown(final TextView btn, int time, int sendFinishDrawable, final int sendAgDrawable) {
        if (btn.isClickable()) {
            btn.setClickable(false);
            btn.setBackgroundDrawable(UIUtil.getDrawable(sendFinishDrawable));
            return new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int remaining = (int) (millisUntilFinished / 1000);
                    btn.setText(String.format("重新发送(%s)", remaining));
                }

                @Override
                public void onFinish() {
                    btn.setText("已发送");
                    btn.setClickable(true);
                    btn.setBackgroundDrawable(UIUtil.getDrawable(sendAgDrawable));
                }
            }.start();
        }

        return null;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2, int dateType) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (dateType) {
            case 3:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 2:
                sdf = new SimpleDateFormat("yyyy-MM");
                break;
            case 1:
                sdf = new SimpleDateFormat("yyyy");
                break;
        }
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return getTimeFormat(System.currentTimeMillis());
    }

    public static String getTimeFormat(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        return formatter.format(timeMillis);
    }

    public static String getTimeFormatYMD(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        return formatter.format(time);
    }


    /**
     * 获取当前时间 小时
     *
     * @return
     */
    public static int getHourOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     * 获取当前时间 分钟
     *
     * @return
     */
    public static int getMinOfHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 根据时间戳获取月份的某一天
     */
    public static int getDay(long time) {
        return getCalendar(time).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据时间戳获取月份
     *
     * @param time
     * @return
     */
    public static int getMonth(long time) {
        return getCalendar(time).get(Calendar.MONTH);
    }

    /**
     * 根据时间戳获取年
     *
     * @param time
     * @return
     */
    public static int getYear(long time) {
        return getCalendar(time).get(Calendar.YEAR);
    }

    @NonNull
    private static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }


    /**
     * 当前时间是一周中的第几天，外国周日为第一天，换算
     *
     * @return
     */
    public static int getDayInWeek() {
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为本周的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day = day - 1;
        }
        return day;
    }


    /**
     * 当前周是一个月中的第几周，外国周日为第一天，换算
     *
     * @return
     */
    public int getWeekInMouth() {
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为本月的第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            week = week - 1;
        }
        return week;
    }

    /**
     * 获取时间
     *
     * @param times
     * @param day   隔几天
     * @return
     */
    public static String getDateAfter(long times, int day) {
        Date date = new Date(times);
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(now.getTime());
    }

    public static String getChatTime(long time) {
        if (isToday(getTime(time))) {
            return "今天 " + getDayTime(time);
        } else {
            if (isYesterday(getTime(time))) {
                return "昨天" + getDayTime(time);
            } else {
                return getOtherTadayTime(time);
            }

        }
    }

    /**
     * 时间格式化  HH:mm
     *
     * @param time
     * @return
     */
    public static String getDayTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    /**
     * 时间格式化  yyyy年MM月dd日 HH:mm
     *
     * @param time
     * @return
     */
    public static String getOtherTadayTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    /**
     * 时间格式化  yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    /**
     * 获取两个时间差
     *
     * @param time1
     * @param time2
     * @param type
     * @return
     */
    public static long getTimeLong(long time1, long time2, int type) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long back = 0;
        long diff = 0;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        switch (type) {
            case DAY:
                back = day;
                break;
            case HOUR:
                back = hour;
                break;
            case MIN:
                back = min;
                break;
            case SEC:
                back = sec;
                break;
        }
        return back;
    }


    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) {
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);
            Calendar cal = Calendar.getInstance();
            Date date = null;

            date = getDateFormat().parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) {

        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);

            Calendar cal = Calendar.getInstance();
            Date date = null;

            date = getDateFormat().parse(day);

            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == -1) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    //ThreadLocal为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。
    //通过ThreadLocal为每一个线程提供了单独的副本
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();


    /**
     * 聊天界面显示的聊天日期
     */
    public void getData() {
        long time = 123566555l;//当前时间
        long dayTime = (long) 86400000;//1天的时间 *6
        long nowTime = System.currentTimeMillis(); //系统时间
        SimpleDateFormat formatNow = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatToday = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat formatWeek = new SimpleDateFormat("EEEE");//星期几
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    }


}
