package com.yekong.droid.simpleapp.ui;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.model.Gank;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.GankContract;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;
import com.yekong.droid.simpleapp.util.DateUtils;

import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankFragment extends RecyclerPageFragment<
        Gank.Article, GankContract.View, GankContract.Presenter>
        implements GankContract.View {

    @Override
    public GankContract.Presenter createPresenter() {
        return new GankContract.Presenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> UserCase.showWebView(mData.get(pos).url));
        return adapter;
    }

    class ListAdapter extends BaseAdapter<Gank.Article> {

        public ListAdapter(List<Gank.Article> data) {
            super(R.layout.item_text_2, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Gank.Article article) {
            baseViewHolder.setText(R.id.primaryText, article.desc);
            baseViewHolder.setText(R.id.secondaryText, String.format("%s %s",
                    DateUtils.dateToString(article.publishedAt), article.who != null ? article.who : ""));
        }
    }
}
