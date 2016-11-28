package com.yekong.droid.simpleapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yekong.droid.simpleapp.app.SimpleApp;

public class PrefUtils {
    private static final String PREF_NAME = "simpleapp";

    public static SharedPreferences getPrefs() {
        return SimpleApp.getAppComponent().getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }
}
