package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.util.Logger;

import java.util.List;

import rx.Observable;

/**
 * Created by baoxiehao on 17/2/4.
 */

public class RssService {
    private RssApi mRssApi;

    public RssService(RssApi rssApi) {
        mRssApi = rssApi;
    }

    public Observable<List<RssItem>> getRssItems(String source, String path, int page) {
        Logger.d("getRssItems(): source=%s, path=%s, page=%s", source, path, page);
        return mRssApi.getRssItems(source, path, page);
    }
}
