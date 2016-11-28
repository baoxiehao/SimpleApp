package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.BaiduApi;
import com.yekong.droid.simpleapp.api.BaiduService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module(includes = {BaiduApiModule.class})
public class BaiduModule {
    @Provides
    @Singleton
    public BaiduService provideBaiduService(BaiduApi baiduApi) {
        return new BaiduService(baiduApi);
    }
}
