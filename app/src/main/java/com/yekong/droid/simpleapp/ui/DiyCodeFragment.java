package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.RssContract;
import com.yekong.droid.simpleapp.ui.adapter.RssAdapter;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

/**
 * Created by baoxiehao on 16/11/28.
 */
@FragmentWithArgs
public class DiyCodeFragment extends RecyclerPageFragment<
        RssItem, RssContract.View, RssContract.DiyCodePresenter>
        implements RssContract.View {

    @Arg
    String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public RssContract.DiyCodePresenter createPresenter() {
        return new RssContract.DiyCodePresenter(mPath);
    }

    @Override
    protected BaseAdapter setupAdapter() {
        BaseAdapter adapter = new RssAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).link));
        return adapter;
    }
}
