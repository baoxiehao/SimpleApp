package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.model.LinkItem;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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

        private static final String ANDROID_WEEEKLY_URL = "http://www.androidweekly.cn";
        private static final String ANDROID_DEV_URL = "https://www.androiddevdigest.com";

        private static final String ANDROID_WEEKLY_PATH = "android-dev-weekly-issue-";
        private static final String ANDROID_DEV_PATH = "digest-";

        private int mAndroidWeeklyPage;
        private int mAndroidDevPage;

        @Override
        public boolean onRefreshData() {
            super.onRefreshData();

            parseLatestPages();
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

        private int parsePageFromLink(String path, List<LinkItem> linkItems) {
            final String href = linkItems.get(0).url;
            final int fromIndex = href.indexOf(path) + path.length();
            final int toIndex = href.indexOf("/", fromIndex);
            final int page = Integer.valueOf(href.substring(fromIndex, toIndex));
            Logger.d("parsePageFromLink(): href = %s, page = %s", href, page);
            return page;
        }

        private void parseLatestPages() {
            Observable.zip(
                    WebUtils.parseLinks(ANDROID_WEEEKLY_URL,
                            String.format("a[href*=%s]", ANDROID_WEEKLY_PATH)),
                    WebUtils.parseLinks(ANDROID_DEV_URL,
                            String.format("a[href*=%s]", ANDROID_DEV_PATH)),
                    (linkItems1, linkItems2) -> {
                        mAndroidWeeklyPage = parsePageFromLink(ANDROID_WEEKLY_PATH, linkItems1);
                        mAndroidDevPage = parsePageFromLink(ANDROID_DEV_PATH, linkItems2);
                        Logger.d("parseLatestPages(): weeklyPage = %s, devPage = %s", mAndroidWeeklyPage, mAndroidDevPage);
                        return new ArrayList<LinkItem>();
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rssItems -> {
                                super.updatePageKey(getPageKey(mAndroidWeeklyPage, mAndroidDevPage), false);
                                loadAndroidPage();
                            },
                            error -> onPageError(error));
        }

        private void loadAndroidPage() {
            final String pageKey = getPageKey(mAndroidWeeklyPage, mAndroidDevPage);

            Observable.zip(
                    WebUtils.parseLinks(
                            String.format("%s/%s%d", ANDROID_WEEEKLY_URL, ANDROID_WEEKLY_PATH, mAndroidWeeklyPage),
                            "li > p > a[href]"),
                    WebUtils.parseLinks(
                            String.format("%s/%s%d", ANDROID_DEV_URL, ANDROID_DEV_PATH, mAndroidDevPage),
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
