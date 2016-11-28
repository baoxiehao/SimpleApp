package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.ZhiHuNews;
import com.yekong.droid.simpleapp.util.ExLog;

import rx.Observable;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class ZhiHuService {
    private ZhiHuApi mZhiHuApi;

    public ZhiHuService(ZhiHuApi zhiHuApi) {
        mZhiHuApi = zhiHuApi;
    }

    public Observable<ZhiHuNews.ListResponse> getLatestNews() {
        ExLog.d("getLatestNews()");
        return mZhiHuApi.getLatestNews();
    }

    public Observable<ZhiHuNews.ListResponse> getNewsBefore(int year, int month, int day) {
        String yyyyMMdd = String.format("%4d%02d%02d", year, month, day);
        ExLog.d("getNewsBefore(): yyyyMMdd=%s", yyyyMMdd);
        return mZhiHuApi.getNewsBefore(yyyyMMdd);
    }

    public Observable<ZhiHuNews.DetailResponse> getNewsDetail(String id) {
        return mZhiHuApi.getNewsDetail(id);
    }
}
