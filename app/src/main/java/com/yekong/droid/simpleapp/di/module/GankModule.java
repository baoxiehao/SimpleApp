package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.GankApi;
import com.yekong.droid.simpleapp.api.GankService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baoxiehao on 16/11/28.
 */
@Module(includes = {GankApiModule.class})
public class GankModule {
    @Provides
    @Singleton
    public GankService providerGankService(GankApi gankApi) {
        return new GankService(gankApi);
    }
}
