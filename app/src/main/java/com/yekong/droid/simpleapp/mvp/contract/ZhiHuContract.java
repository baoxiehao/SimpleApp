package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.multitype.ZhiHu;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface ZhiHuContract {

    interface News {
        interface View extends BaseView<List<ZhiHu.News>> {
        }

        class Presenter extends BasePagePresenter<View, ZhiHu.News> {

            private int mYear;
            private int mMonth;
            private int mDay;

            void getNewsAt(int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                int nowYear = c.get(Calendar.YEAR);
                int nowMonth = c.get(Calendar.MONTH);
                int nowDay = c.get(Calendar.DAY_OF_MONTH);
                Observable<ZhiHu.NewsResponse> observable =
                        mYear == nowYear && nowMonth == mMonth && nowDay == mDay ?
                                SimpleApp.getAppComponent().getZhiHuService().getLatestNews()
                                : SimpleApp.getAppComponent().getZhiHuService().getNewsBefore(year, month, day);

                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> {
                            List<ZhiHu.News> stories = response.stories;
                            for (ZhiHu.News story : stories) {
                                story.date = response.date;
                            }
                            return stories;
                        })
                        .subscribe(stories -> {
                            onPageData(getPageKey(), stories);
                        }, error -> {
                            onPageError(error);
                        });
            }

            @Override
            protected boolean isNumericPage() {
                return false;
            }

            @Override
            protected String getPageKey(Object... args) {
                return super.getPageKey(mYear, mMonth, mDay);
            }

            @Override
            public void onRefreshData() {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);

                super.onRefreshData();

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

                super.onLoadMoreData();

                getNewsAt(mYear, mMonth, mDay);
            }
        }
    }
}
