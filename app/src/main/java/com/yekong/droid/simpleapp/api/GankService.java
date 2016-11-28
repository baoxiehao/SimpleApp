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

    public Observable<Gank.FuliResponse> getFuli(int month, int day) {
        return mGankApi.getFuli(month, day);
    }

    public Observable<Gank.ArticleResponse> getArticle(String type, int month, int day) {
        return mGankApi.getArticle(type, month, day);
    }
}
