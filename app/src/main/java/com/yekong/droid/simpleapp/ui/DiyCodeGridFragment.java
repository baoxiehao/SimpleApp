package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.RssContract;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */
@FragmentWithArgs
public class DiyCodeGridFragment extends RecyclerPageFragment<
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
        BaseAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).link));
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager setupLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    class ListAdapter extends BaseAdapter<RssItem> {
        public ListAdapter(List<RssItem> data) {
            super(R.layout.item_text, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, RssItem data) {
            baseViewHolder.setText(R.id.primaryText, data.title);
        }
    }
}
