package com.yekong.droid.simpleapp.mvp.presenter;

import com.yekong.droid.simpleapp.mvp.view.BaseView;

/**
 * Created by mi on 16-11-29.
 */

public abstract class BasePagePresenter<V extends BaseView, M> extends BasePresenter<V> {

    public abstract void onRefreshData();

    public abstract void onLoadMoreData();

    protected abstract void onPageData(String page, M data);

    protected abstract void onPageError(Throwable throwable);
}
