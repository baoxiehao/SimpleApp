package com.yekong.droid.simpleapp.util;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.yekong.droid.simpleapp.app.SimpleApp;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

/**
 * Created by baoxiehao on 16/11/29.
 */

public class UiUtils {

    public static void loadImage(ImageView imageView, final String imageUrl) {
        loadImage(imageView, imageUrl, Priority.NORMAL);
    }

    public static void loadImage(ImageView imageView, final String imageUrl, final Priority priority) {
        Glide.with(SimpleApp.getAppComponent().getContext())
                .load(Uri.parse(imageUrl))
                .priority(priority)
                .into(imageView);    }

    public static void loadImage(ImageView imageView, final String imageUrl, final Priority priority, RequestListener listener) {
        Glide.with(SimpleApp.getAppComponent().getContext())
                .load(Uri.parse(imageUrl))
                .listener(listener)
                .priority(priority)
                .into(imageView);
    }

    public static void enterFullScreen(View decorView) {
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void enterFullscreenWithActionBar(View decorView) {
        decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
