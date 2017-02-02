package com.yekong.droid.simpleapp.ui;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.model.LinkItem;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.LinkContract;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class AndroidFragment extends RecyclerPageFragment<
        LinkItem, LinkContract.View, LinkContract.AndroidPresenter>
        implements LinkContract.View {

    @Override
    public LinkContract.AndroidPresenter createPresenter() {
        return new LinkContract.AndroidPresenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).url));
        return adapter;
    }

    class ListAdapter extends BaseAdapter<LinkItem> {

        public ListAdapter(List<LinkItem> data) {
            super(R.layout.item_text_3, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, LinkItem linkItem) {
            baseViewHolder.setText(R.id.primaryText, linkItem.title);
            baseViewHolder.setText(R.id.secondaryText, linkItem.source);
            if (TextUtils.isEmpty(linkItem.desc)) {
                baseViewHolder.getView(R.id.previewText).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.previewText).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.previewText, linkItem.desc);
            }
        }
    }
}
