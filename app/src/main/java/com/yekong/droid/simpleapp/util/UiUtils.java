package com.yekong.droid.simpleapp.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yekong.droid.simpleapp.app.SimpleApp;

/**
 * Created by baoxiehao on 16/11/29.
 */

public class UiUtils {

    public static void loadImage(ImageView imageView, final String imageUrl) {
        Glide.with(SimpleApp.getAppComponent().getContext())
                .load(Uri.parse(imageUrl))
                .into(imageView);
    }

}
