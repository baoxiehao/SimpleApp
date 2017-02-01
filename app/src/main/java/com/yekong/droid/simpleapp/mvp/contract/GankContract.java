package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.api.GankApi;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.Gank;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface GankContract {

    interface View extends BaseView<List<Gank.Article>> {
    }

    class Presenter extends BasePagePresenter<View, Gank.Article> {

        int mPage = 1;

        void getAndroid(int page) {
            final String pageKey = getPageKey(page);

            SimpleApp.getAppComponent().getGankService().getArticle("Android", GankApi.PAGE_SIZE, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(response -> response.results)
                    .subscribe(entities -> onPageData(pageKey, entities),
                            error -> onPageError(error));
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            updatePageKey(getPageKey(mPage));
            getAndroid(mPage);
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage += 1;
            updatePageKey(getPageKey(mPage));
            getAndroid(mPage);
            return true;
        }
    }
}
