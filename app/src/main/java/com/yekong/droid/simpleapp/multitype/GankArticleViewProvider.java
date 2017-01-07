package com.yekong.droid.simpleapp.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by baoxiehao on 16/11/28.
 */
public class GankArticleViewProvider
        extends ItemViewProvider<Gank.Article, GankArticleViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_text_2, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull Gank.Article data) {
        holder.mPrimaryText.setText(data.desc);
        holder.mSecondaryText.setText(DateUtils.dateToString(data.publishedAt));
        holder.mPrimaryText.getRootView().setOnClickListener((view) -> UserCase.showWebView(data.url));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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