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
        View root = inflater.inflate(R.layout.item_gank_article, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull Gank.Article gankArticle) {
        holder.mTitleText.setText(gankArticle.desc);
        holder.mPublishedAtText.setText(DateUtils.dateToString(gankArticle.publishedAt));
        holder.mTitleText.getRootView().setOnClickListener((view) -> UserCase.showWebView(gankArticle.url));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleText)
        TextView mTitleText;

        @BindView(R.id.publishedAtText)
        TextView mPublishedAtText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}