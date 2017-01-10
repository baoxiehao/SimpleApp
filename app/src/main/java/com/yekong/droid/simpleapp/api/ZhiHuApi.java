package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.ZhiHu;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface ZhiHuApi {
    @GET("news/latest")
    Observable<ZhiHu.NewsResponse> getLatestNews();

    @GET("news/before/{yyyyMMdd}")
    Observable<ZhiHu.NewsResponse> getNewsBefore(@Path("yyyyMMdd") String yyyyMMdd);

    @GET("news/{id}")
    Observable<ZhiHu.NewsDetail> getNewsDetail(@Path("id") String id);
}
