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

    interface Article {
        interface View extends BaseView<List<Gank.Article>> {
        }

        class Presenter extends BasePagePresenter<GankContract.Article.View, Gank.Article> {

            void getAndroid() {
                SimpleApp.getAppComponent().getGankService().getArticle("Android", GankApi.PAGE_SIZE, mPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> response.results)
                        .subscribe(entities -> {
                            onPageData(super.getPageKey(mPage), entities);
                        }, error -> {
                            onPageError(error);
                        });
            }

            @Override
            public void onRefreshData() {
                super.onRefreshData();
                getAndroid();
            }

            @Override
            public void onLoadMoreData() {
                super.onLoadMoreData();
                getAndroid();
            }
        }
    }

    interface Fuli {
        interface View extends BaseView<List<Gank.Fuli>> {
        }

        class Presenter extends BasePagePresenter<GankContract.Fuli.View, Gank.Fuli> {

            void getFuli() {
                SimpleApp.getAppComponent().getGankService().getFuli(GankApi.PAGE_SIZE, mPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> response.results)
                        .subscribe(entities -> {
                            onPageData(super.getPageKey(mPage), entities);
                        }, error -> {
                            onPageError(error);
                        });
            }

            @Override
            public void onRefreshData() {
                super.onRefreshData();
                getFuli();
            }

            @Override
            public void onLoadMoreData() {
                super.onLoadMoreData();
                getFuli();
            }
        }
    }
}
