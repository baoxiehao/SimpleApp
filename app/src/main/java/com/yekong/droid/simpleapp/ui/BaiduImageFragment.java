package com.yekong.droid.simpleapp.ui;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.multitype.Baidu;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.BaiduContract;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

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
    protected RecyclerView.Adapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> {
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<String> urls = new ArrayList<>();
            for (Baidu.Image image : mData) {
                titles.add(image.title);
                urls.add(image.imageUrl);
            }
            UserCase.showImages(titles, urls, pos);
            mShowingDetail = true;
        });
        return adapter;
    }

    class ListAdapter extends BaseQuickAdapter<Baidu.Image> {
        public ListAdapter(List<Baidu.Image> data) {
            super(R.layout.item_image_text, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Baidu.Image image) {
            UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), image.imageUrl);
            baseViewHolder.setText(R.id.textView,
                    String.format("%s %s", DateUtils.dateToString(image.date), image.title));
            baseViewHolder.getView(R.id.textView).setOnClickListener(view -> UserCase.showWebView(image.fromUrl));
        }
    }
}
