package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 17/2/4.
 */

public interface RssContract {

    interface View extends BaseView<List<RssItem>> {

    }

    final class Presenter extends BasePagePresenter<View, RssItem> {

        String mPath;
        int mPage = 1;

        public Presenter(String path) {
            mPath = path;
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            updatePageKey(getPageKey(mPath, mPage), false);
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage++;
            updatePageKey(getPageKey(mPath, mPage));
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mPath, mPage);

            SimpleApp.getAppComponent().getRssService()
                    .getRssItems(mPath, mPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));
        }
    }
}
