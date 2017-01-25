package com.yekong.droid.simpleapp.ui;

import com.chad.library.adapter.base.BaseViewHolder;
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

public class RssFragment extends RecyclerPageFragment<
        RssItem, RssContract.View, RssContract.Presenter>
        implements RssContract.View {

    @Override
    public RssContract.Presenter createPresenter() {
        return new RssContract.Presenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).url));
        return adapter;
    }

    class ListAdapter extends BaseAdapter<RssItem> {

        public ListAdapter(List<RssItem> data) {
            super(R.layout.item_text_2, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, RssItem rssItem) {
            baseViewHolder.setText(R.id.primaryText, rssItem.title);
            baseViewHolder.setText(R.id.secondaryText, rssItem.source);
        }
    }
}
