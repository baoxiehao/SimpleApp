package com.yekong.droid.simpleapp.ui;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.ZhiHu;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.mvp.contract.ZhiHuContract;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.Toaster;
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class ZhiHuFragment extends RecyclerPageFragment<
        ZhiHu.News, ZhiHuContract.News.View, ZhiHuContract.News.Presenter>
        implements ZhiHuContract.News.View {

    @Override
    public ZhiHuContract.News.Presenter createPresenter() {
        return new ZhiHuContract.News.Presenter();
    }

    @Override
    protected BaseAdapter setupAdapter() {
        ListAdapter adapter = new ListAdapter(mData);
        adapter.setOnRecyclerViewItemClickListener((view, pos) -> {
            SimpleApp.getAppComponent().getZhiHuService().getNewsDetail(mData.get(pos).id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsDetail -> {
                        UserCase.showWebView(newsDetail.share_url);
                    }, error -> {
                        Toaster.quick(error.getMessage());
                    });
        });
        return adapter;
    }

    class ListAdapter extends BaseAdapter<ZhiHu.News> {

        public ListAdapter(List<ZhiHu.News> data) {
            super(R.layout.item_image_text_2, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ZhiHu.News news) {
            final String title = news.title;
            final String imageUrl = news.images.get(0);

            UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), imageUrl);
            baseViewHolder.setText(R.id.primaryText, title);
            baseViewHolder.setText(R.id.secondaryText, DateUtils.dateToString(news.date));
        }
    }
}
