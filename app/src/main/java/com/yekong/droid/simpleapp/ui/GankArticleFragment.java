package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.Gank;
import com.yekong.droid.simpleapp.multitype.GankArticleViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GankArticleContract;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankArticleFragment extends RecyclerPageFragment<
        List<Gank.Article>, GankArticleContract.View, GankArticleContract.Presenter>
        implements GankArticleContract.View {

    @Override
    public GankArticleContract.Presenter createPresenter() {
        return new GankArticleContract.Presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        super.presenter.onRefreshData();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(Gank.Article.class, new GankArticleViewProvider());
        return adapter;
    }
}
