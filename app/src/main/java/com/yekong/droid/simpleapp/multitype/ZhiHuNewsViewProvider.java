package com.yekong.droid.simpleapp.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.util.UiUtils;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.util.DateUtils;
import com.yekong.droid.simpleapp.util.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/26.
 */
public class ZhiHuNewsViewProvider
        extends ItemViewProvider<ZhiHu.News, ZhiHuNewsViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_image_text_2, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull ZhiHu.News data) {
        final String title = data.title;
        final String imageUrl = data.images.get(0);

        UiUtils.loadImage(holder.mImageView, imageUrl);
        holder.mPrimaryText.setText(title);
        holder.mSecondaryText.setText(DateUtils.dateToString(data.date));

        holder.mImageView.getRootView().setOnClickListener((view) -> {
            SimpleApp.getAppComponent().getZhiHuService().getNewsDetail(data.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsDetail -> {
                        UserCase.showWebView(newsDetail.share_url);
                    }, error -> {
                        Toaster.quick(error.getMessage());
                    });
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView mImageView;

        @BindView(R.id.primaryText)
        TextView mPrimaryText;

        @BindView(R.id.secondaryText)
        TextView mSecondaryText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}