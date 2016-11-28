package com.yekong.droid.simpleapp.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.util.UiUtils;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by baoxiehao on 16/11/27.
 */
public class BaiduImageViewProvider
        extends ItemViewProvider<BaiduImage.Entity, BaiduImageViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_image_title, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull BaiduImage.Entity baiduImage) {
        holder.mTitleText.setText(String.format("%s %s", DateUtils.dateToString(baiduImage.date), baiduImage.title));
        UiUtils.loadImage(holder.mImageView, baiduImage.imageUrl);

        holder.mImageView.setOnClickListener(view -> UserCase.showImageFullscreen(baiduImage.title, baiduImage.imageUrl));
        holder.mTitleText.setOnClickListener(view -> UserCase.showWebView(baiduImage.fromUrl));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleText)
        TextView mTitleText;

        @BindView(R.id.imageView)
        ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}