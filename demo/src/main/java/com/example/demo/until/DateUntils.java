package com.example.demo.until;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUntils {

    /**
     * 将传入的 Date 对象按照指定格式转换为 String 类型
     * @param date 要转换的 Date 对象
     * @param format 指定的日期格式，例如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date, String format) {
        if (date == null || format == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
