package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.ZhiHu;
import com.yekong.droid.simpleapp.multitype.ZhiHuNewsViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.ZhiHuContract;

import java.util.ArrayList;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class ZhiHuFragment extends RecyclerPageFragment<
        ZhiHu.News, ZhiHuContract.News.View, ZhiHuContract.News.Presenter>
        implements ZhiHuContract.News.View {

    @Override
    public ZhiHuContract.News.Presenter createPresenter() {
        return new ZhiHuContract.News.Presenter();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData != null ? mData : new ArrayList<>());
        adapter.register(ZhiHu.News.class, new ZhiHuNewsViewProvider());
        return adapter;
    }
}
