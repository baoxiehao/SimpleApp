package com.yekong.droid.simpleapp.mvp.contract;

import android.content.Context;
import android.text.TextUtils;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.api.GankApi;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.cache.CacheManager;
import com.yekong.droid.simpleapp.model.Gank;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.QiniuUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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

        int mMarkerIndex = 0;
        List<String> mMarkers = new ArrayList<>();

        public QiniuPresenter(String title) {
            mTitle = title;
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            final String keyMarkers = String.format("Markers-%s", mTitle);

            List<String> cachedMarkers = CacheManager.get(keyMarkers);
            if (cachedMarkers != null) {
                mMarkers = cachedMarkers;
                Logger.d("onRefreshData(): title = %s, cachedMarkers = %s", mTitle, TextUtils.join(", ", cachedMarkers));
            }

            mMarkerIndex = 0;
            final String marker = getCurrentMarker();
            QiniuUtils.parseBucketMarkers(marker, mTitle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(markers -> {
                                mMarkers.addAll(0, markers);
                                CacheManager.put(keyMarkers, mMarkers);
                                updatePageKey(getPageKey(marker), false);
                                loadPage();
                            },
                            error -> onPageError(error));
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            if (mMarkers.isEmpty()) {
                return false;
            }

            super.onLoadMoreData();

            mMarkerIndex += 1;
            updatePageKey(getPageKey(getCurrentMarker()));
            loadPage();
            return true;
        }

        private String getCurrentMarker() {
            final String marker = mMarkerIndex < mMarkers.size() ? mMarkers.get(mMarkerIndex) : null;
            Logger.d("getCurrentMarker(): title = %s, index = %s, marker = %s", mTitle, mMarkerIndex, marker);
            return marker;
        }

        private void loadPage() {
            final String marker = getCurrentMarker();
            final String pageKey = getPageKey(marker);

            QiniuUtils.parseBucketUrls(marker, mTitle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(urls -> {
                                onPageData(pageKey, urls);
                                // Auto reload when not last page
                                if (urls.size() < QiniuUtils.LIMIT && marker != null) {
                                    Logger.d("loadPage(): title = %s, auto reload when not last page", mTitle);
                                    onLoadMoreData();
                                }
                            },
                            error -> onPageError(error));
        }
    }
}
