package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.RssItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by baoxiehao on 17/2/4.
 */

public interface RssApi {
    @GET("api/rss/diycode")
    Observable<List<RssItem>> getRssItems(@Query("path") String path, @Query("page") int page);
}
