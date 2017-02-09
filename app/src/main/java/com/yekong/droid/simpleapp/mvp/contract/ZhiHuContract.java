package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.ZhiHu;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                final String pageKey = getPageKey(year, month, day);

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
                        .subscribe(stories -> onPageData(pageKey, stories),
                                error -> onPageError(error));
            }

            @Override
            public boolean onRefreshData() {
                super.onRefreshData();

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);

                super.updatePageKey(getPageKey(mYear, mMonth, mDay), false);
                getNewsAt(mYear, mMonth, mDay);
                return true;
            }

            @Override
            public boolean onLoadMoreData() {
                super.onLoadMoreData();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, mYear);
                c.set(Calendar.MONTH, mMonth - 1);
                c.set(Calendar.DAY_OF_MONTH, mDay);
                c.add(Calendar.DAY_OF_MONTH, -1);

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);

                super.updatePageKey(getPageKey(mYear, mMonth, mDay));
                getNewsAt(mYear, mMonth, mDay);
                return true;
            }
        }
    }
}
