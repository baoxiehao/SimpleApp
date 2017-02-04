package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.RssApi;
import com.yekong.droid.simpleapp.api.RssService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baoxiehao on 16/11/28.
 */
@Module(includes = {RssApiModule.class})
public class RssModule {
    @Provides
    @Singleton
    public RssService provideRssService(RssApi rssApi) {
        return new RssService(rssApi);
    }
}
