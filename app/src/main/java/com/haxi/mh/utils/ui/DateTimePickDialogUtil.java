package com.haxi.mh.utils.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.facebook.stetho.common.LogUtil;
import com.haxi.mh.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.functions.Consumer;


/**
 * 日期时间选择控件
 * Created by Han on 2017/12/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DateTimePickDialogUtil implements OnDateChangedListener, OnTimeChangedListener {
    private DatePicker datePicker;
    public TimePicker timePicker;
    private AlertDialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    public static Calendar calendar;


    public DateTimePickDialogUtil(Activity activity) {
        this.activity = activity;
    }

    public void init(DatePicker datePicker, TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年"
                    + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    public interface DialogClickedListener {
        void onDialogClickedListener(int num);
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param inputDate :为需要设置的日期时间文本编辑框
     * @return
     */
    public AlertDialog dateTimePicKDialog(final TextView inputDate, final Context context, final Consumer<String> action1) {
        RelativeLayout dateTimeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.date_time_picker_two, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        init(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);

        ad = new AlertDialog.Builder(activity)
                //                .setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            action1.accept(dateTime);
                            if (inputDate != null) {
                                inputDate.setText(dateTime);
                                inputDate.setTextColor(context.getResources().getColor(R.color.grey_33));
                                if (context instanceof DialogClickedListener) {
                                    DialogClickedListener dialogClickedListener = (DialogClickedListener) context;
                                    dialogClickedListener.onDialogClickedListener(3);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //						inputDate.setText("");
                    }
                }).show();

        DatePicker dp = findDatePicker((ViewGroup) ad.getWindow().getDecorView());
        //		if (dp != null) {
        //			((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);//找到年的控件隐藏掉
        //
        //		}
        onDateChanged(null, 0, 0, 0);
        return ad;
    }


    public AlertDialog dateTimeDialog(final TextView inputDate, final Context context, final Consumer<String> action1) {
        RelativeLayout dateTimeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.date_time_picker_two, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        datePicker.setVisibility(View.GONE);
        timePicker.setVisibility(View.VISIBLE);
        initTime(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calend = Calendar.getInstance();
                calend.set(datePicker.getYear(), datePicker.getMonth(),
                        datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
                SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat mysdf = new SimpleDateFormat("yyyy-MM-dd");
                dateTime = timesdf.format(calend.getTime());
                LogUtil.e("---->>>dateTime=" + dateTime);
            }
        });

        ad = new AlertDialog.Builder(activity)
                //                .setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            action1.accept(dateTime);
                            if (inputDate != null) {
                                inputDate.setText(dateTime);
                                inputDate.setTextColor(context.getResources().getColor(R.color.grey_33));
                                if (context instanceof DialogClickedListener) {
                                    DialogClickedListener dialogClickedListener = (DialogClickedListener) context;
                                    dialogClickedListener.onDialogClickedListener(3);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //						inputDate.setText("");
                    }
                }).show();
        onTimeChanged(null, 0, 0, 0);
        return ad;
    }

    public AlertDialog dateDialog(final TextView inputDate, final Context context, final Consumer<String> action1) {
        LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.date_time_picker_one, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        DatePickerUtils.resizePikcer(datePicker);
        DatePickerUtils.resizePikcer(timePicker);
        initTime(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calend = Calendar.getInstance();
                calend.set(datePicker.getYear(), datePicker.getMonth(),
                        datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateTime = sdf.format(calend.getTime());
                LogUtil.e("---->>>dateTime=" + dateTime);
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calend = Calendar.getInstance();
                        calend.set(datePicker.getYear(), datePicker.getMonth(),
                                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dateTime = sdf.format(calend.getTime());
                        LogUtil.e("---->>>dateTime=" + dateTime);
                    }
                });


        ad = new AlertDialog.Builder(activity)
                //                .setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            action1.accept(dateTime);
                            if (inputDate != null) {
                                inputDate.setText(dateTime);
                                inputDate.setTextColor(context.getResources().getColor(R.color.grey_33));
                                if (context instanceof DialogClickedListener) {
                                    DialogClickedListener dialogClickedListener = (DialogClickedListener) context;
                                    dialogClickedListener.onDialogClickedListener(3);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //						inputDate.setText("");
                    }
                }).show();
        onChanged(null, 0, 0, 0);
        return ad;
    }

    private void onChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
        Calendar calend = Calendar.getInstance();
        calend.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat mysdf = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = sdf.format(calend.getTime());
        ad.setTitle(sdf.format(calend.getTime()));
        LogUtil.e("---->>>dateTime=onChanged" + dateTime);
    }

    private void initTime(DatePicker datePicker, TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年"
                    + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    private void onTimeChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
        Calendar calend = Calendar.getInstance();
        calend.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat mysdf = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = timesdf.format(calend.getTime());
        ad.setTitle(timesdf.format(calend.getTime()));
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

        SimpleDateFormat mysdf = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = mysdf.format(calendar.getTime());
        ad.setTitle(sdf.format(calendar.getTime()));
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期
        String time = spliteString(initDateTime, "日", "index", "back"); // 时间

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = spliteString(time, ":", "index", "front"); // 时
        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue();
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();
        int currentHour = Integer.valueOf(hourStr.trim()).intValue();
        int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay, currentHour,
                currentMinute);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }


    /**
     * 从当前Dialog中查找DatePicker子控件
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

}
