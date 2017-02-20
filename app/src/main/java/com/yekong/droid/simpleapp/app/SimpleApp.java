package com.yekong.droid.simpleapp.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.squareup.leakcanary.LeakCanary;
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
        initApp();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    private void initApp() {
        sAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        CacheManager.init();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
