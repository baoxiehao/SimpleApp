package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.ZhiHuApi;
import com.yekong.droid.simpleapp.di.ZhiHu;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module(includes = {RetrofitModule.class})
public class ZhiHuApiModule {
    @Provides
    @Singleton
    public ZhiHuApi provideZhiHuApi(@ZhiHu Retrofit retrofit) {
        return retrofit.create(ZhiHuApi.class);
    }
}
