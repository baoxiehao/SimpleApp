package com.yekong.droid.simpleapp.util;

import android.widget.Toast;

import com.yekong.droid.simpleapp.app.SimpleApp;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class Toaster {
    public static void quick(String message) {
        Toast.makeText(SimpleApp.getAppComponent().getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void slow(String message) {
        Toast.makeText(SimpleApp.getAppComponent().getContext(), message, Toast.LENGTH_LONG).show();
    }
}
