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
 * Created by baoxiehao on 16/11/28.
 */
public class GankFuliViewProvider
        extends ItemViewProvider<Gank.Fuli, GankFuliViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_image_title, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull Gank.Fuli gankFuli) {
        holder.mTitleText.setText(String.format("%s %s", DateUtils.dateToString(gankFuli.publishedAt), gankFuli.type));
        UiUtils.loadImage(holder.mImageView, gankFuli.url);

        holder.mImageView.setOnClickListener((view) -> UserCase.showImageFullscreen(gankFuli.desc, gankFuli.url));
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