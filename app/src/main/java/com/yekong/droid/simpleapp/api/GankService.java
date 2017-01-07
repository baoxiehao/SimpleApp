package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.Gank;

import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankService {
    private GankApi mGankApi;

    public GankService(GankApi gankApi) {
        mGankApi = gankApi;
    }

    public Observable<Gank.FuliResponse> getFuli(int count, int page) {
        return mGankApi.getFuli(count, page);
    }

    public Observable<Gank.ArticleResponse> getArticle(String type, int count, int page) {
        return mGankApi.getArticle(type, count, page);
    }
}
