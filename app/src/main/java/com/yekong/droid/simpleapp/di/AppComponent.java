package com.yekong.droid.simpleapp.di;

import android.content.Context;

import com.yekong.droid.simpleapp.api.GankService;
import com.yekong.droid.simpleapp.api.RssService;
import com.yekong.droid.simpleapp.api.ZhiHuService;
import com.yekong.droid.simpleapp.di.module.BusModule;
import com.yekong.droid.simpleapp.di.module.ContextModule;
import com.yekong.droid.simpleapp.di.module.GankModule;
import com.yekong.droid.simpleapp.di.module.GithubModule;
import com.yekong.droid.simpleapp.di.module.RssModule;
import com.yekong.droid.simpleapp.di.module.ZhiHuModule;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by baoxiehao on 16/11/26.
 */

@Singleton
@Component(modules = {ContextModule.class, BusModule.class, GithubModule.class, GankModule.class, ZhiHuModule.class, RssModule.class})
public interface AppComponent {
    Context getContext();
    GankService getGankService();
    ZhiHuService getZhiHuService();
    RssService getRssService();
    EventBus getBus();
}
