package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.QiniuUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by baoxiehao on 17/2/4.
 */

public interface RssContract {

    interface View extends BaseView<List<RssItem>> {

    }

    final class TechPresenter extends BasePagePresenter<View, RssItem> {

        Date mDate;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mDate = new Date(System.currentTimeMillis());

            updatePageKey(getPageKey(DateUtils.dateToString(mDate)), false);
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            Calendar c = Calendar.getInstance();
            c.setTime(mDate);
            c.add(Calendar.DAY_OF_MONTH, -1);
            mDate = new Date(c.getTimeInMillis());

            updatePageKey(getPageKey(DateUtils.dateToString(mDate)), true);
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(DateUtils.dateToString(mDate));

            QiniuUtils.fetchBucketFile(String.format("rss-%s.json", DateUtils.dateToString(mDate)))
                    .subscribeOn(Schedulers.io())
                    .map(fileContent -> RssItem.fromString(fileContent))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));
        }
    }

    final class DiyCodePresenter extends BasePagePresenter<View, RssItem> {

        final static String SOURCE = "diycode";

        String mPath;
        int mPage = 1;

        public DiyCodePresenter(String path) {
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
                    .getRssItems(SOURCE, mPath, mPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));
        }
    }
}
