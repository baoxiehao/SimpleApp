package com.yekong.droid.simpleapp.cache;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.util.Logger;

import java.util.Collection;

import io.paperdb.Paper;

import static io.paperdb.Paper.book;

/**
 * Created by baoxiehao on 17/1/14.
 */

public final class CacheManager {

    private CacheManager() {
    }

    public static void init() {
        Paper.init(SimpleApp.getAppComponent().getContext());
    }

    public static <T> void put(String key, T object) {
        if (!(object instanceof Collection)) {
            Logger.d("put(): key=%s, value=%s", key, object);
        }
        Paper.book().write(key, object);
    }

    public static <T> T get(String key) {
        T value = Paper.book().read(key);
        if (!(value instanceof Collection)) {
            Logger.d("get(): key=%s, value=%s", key, value);
        }
        return value;
    }

    public static boolean contains(String key) {
        boolean contains = book().exist(key);
        Logger.d("contains(): key=%s, value=%s", key, contains);
        return contains;
    }

    public static boolean containsAll(String... keys) {
        for (String key : keys) {
            if (!contains(key)) {
                return false;
            }
        }
        return true;
    }
}
