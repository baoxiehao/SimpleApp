package com.yekong.droid.simpleapp.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by baoxiehao on 16/11/30.
 */

public class DateUtils {

    public static String dateToString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return String.format("%s-%02d-%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }

    public static String timeToFileName(long timeInMillis, String fileName) {
        final int index = fileName.lastIndexOf(".");
        String fileSuffix = fileName;
        if (index > 0) {
            fileSuffix = fileName.substring(index + 1);
        }
        return String.format("%s.%s", DateUtils.timeToFileName(timeInMillis), fileSuffix);
    }

    private static String timeToFileName(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        return String.format("%s-%02d-%02d_%02d.%02d.%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }

    public static Calendar getCalendar(final int year, final int month, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar;
    }

    public static int getDayOffset(final Calendar now, final Calendar baseDate) {
        int dayOffset = 0;
        int compareTo = now.compareTo(baseDate);
        if (compareTo != 0) {
            while (now.compareTo(baseDate) == compareTo) {
                dayOffset += 1;
                now.add(Calendar.DAY_OF_MONTH, -compareTo);
            }
        }
        Logger.d("getDayOffset(): %s", dayOffset);
        return dayOffset;
    }
}
