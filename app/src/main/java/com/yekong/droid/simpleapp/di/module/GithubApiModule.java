package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.GithubApi;
import com.yekong.droid.simpleapp.di.Github;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module(includes = {RetrofitModule.class})
public class GithubApiModule {
    @Provides
    @Singleton
    public GithubApi provideGithubApi(@Github Retrofit retrofit) {
        return retrofit.create(GithubApi.class);
    }
}
