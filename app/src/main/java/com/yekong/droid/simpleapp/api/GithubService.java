package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.GithubRepo;

import java.util.List;

import rx.Observable;

/**
 * Created by baoxiehao on 16/11/26.
 */

public class GithubService {
    private GithubApi mGithubApi;

    public GithubService(GithubApi githubApi) {
        mGithubApi = githubApi;
    }

    public Observable<List<GithubRepo>> getUserRepos(String user, int page, Integer pageSize) {
        return mGithubApi.getUserRepos(user, page, pageSize);
    }

    public Observable<List<GithubRepo>> searchRepos(String query, String sort) {
        return mGithubApi.searchRepos(query, sort);
    }
}
