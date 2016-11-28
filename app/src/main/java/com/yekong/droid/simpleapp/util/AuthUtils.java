package com.yekong.droid.simpleapp.util;

public class AuthUtils {
    private static final String TOKEN = "token";

    public static String getToken() {
        return PrefUtils.getPrefs().getString(TOKEN, "");
    }

    public static void setToken(String token) {
        PrefUtils.getEditor().putString(TOKEN, token).commit();
    }
}
