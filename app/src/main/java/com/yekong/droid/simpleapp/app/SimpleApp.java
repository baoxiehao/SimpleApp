package com.yekong.droid.simpleapp.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.yekong.droid.simpleapp.cache.CacheManager;
import com.yekong.droid.simpleapp.di.AppComponent;
import com.yekong.droid.simpleapp.di.DaggerAppComponent;
import com.yekong.droid.simpleapp.di.module.ContextModule;

/**
 * Created by baoxiehao on 16/11/26.
 */

public class SimpleApp extends Application {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        CacheManager.init();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }
}
