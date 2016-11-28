package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.multitype.Gank;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.ExLog;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface GankFuliContract {

    interface View extends BaseView<List<Gank.Fuli>> {
    }

    class Presenter extends BasePagePresenter<View, List<Gank.Fuli>> {

        private int mMonth;
        private int mDay;

        private List<String> mPages = new ArrayList<>();
        private List<Gank.Fuli> mData;
        private Map<String, List<Gank.Fuli>> mPageDataMap = new HashMap<>();

        void getFuli(int month, int day) {
            SimpleApp.getAppComponent().getGankService().getFuli(month, day)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(response -> response.results)
                    .subscribe(entities -> {
                        onPageData(getPageKey(month, day), entities);
                    }, error -> {
                        onPageError(error);
                    });
        }

        @Override
        public void onRefreshData() {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mPages.clear();
            mPages.add(getPageKey(mMonth, mDay));
            getFuli(mMonth, mDay);
        }

        @Override
        public void onLoadMoreData() {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, mMonth);
            c.set(Calendar.DAY_OF_MONTH, mDay);
            c.add(Calendar.DAY_OF_MONTH, -1);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mPages.add(getPageKey(mMonth, mDay));
            getFuli(mMonth, mDay);
        }

        @Override
        protected void onPageData(String page, List<Gank.Fuli> pageData) {
            ExLog.d("onPageData(): page=%s, count=%s", page, pageData.size());
            mPageDataMap.put(page, pageData);

            mData = new ArrayList<>();
            for (String p : mPages) {
                mData.addAll(mPageDataMap.get(p));
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

        private String getPageKey(int month, int day) {
            return String.format("%s%s", month, day);
        }
    }
}
