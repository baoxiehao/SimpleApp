package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.RssUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by baoxiehao on 17/2/4.
 */

public interface RssContract {

    interface View extends BaseView<List<RssItem>> {

    }

    final class TechPresenter extends BasePagePresenter<View, RssItem> {

        int mPage;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage++;
            if (mPage > 3) {
                return false;
            }
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mPage);
            updatePageKey(pageKey, mPage != 1);

            Observable<List<RssItem>> observable = null;
            switch (mPage) {
                case 1:
                    observable = RssUtils.parseIfanrItems();
                    break;
                case 2:
                    observable = RssUtils.parseGeekParkItems();
                    break;
                case 3:
                    observable = RssUtils.parseQdailyItems();
                    break;
                default:
                    break;
            }

            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(rssItems -> onPageData(pageKey, rssItems),
                                error -> onPageError(error));
            }
        }
    }

    final class DiyCodePresenter extends BasePagePresenter<View, RssItem> {

        private static final String PATH_TOPICS = "topics";
        private static final String PATH_PROJECTS = "projects";
        private static final String PATH_TRENDS = "trends/Android";
        private static final String PATH_NEWS = "news";
        private static final String PATH_SITES = "sites";

        String mPath;
        int mPage = 1;

        public DiyCodePresenter(String path) {
            mPath = path;
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            if (PATH_SITES.equals(mPath)) {
                return false;
            }

            mPage++;
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mPath, mPage);
            updatePageKey(pageKey, mPage != 1);

            Observable<List<RssItem>> observable = null;
            if (PATH_TOPICS.equals(mPath) || PATH_NEWS.equals(mPath)) {
                observable = RssUtils.parseDiycodeTopics(mPath, mPage);
            } else if (PATH_PROJECTS.equals(mPath)) {
                observable = RssUtils.parseDiycodeProjects(mPath, mPage);
            } else if (PATH_TRENDS.equals(mPath)) {
                observable = RssUtils.parseDiycodeTrends(mPath, mPage);
            } else if (PATH_SITES.equals(mPath)) {
                observable = RssUtils.parseDiycodeSites(mPath, mPage);
            }
            if (observable != null) {
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(rssItems -> onPageData(pageKey, rssItems),
                                error -> onPageError(error));
            }
        }
    }
}
