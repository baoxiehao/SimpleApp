package com.yekong.droid.simpleapp.di.module;

import com.yekong.droid.simpleapp.api.GithubApi;
import com.yekong.droid.simpleapp.api.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baoxiehao on 16/11/26.
 */
@Module(includes = {GithubApiModule.class})
public class GithubModule {
    @Provides
    @Singleton
    public GithubService provideGithubService(GithubApi githubApi) {
        return new GithubService(githubApi);
    }
}
