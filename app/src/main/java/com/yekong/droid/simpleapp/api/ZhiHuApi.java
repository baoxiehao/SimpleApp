package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.ZhiHuNews;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface ZhiHuApi {
    @GET("news/latest")
    Observable<ZhiHuNews.ListResponse> getLatestNews();

    @GET("news/before/{yyyyMMdd}")
    Observable<ZhiHuNews.ListResponse> getNewsBefore(@Path("yyyyMMdd") String yyyyMMdd);

    @GET("news/{id}")
    Observable<ZhiHuNews.DetailResponse> getNewsDetail(@Path("id") String id);
}
