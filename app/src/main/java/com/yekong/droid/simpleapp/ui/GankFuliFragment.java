package com.yekong.droid.simpleapp.ui;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.model.Gank;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.GankContract;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GankFuliFragment extends RecyclerPageFragment<
        Gank.Fuli, GankContract.Fuli.View, GankContract.Fuli.Presenter>
        implements GankContract.Fuli.View {

    @Override
    public GankContract.Fuli.Presenter createPresenter() {
        return new GankContract.Fuli.Presenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> {
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<String> urls = new ArrayList<>();
            for (Gank.Fuli fuli : mData) {
                titles.add(fuli.desc);
                urls.add(fuli.url);
            }
            UserCase.showImages(titles, urls, pos);
            mShowingDetail = true;
        });
        return adapter;
    }

    class ListAdapter extends BaseAdapter<Gank.Fuli> {
        public ListAdapter(List<Gank.Fuli> data) {
            super(R.layout.item_image_text, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Gank.Fuli fuli) {
            UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), fuli.url);
            baseViewHolder.setText(R.id.textView,
                    String.format("%s %s", DateUtils.dateToString(fuli.publishedAt), fuli.type));
        }
    }
}
