package com.haxi.mh.utils.im;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 聊天和首页消息时间展示工具类
 * Created by Han on 2018/10/20
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class IMTimeUtils {

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<>();

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    /**
     * 返回的时间为String类型, 用于首页消息列表
     *
     * @param time long类型
     * @return String类型
     */
    public static String getMainTime(long time) {
        long dayTime = (long) 86400000;//1天的时间 *6
        long nowTime = System.currentTimeMillis(); //系统时间
        SimpleDateFormat formatNow = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatToday = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat formatWeek = new SimpleDateFormat("EEEE HH:mm");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date(nowTime));
        try {
            if (calendar.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR)) {
                if (calendar.get(Calendar.MONTH) == calendarNow.get(Calendar.MONTH)) {
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int dayNow = calendarNow.get(Calendar.DAY_OF_MONTH);
                    int daydiff = dayNow - day;
                    if (daydiff == 0) {
                        return "今天 " + formatNow.format(time);
                    } else if (daydiff > 0 && daydiff <= 1) {
                        return "昨天 " + formatNow.format(time);
                    } else if (daydiff > 1 && daydiff <= 6) {
                        return formatWeek.format(time);
                    } else {
                        return formatToday.format(time);
                    }
                } else {
                    return formatToday.format(time);
                }
            } else {
                return formatDate.format(time);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 消息通知栏时间提醒 用于首页消息列表
     * 返回String 类型时间
     *
     * @param times 传入String
     * @return String
     */
    public static String getMainTime(String times) {
        try {
            return getMainTime(getTimeResultLong(times));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 返回Long 类型时间
     *
     * @param times 传入String
     * @return String
     */
    public static Long getTimeResultLong(String times) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(times).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }


    public static String getCurrentTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String getDayTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    public static String getOtherTodayTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");//得到当前的时间
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }


    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isChatToday(String day) {
        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);
            Calendar cal = Calendar.getInstance();
            Date date = getDateFormat().parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * 获取首页消息界面 聊天界面的时间展示
     *
     * @param time
     * @return
     */
    public static String getIMChatTime(long time) {
        if (isChatToday(getCurrentTime(time))) {
            return "今天 " + getDayTime(time);
        } else {
            return getOtherTodayTime(time);
        }
    }


}
