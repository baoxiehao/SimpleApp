package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.Gank;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface GankApi {
    @GET("data/福利/{month}/{day}")
    Observable<Gank.FuliResponse> getFuli(@Path("month") int month, @Path("day") int day);

    @GET("data/{type}/{month}/{day}")
    Observable<Gank.ArticleResponse> getArticle(@Path("type") String type, @Path("month") int month, @Path("day") int day);
}
