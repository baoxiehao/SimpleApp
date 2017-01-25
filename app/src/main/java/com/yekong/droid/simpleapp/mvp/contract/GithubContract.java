package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.api.GithubApi;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.GithubRepo;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/26.
 */

public interface GithubContract {

    interface View extends BaseView<List<GithubRepo>> {
    }

    class Presenter extends BasePagePresenter<View, GithubRepo> {

        int mPage = 1;

        @Override
        public boolean onRefreshData() {
            SimpleApp.getAppComponent().getGithubService().getUserRepos("baoxiehao", mPage, GithubApi.REPO_PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(repos -> {
                        onPageData(super.getPageKey(), repos);
                    }, error -> {
                        onPageError(error);
                    });
            return true;
        }
    }
}
