package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.BaiduImage;
import com.yekong.droid.simpleapp.multitype.BaiduImageViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.BaiduImageContract;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class BaiduImageFragment extends RecyclerPageFragment<
        List<BaiduImage.Entity>, BaiduImageContract.View, BaiduImageContract.Presenter>
        implements BaiduImageContract.View {

    @Override
    public BaiduImageContract.Presenter createPresenter() {
        return new BaiduImageContract.Presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        super.presenter.onRefreshData();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(BaiduImage.Entity.class, new BaiduImageViewProvider());
        return adapter;
    }
}
