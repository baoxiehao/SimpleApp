package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.Gank;
import com.yekong.droid.simpleapp.multitype.GankFuliViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GankContract;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankFuliFragment extends RecyclerPageFragment<
        Gank.Fuli, GankContract.Fuli.View, GankContract.Fuli.Presenter>
        implements GankContract.Fuli.View {

    @Override
    public GankContract.Fuli.Presenter createPresenter() {
        return new GankContract.Fuli.Presenter();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(Gank.Fuli.class, new GankFuliViewProvider());
        return adapter;
    }
}
