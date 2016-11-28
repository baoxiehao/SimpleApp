package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.GankApi;
import com.yekong.droid.simpleapp.di.Gank;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by baoxiehao on 16/11/28.
 */
@Module(includes = {RetrofitModule.class})
public class GankApiModule {
    @Provides
    @Singleton
    public GankApi provideGankApi(@Gank Retrofit retrofit) {
        return retrofit.create(GankApi.class);
    }
}
