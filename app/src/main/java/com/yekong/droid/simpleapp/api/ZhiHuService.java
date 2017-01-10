package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.model.ZhiHu;
import com.yekong.droid.simpleapp.util.Logger;

import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class ZhiHuService {
    private ZhiHuApi mZhiHuApi;

    public ZhiHuService(ZhiHuApi zhiHuApi) {
        mZhiHuApi = zhiHuApi;
    }

    public Observable<ZhiHu.NewsResponse> getLatestNews() {
        Logger.d("getLatestNews()");
        return mZhiHuApi.getLatestNews();
    }

    public Observable<ZhiHu.NewsResponse> getNewsBefore(int year, int month, int day) {
        String yyyyMMdd = String.format("%4d%02d%02d", year, month, day);
        Logger.d("getNewsBefore(): yyyyMMdd=%s", yyyyMMdd);
        return mZhiHuApi.getNewsBefore(yyyyMMdd);
    }

    public Observable<ZhiHu.NewsDetail> getNewsDetail(String id) {
        return mZhiHuApi.getNewsDetail(id);
    }
}
