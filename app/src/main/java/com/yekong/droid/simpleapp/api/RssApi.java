package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.RssItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by baoxiehao on 17/2/4.
 */

public interface RssApi {
    @GET("api/rss/{source}")
    Observable<List<RssItem>> getRssItems(
            @Path("source") String source,
            @Query("path") String path,
            @Query("page") int page);
}
