package com.yekong.droid.simpleapp.mvp.contract;

import android.content.Context;
import android.text.TextUtils;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.api.GankApi;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.Gank;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.QiniuUtils;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 17/1/25.
 */

public interface ImageContract {

    interface View extends BaseView<List<String>> {
    }

    abstract class Presenter extends BasePagePresenter<View, String> {
        public static Presenter createPresenter(final String title) {
            final Context context = SimpleApp.getAppComponent().getContext();
            if (TextUtils.equals(context.getString(R.string.fragment_title_fuli), title)) {
                return new ImageContract.FuliPresenter();
            } else {
                return new ImageContract.QiniuPresenter(title);
            }
        }
    }

    class FuliPresenter extends Presenter {

        int mPage = 1;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            updatePageKey(getPageKey(mPage));
            getFuli(mPage);
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage += 1;
            updatePageKey(getPageKey(mPage));
            getFuli(mPage);
            return true;
        }

        void getFuli(int page) {
            final String pageKey = getPageKey(page);

            SimpleApp.getAppComponent().getGankService().getFuli(GankApi.PAGE_SIZE, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(response -> {
                        List<String> urls = new ArrayList<>();
                        for (Gank.Fuli fuli : response.results) {
                            urls.add(fuli.url);
                        }
                        return urls;
                    })
                    .subscribe(urls -> onPageData(pageKey, urls),
                            error -> onPageError(error));
        }
    }

    class QiniuPresenter extends Presenter {

        String mTitle;

        boolean mIsLastPage = false;
        String mMarker = null;

        public QiniuPresenter(String title) {
            mTitle = title;
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mMarker = null;
            updatePageKey(getPageKey(mMarker), false);
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            if (mIsLastPage) {
                return false;
            }

            super.onLoadMoreData();

            updatePageKey(getPageKey(mMarker));
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mMarker);

            QiniuUtils.parseBucketKeys(mMarker, mTitle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pair -> {
                                mMarker = pair.first;
                                onPageData(pageKey, pair.second);
                                mIsLastPage = mMarker == null;
                            },
                            error -> onPageError(error));
        }
    }
}
