package com.yekong.droid.simpleapp.multitype;

import android.support.annotation.NonNull;

import me.drakeet.multitype.Item;

/**
 * Created by baoxiehao on 16/11/26.
 */
public class News implements Item {
    @NonNull
    public String title;
    @NonNull public String subTitle;
    @NonNull public String url;

    public News(@NonNull final String title,
                @NonNull final String subTitle,
                @NonNull final String url) {
        this.title = title;
        this.subTitle = subTitle;
        this.url = url;
    }
}