package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.Baidu;
import com.yekong.droid.simpleapp.multitype.BaiduImageViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.BaiduContract;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class BaiduImageFragment extends RecyclerPageFragment<
        Baidu.Image, BaiduContract.Image.View, BaiduContract.Image.Presenter>
        implements BaiduContract.Image.View {

    @Override
    public BaiduContract.Image.Presenter createPresenter() {
        return new BaiduContract.Image.Presenter();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);
        adapter.register(Baidu.Image.class, new BaiduImageViewProvider());
        return adapter;
    }
}
