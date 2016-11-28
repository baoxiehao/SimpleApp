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
        return String.format("%s-%s-%s",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }
}
