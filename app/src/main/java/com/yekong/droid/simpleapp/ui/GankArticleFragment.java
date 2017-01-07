package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.Gank;
import com.yekong.droid.simpleapp.multitype.GankArticleViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GankContract;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankArticleFragment extends RecyclerPageFragment<
        Gank.Article, GankContract.Article.View, GankContract.Article.Presenter>
        implements GankContract.Article.View {

    @Override
    public GankContract.Article.Presenter createPresenter() {
        return new GankContract.Article.Presenter();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(Gank.Article.class, new GankArticleViewProvider());
        return adapter;
    }
}
