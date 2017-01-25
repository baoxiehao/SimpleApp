package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.ImageContract;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoxiehao on 16/11/28.
 */

@FragmentWithArgs
public class ImageFragment extends RecyclerPageFragment<
        String, ImageContract.View, ImageContract.Presenter>
        implements ImageContract.View {

    @Arg
    String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public ImageContract.Presenter createPresenter() {
        return ImageContract.Presenter.createPresenter(mTitle);
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> {
            ArrayList<String> urls = new ArrayList<>();
            urls.addAll(mData);
            UserCase.showImages(mTitle, urls, pos);
            mShowingDetail = true;
        });
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager setupLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    class ListAdapter extends BaseAdapter<String> {
        public ListAdapter(List<String> data) {
            super(R.layout.item_image, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String url) {
            UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), url);
        }
    }
}
