package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

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
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */
@FragmentWithArgs
public class DiyCodeFragment extends RecyclerPageFragment<
        RssItem, RssContract.View, RssContract.Presenter>
        implements RssContract.View {

    @Arg
    String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public RssContract.Presenter createPresenter() {
        return new RssContract.Presenter(mPath);
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).link));
        return adapter;
    }

    class ListAdapter extends BaseAdapter<RssItem> {

        public ListAdapter(List<RssItem> data) {
            super(R.layout.item_image_text_2, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, RssItem rssItem) {
            UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), rssItem.image);
            baseViewHolder.setText(R.id.primaryText, rssItem.title);
            if (!TextUtils.isEmpty(rssItem.desc)) {
                baseViewHolder.getView(R.id.secondaryText).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.secondaryText, rssItem.desc);
            } else if (!TextUtils.isEmpty(rssItem.time)) {
                baseViewHolder.getView(R.id.secondaryText).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.secondaryText, rssItem.time);
            } else {
                baseViewHolder.getView(R.id.secondaryText).setVisibility(View.GONE);
            }
        }
    }
}
