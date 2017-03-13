package com.yekong.droid.simpleapp.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by baoxiehao on 16/11/30.
 */

public class DateUtils {

    public static final String PATTERN_IMAGE_FILENAME = ".*\\d{4}-\\d{2}-\\d{2}_\\d{2}.\\d{2}.\\d{2}.*";

    private static final String PATTERN_RSS_DATE_TIME = "(\\d{4}-\\d{2}-\\d{2})[^0-9]+(\\d{2}:\\d{2}).*";

    private static final String FORMAT_RSS_DATE = "%04d-%02d-%02d %02d:%02d";
    private static final String FORMAT_FILE_NAME = "%04d-%02d-%02d_%02d.%02d.%02d";

    public static String dateToString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dateString = String.format(FORMAT_RSS_DATE,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        dateString = dateString.replace("00:00", "");
        return dateString;
    }

    public static String timeToFileName(long timeInMillis, String fileName) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        final String timeFileName = String.format(FORMAT_FILE_NAME,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        final int index = fileName.lastIndexOf(".");
        String fileSuffix = fileName;
        if (index > 0) {
            fileSuffix = fileName.substring(index + 1);
        }
        return String.format("%s.%s", timeFileName, fileSuffix);
    }

    public static String parseRssDateTime(String string) {
        if (string.contains("昨天")) {
            string = string.replace("昨天", dateToString(getCalendarByDayOffset(-1).getTime()));
        }
        if (string.contains("小时前")) {
            string = dateToString(getCalendarByHourOffset(
                    -Integer.valueOf(RegexUtils.matchJoin(string, "(\\d+)[^0-9]+"))).getTime());
        }
        if (string.contains("分钟前")) {
            string = dateToString(getCalendarByMinuteOffset(
                    -Integer.valueOf(RegexUtils.matchJoin(string, "(\\d+)[^0-9]+"))).getTime());
        }
        return RegexUtils.matchJoin(string, PATTERN_RSS_DATE_TIME, " ");
    }

    private static Calendar getCalendarByDayOffset(final int dayOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        return calendar;
    }

    private static Calendar getCalendarByHourOffset(final int hourOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hourOffset);
        return calendar;
    }

    private static Calendar getCalendarByMinuteOffset(final int minuteOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minuteOffset);
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
