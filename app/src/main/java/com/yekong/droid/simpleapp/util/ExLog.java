package com.yekong.droid.simpleapp.util;

import android.util.Log;

/**
 * Created by baoxiehao on 16/11/27.
 */

public class ExLog {

    private static final String TAG = "SimpleApp";

    public static void d(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    public static void e(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }
}
