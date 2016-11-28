package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.GithubRepo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/26.
 */

public interface GithubApi {
    int REPO_PAGE_SIZE = 3;

    @GET("users/{login}/repos")
    Observable<List<GithubRepo>> getUserRepos(
            @Path("login") String login,
            @Query("page") int page,
            @Query("per_page") int pageSize);

    @GET("search/repositories")
    Observable<List<GithubRepo>> searchRepos(
            @Query("q") String query,
            @Query("sort") String sort);
}
