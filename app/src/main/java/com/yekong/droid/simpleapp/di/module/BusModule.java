package com.yekong.droid.simpleapp.di.module;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module
public class BusModule {
    @Provides
    @Singleton
    public EventBus provideBus() {
        return EventBus.getDefault();
    }
}
