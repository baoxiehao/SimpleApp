package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.Gank;
import com.yekong.droid.simpleapp.multitype.GankFuliViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GankFuliContract;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankFuliFragment extends RecyclerPageFragment<
        List<Gank.Fuli>, GankFuliContract.View, GankFuliContract.Presenter>
        implements GankFuliContract.View {

    @Override
    public GankFuliContract.Presenter createPresenter() {
        return new GankFuliContract.Presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        super.presenter.onRefreshData();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(Gank.Fuli.class, new GankFuliViewProvider());
        return adapter;
    }
}
