package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.multitype.ZhiHuNews;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.ExLog;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface ZhiHuNewsContract {

    interface View extends BaseView<List<ZhiHuNews.ListItem>> {
    }

    class Presenter extends BasePagePresenter<View, List<ZhiHuNews.ListItem>> {

        private int mYear;
        private int mMonth;
        private int mDay;

        private List<String> mPages = new ArrayList<>();
        private List<ZhiHuNews.ListItem> mData = new ArrayList<>();
        private Map<String, List<ZhiHuNews.ListItem>> mPageDataMap = new HashMap<>();

        void getNewsAt(int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            int nowYear = c.get(Calendar.YEAR);
            int nowMonth = c.get(Calendar.MONTH);
            int nowDay = c.get(Calendar.DAY_OF_MONTH);
            Observable<ZhiHuNews.ListResponse> observable =
                    mYear == nowYear && nowMonth == mMonth && nowDay == mDay ?
                            SimpleApp.getAppComponent().getZhiHuService().getLatestNews()
                            : SimpleApp.getAppComponent().getZhiHuService().getNewsBefore(year, month, day);

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(response -> {
                        List<ZhiHuNews.ListItem> stories = response.stories;
                        for (ZhiHuNews.ListItem story : stories) {
                            story.date = response.date;
                        }
                        return stories;
                    })
                    .subscribe(entities -> {
                        onPageData(getPageKey(year, month, day), entities);
                    }, error -> {
                        onPageError(error);
                    });
        }

        @Override
        public void onRefreshData() {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH) + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mPages.clear();
            mPages.add(getPageKey(mYear, mMonth, mDay));
            getNewsAt(mYear, mMonth, mDay);
        }

        @Override
        public void onLoadMoreData() {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, mYear);
            c.set(Calendar.MONTH, mMonth - 1);
            c.set(Calendar.DAY_OF_MONTH, mDay);
            c.add(Calendar.DAY_OF_MONTH, -1);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH) + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mPages.add(getPageKey(mYear, mMonth, mDay));
            getNewsAt(mYear, mMonth, mDay);
        }

        @Override
        protected void onPageData(String page, List<ZhiHuNews.ListItem> pageData) {
            ExLog.d("onPageData(): page=%s, count=%s", page, pageData.size());
            mPageDataMap.put(page, pageData);

            mData.clear();
            for (String p : mPages) {
                List<ZhiHuNews.ListItem> data = mPageDataMap.get(p);
                if (data != null) {
                    mData.addAll(data);
                }
            }
            if (getView() != null) {
                getView().setData(mData);
            }
        }

        @Override
        protected void onPageError(Throwable throwable) {
            ExLog.e("onPageError()", throwable);
            Toaster.quick(throwable.toString());
            BaseView view = getView();
            if (view != null) {
                if (mPageDataMap.isEmpty()) {
                    view.showError(throwable, false);
                } else {
                    view.setData(mData);
                }
            }
        }

        private String getPageKey(int year, int month, int day) {
            return String.format("%s%s%s", year, month, day);
        }
    }
}
