package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.model.LinkItem;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.WebUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 17/1/25.
 */

public interface LinkContract {

    interface View extends BaseView<List<LinkItem>> {

    }

    final class BlogPresenter extends BasePagePresenter<View, LinkItem> {

        private static final String URL_PREFIX = "http://coolshell.cn/page/%s";

        int mPage = 1;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            super.updatePageKey(getPageKey(mPage), false);
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage += 1;
            super.updatePageKey(getPageKey(mPage));
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mPage);

            WebUtils.parseLinks(String.format(URL_PREFIX, mPage), ".entry-title > a[href]")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));
        }
    }

    final class TechPresenter extends BasePagePresenter<View, LinkItem> {

        private static final String URL_IFANR = "http://www.ifanr.com";
        private static final String URL_GEEK = "http://www.geekpark.net";

        int mPage = 1;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mPage = 1;
            super.updatePageKey(getPageKey(mPage), false);
            loadPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mPage = 1;
            super.updatePageKey(getPageKey(mPage));
            loadPage();
            return true;
        }

        private void loadPage() {
            final String pageKey = getPageKey(mPage);

            Observable.zip(
                    WebUtils.parseLinks(URL_IFANR, ".article-info > h3 > a[href]"),
                    WebUtils.parseLinks(URL_GEEK, "a[href].article-title"),
                    (rssItems1, rssItems2) -> {
                        List<LinkItem> linkItems = new ArrayList<>();
                        linkItems.addAll(rssItems1);
                        linkItems.addAll(rssItems2);
                        Logger.d("zip %s %s", rssItems1.size(), rssItems2.size());
                        return linkItems;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));

        }
    }

    final class AndroidPresenter extends BasePagePresenter<View, LinkItem> {

        private static final String ANDROID_WEEEKLY_URL_FORMAT = "http://www.androidweekly.cn/android-dev-weekly-issue-%s/";
        private static final String ANDROID_DEV_URL_FORMAT = "https://www.androiddevdigest.com/digest-%s/";

        private static final int ANDROID_WEEKLY_BASE_PAGE = 111;
        private static final int ANDROID_DEV_BASE_PAGE = 124;

        private static final Calendar ANDROID_WEEKLY_BASE_DATE = DateUtils.getCalendar(2017, 1, 3);
        private static final Calendar ANDROID_DEV_BASE_DATE = DateUtils.getCalendar(2017, 1, 3);

        private int mAndroidWeeklyPage;
        private int mAndroidDevPage;

        public static int getLatestPage(final int basePage, final Calendar baseDate) {
            return basePage + DateUtils.getDayOffset(Calendar.getInstance(), baseDate) / 7;
        }

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            mAndroidWeeklyPage = getLatestPage(ANDROID_WEEKLY_BASE_PAGE, ANDROID_WEEKLY_BASE_DATE);
            mAndroidDevPage = getLatestPage(ANDROID_DEV_BASE_PAGE, ANDROID_DEV_BASE_DATE);
            super.updatePageKey(getPageKey(mAndroidWeeklyPage, mAndroidDevPage), false);
            loadAndroidPage();
            return true;
        }

        @Override
        public boolean onLoadMoreData() {
            super.onLoadMoreData();

            mAndroidWeeklyPage -= 1;
            mAndroidDevPage -= 1;
            super.updatePageKey(getPageKey(mAndroidWeeklyPage, mAndroidDevPage));
            loadAndroidPage();
            return true;
        }

        private void loadAndroidPage() {
            final String pageKey = getPageKey(mAndroidWeeklyPage, mAndroidDevPage);

            Observable.zip(
                    WebUtils.parseLinks(
                            String.format(ANDROID_WEEEKLY_URL_FORMAT, mAndroidWeeklyPage),
                            "li > p > a[href]"),
                    WebUtils.parseLinks(
                            String.format(ANDROID_DEV_URL_FORMAT, mAndroidDevPage),
                            "p > a[href]"),
                    (rssItems1, rssItems2) -> {
                        List<LinkItem> linkItems = new ArrayList<>();
                        linkItems.addAll(rssItems1);
                        linkItems.addAll(rssItems2);
                        Logger.d("zip %s %s", rssItems1.size(), rssItems2.size());
                        return linkItems;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> onPageData(pageKey, rssItems),
                            error -> onPageError(error));
        }
    }
}
