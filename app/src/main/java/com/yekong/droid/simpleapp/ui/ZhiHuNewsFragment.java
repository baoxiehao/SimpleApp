package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.ZhiHuNews;
import com.yekong.droid.simpleapp.multitype.ZhiHuNewsViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.ZhiHuNewsContract;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class ZhiHuNewsFragment extends RecyclerPageFragment<
        List<ZhiHuNews.ListItem>, ZhiHuNewsContract.View, ZhiHuNewsContract.Presenter>
        implements ZhiHuNewsContract.View {

    @Override
    public ZhiHuNewsContract.Presenter createPresenter() {
        return new ZhiHuNewsContract.Presenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        super.presenter.onRefreshData();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(ZhiHuNews.ListItem.class, new ZhiHuNewsViewProvider());
        return adapter;
    }
}
