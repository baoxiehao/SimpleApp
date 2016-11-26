package com.yekong.droid.simpleapp.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yekong.droid.simpleapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by baoxiehao on 16/11/26.
 */
public class NewsViewProvider
        extends ItemViewProvider<News, NewsViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull News news) {
        holder.mTitleText.setText(news.title);
        holder.mSubTitleText.setText(news.subTitle);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView mTitleText;
        @BindView(R.id.sub_title) TextView mSubTitleText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}