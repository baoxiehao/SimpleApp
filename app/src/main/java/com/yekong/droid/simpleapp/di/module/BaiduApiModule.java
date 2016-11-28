package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.BaiduApi;
import com.yekong.droid.simpleapp.di.BaiduImage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module(includes = {RetrofitModule.class})
public class BaiduApiModule {
    @Provides
    @Singleton
    public BaiduApi provideBaiduApi(@BaiduImage  Retrofit retrofit) {
        return retrofit.create(BaiduApi.class);
    }
}
