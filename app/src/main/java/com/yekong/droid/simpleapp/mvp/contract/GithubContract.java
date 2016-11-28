package com.yekong.droid.simpleapp.mvp.contract;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.yekong.droid.simpleapp.api.GithubApi;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.multitype.GithubRepo;
import com.yekong.droid.simpleapp.util.ExLog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/26.
 */

public interface GithubContract {

    interface View extends MvpLceView<List<GithubRepo>> {
        void onUserRepos(int page, List<GithubRepo> repos);
        void onSearchRepos(List<GithubRepo> repos);
    }

    interface P {
        void loadUserRepos(int page);
        void searchRepos(String query);
    }

    class Presenter extends MvpBasePresenter<View> implements P {
        @Override
        public void loadUserRepos(int page) {
            SimpleApp.getAppComponent().getGithubService().getUserRepos("baoxiehao", page, GithubApi.REPO_PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(repos -> {
                        if (getView() != null) {
                            getView().onUserRepos(page, repos);
                        }
                    }, error -> {
                        ExLog.e("loadUserRepos", error);
                        if (getView() != null) {
                            getView().showError(error, false);
                        }
                    });
        }

        @Override
        public void searchRepos(String query) {
            SimpleApp.getAppComponent().getGithubService().searchRepos(query, "stars")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(repos -> {
                        if (getView() != null) {
                            getView().onSearchRepos(repos);
                        }
                    }, error -> {
                        ExLog.e("searchImages", error);
                        if (getView() != null) {
                            getView().showError(error, false);
                        }
                    });
        }
    }
}
