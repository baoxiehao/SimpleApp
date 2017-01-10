package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.Baidu;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/26.
 */

public interface BaiduApi {
    @GET("data/imgs")
    Observable<Baidu.ImagesResponse> searchImages(@QueryMap Map<String, String> options);
}
