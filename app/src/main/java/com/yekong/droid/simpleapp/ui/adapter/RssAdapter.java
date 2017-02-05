package com.yekong.droid.simpleapp.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.model.RssItem;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.util.UiUtils;

import java.util.List;

/**
 * Created by baoxiehao on 17/2/5.
 */

public class RssAdapter extends BaseAdapter<RssItem> {

    public RssAdapter(List<RssItem> data) {
        super(R.layout.item_image_text_3, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RssItem rssItem) {
        UiUtils.loadImage(baseViewHolder.getView(R.id.imageView), rssItem.image);
        final String primaryText = rssItem.title;
        final String secondaryText = rssItem.desc;
        String ternaryText = rssItem.time;
        if (!TextUtils.isEmpty(ternaryText) && !TextUtils.isEmpty(rssItem.source)) {
            ternaryText += String.format(" @ %s", rssItem.source);
        }

        baseViewHolder.setText(R.id.primaryText, primaryText);
        if (!TextUtils.isEmpty(secondaryText)) {
            baseViewHolder.getView(R.id.secondaryText).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.secondaryText, secondaryText);
        } else {
            baseViewHolder.getView(R.id.secondaryText).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(ternaryText)) {
            baseViewHolder.getView(R.id.ternaryText).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.ternaryText, ternaryText);
        } else {
            baseViewHolder.getView(R.id.ternaryText).setVisibility(View.GONE);
        }
    }
}
