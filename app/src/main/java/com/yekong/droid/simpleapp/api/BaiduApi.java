package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.BaiduImage;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by baoxiehao on 16/11/26.
 */

public interface BaiduApi {
    @GET("data/imgs")
    Observable<BaiduImage.Response> searchImages(@QueryMap Map<String, String> options);
}
