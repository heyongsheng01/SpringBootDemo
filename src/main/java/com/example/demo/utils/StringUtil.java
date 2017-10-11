
package com.example.demo.utils;


import java.text.NumberFormat;
import java.util.Locale;
/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断Object类型 为空
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        if (object instanceof String) {
            return isEmpty(object.toString());
        }
        return (object == null);
    }

    /**
     * 判断String类型 为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return ((value == null) || (value.trim().length() == 0) || ("null"
                .endsWith(value)));
    }

    /**
     * Object类型为空时转换为 ""
     *
     * @param obj
     * @return
     */
    public static String null2String(Object obj) {
        return ((obj == null) ? "" : obj.toString());
    }

    /**
     * String类型为空时转换为 ""
     *
     * @param str
     * @return
     */
    public static String null2String(String str) {
        return ((str == null) ? "" : str);
    }

    /**
     *  格式化钱
     * @param currency
     * @return
     */
    public static String formatCurrecy(String currency) {
        if (isEmpty(currency)) {
            return "";
        }

        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        try {
            return usFormat.format(Double.parseDouble(currency));
        } catch (Exception e) {
        }
        return "";
    }

    public static String iso2Gb(String gbString) {
        if (gbString == null)
            return null;
        String outString = "";
        try {
            byte[] temp = null;
            temp = gbString.getBytes("ISO8859-1");
            outString = new String(temp, "GB2312");
        } catch (Exception e) {
        }
        return outString;
    }

    public static String iso2Utf(String isoString) {
        if (isoString == null)
            return null;
        String outString = "";
        try {
            byte[] temp = null;
            temp = isoString.getBytes("ISO8859-1");
            outString = new String(temp, "UTF-8");
        } catch (Exception e) {
        }
        return outString;
    }

    public static String str2Gb(String inString) {
        if (inString == null)
            return null;
        String outString = "";
        try {
            byte[] temp = null;
            temp = inString.getBytes();
            outString = new String(temp, "GB2312");
        } catch (Exception e) {
        }
        return outString;
    }

}
