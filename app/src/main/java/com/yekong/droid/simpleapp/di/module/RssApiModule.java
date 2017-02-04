package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.RssApi;
import com.yekong.droid.simpleapp.di.Rss;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by baoxiehao on 17/2/4.
 */
@Module(includes = {RetrofitModule.class})
public class RssApiModule {
    @Provides
    @Singleton
    public RssApi provideRssApi(@Rss Retrofit retrofit) {
        return retrofit.create(RssApi.class);
    }
}
