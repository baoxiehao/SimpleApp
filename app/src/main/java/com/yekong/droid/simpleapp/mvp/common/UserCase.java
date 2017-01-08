package com.yekong.droid.simpleapp.mvp.common;

import android.content.Intent;

import com.thefinestartist.finestwebview.FinestWebView;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.ui.ImageActivity;

import java.util.ArrayList;

/**
 * Created by baoxiehao on 16/11/29.
 */

public class UserCase {

    public static void showImages(ArrayList<String> titles, ArrayList<String> urls, int position) {
        Intent intent = new Intent(ImageActivity.ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putStringArrayListExtra(ImageActivity.EXTRA_TITLES, titles);
        intent.putStringArrayListExtra(ImageActivity.EXTRA_URLS, urls);
        intent.putExtra(ImageActivity.EXTRA_POS, position);
        SimpleApp.getAppComponent().getContext().startActivity(intent);
    }

    public static void showWebView(final String url) {
        new FinestWebView.Builder(SimpleApp.getAppComponent().getContext())
                .iconDefaultColorRes(R.color.icon)
                .progressBarColorRes(R.color.icon)
                .stringResRefresh(R.string.web_refresh)
                .stringResShareVia(R.string.web_share)
                .stringResCopyLink(R.string.web_copy_link)
                .stringResOpenWith(R.string.web_open_with)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(url);
    }
}
