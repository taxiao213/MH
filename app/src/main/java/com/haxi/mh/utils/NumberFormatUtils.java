package com.haxi.mh.utils;

import java.text.DecimalFormat;

/**
 * 格式化数字
 * Created by Han on 2017/11/30
 * Email:yin13753884368@163.com
 */

public class NumberFormatUtils {

    /**
     * 指定小数点后面留两位的类
     *
     * @param value
     * @return
     */
    public static String format(String value) {
        DecimalFormat df = new DecimalFormat(",##0.00");
        try {
            float v = Float.parseFloat(value);
            if (v >= 100000000) {
                float v1 = v / 100000000;
                return (df.format(v1) + "亿");
            } else if (v >= 10000000) {
                float v1 = v / 10000000;
                return (df.format(v1) + "千万");
            } else if (v >= 1000000) {
                float v1 = v / 1000000;
                return (df.format(v1) + "百万");
            } else if (v >= 10000) {
                float v1 = v / 10000;
                return (df.format(v1) + "万");
            } else {
                return (df.format(v) + "元");
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 指定小数点后面留两位的类 万元
     *
     * @param value
     * @return
     */
    public static String formatWY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.00");
        try {
            double v1 = value / 10000;
            return (df.format(v1));
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 指定小数点后面留两位的类 千万元
     * DecimalFormat mFormat = new DecimalFormat("###,###,##0.00");
     *
     * @param value
     * @return
     */
    public static String formatQY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.00");
        try {
            double v1 = value / 10000000;
            return (df.format(v1));
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 指定小数点后面留四位的类 千万元
     * DecimalFormat mFormat = new DecimalFormat("###,###,##0.0000");
     *
     * @param value
     * @return
     */
    public static String formatKSQY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.0000");
        try {
            double v1 = value / 10000000;
            return (df.format(v1) + "千万");
        } catch (Exception e) {
            return "0.0000" + "千万";
        }
    }

    /**
     * 指定小数点后面留四位的类 百万元
     * DecimalFormat mFormat = new DecimalFormat("###,###,##0.0000");
     *
     * @param value
     * @return
     */
    public static String formatKSBY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.0000");
        try {
            double v1 = value / 1000000;
            return (df.format(v1) + "百万");
        } catch (Exception e) {
            return "0.0000" + "百万";
        }
    }

    /**
     * 指定小数点后面留四位的类 万元
     * DecimalFormat mFormat = new DecimalFormat("###,###,##0.0000");
     *
     * @param value
     * @return
     */
    public static String formatKSWY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.0000");
        try {
            double v1 = value / 10000;
            return (df.format(v1) + "万");
        } catch (Exception e) {
            return "0.0000" + "万";
        }
    }

    /**
     * 元
     * DecimalFormat mFormat = new DecimalFormat("###,###,##0");
     *
     * @param value
     * @return
     */
    public static String formatKSY(double value) {
        DecimalFormat df = new DecimalFormat(",##0");
        try {
            return (df.format(value) + "元");
        } catch (Exception e) {
            return "0" + "元";
        }
    }

    /**
     * 指定小数点后面留两位的类万元转千万元
     *
     * @param value
     * @return
     */
    public static String formatWZQY(double value) {
        DecimalFormat df = new DecimalFormat(",##0.00");
        try {
            double v1 = value / 1000;
            return (df.format(v1));
        } catch (Exception e) {
            return "0.00";
        }
    }
}
