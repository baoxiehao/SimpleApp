package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.ZhiHuApi;
import com.yekong.droid.simpleapp.api.ZhiHuService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mi on 16-11-29.
 */
@Module(includes = {ZhiHuApiModule.class})
public class ZhiHuModule {
    @Provides
    @Singleton
    public ZhiHuService provideZhiHuService(ZhiHuApi api) {
        return new ZhiHuService(api);
    }
}
