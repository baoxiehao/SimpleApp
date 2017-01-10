package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.Gank;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface GankApi {
    int PAGE_SIZE = 20;

    @GET("data/福利/{count}/{page}")
    Observable<Gank.FuliResponse> getFuli(@Path("count") int count, @Path("page") int page);

    @GET("data/{type}/{count}/{page}")
    Observable<Gank.ArticleResponse> getArticle(@Path("type") String type, @Path("count") int count, @Path("page") int page);
}
