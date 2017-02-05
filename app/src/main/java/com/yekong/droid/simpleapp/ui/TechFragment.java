package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.RssContract;
import com.yekong.droid.simpleapp.ui.adapter.RssAdapter;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class TechFragment extends RecyclerPageFragment<
        RssItem, RssContract.View, RssContract.TechPresenter>
        implements RssContract.View {

    @Override
    public RssContract.TechPresenter createPresenter() {
        return new RssContract.TechPresenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        BaseAdapter adapter = new RssAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).link));
        return adapter;
    }
}
