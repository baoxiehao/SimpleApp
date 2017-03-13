package com.yekong.droid.simpleapp.ui;

import android.content.Context;
import android.text.TextUtils;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baoxiehao on 17/2/4.
 */

public class FragmentMapper {

    private static Map<String, String> DIY_CODE_TITLE_PATH_MAPPING = new HashMap<>();
    static {
        final Context context = SimpleApp.getAppComponent().getContext();
        DIY_CODE_TITLE_PATH_MAPPING.put(context.getString(R.string.fragment_title_diycode_topics), "topics");
        DIY_CODE_TITLE_PATH_MAPPING.put(context.getString(R.string.fragment_title_diycode_projects), "projects");
        DIY_CODE_TITLE_PATH_MAPPING.put(context.getString(R.string.fragment_title_diycode_news), "news");
        DIY_CODE_TITLE_PATH_MAPPING.put(context.getString(R.string.fragment_title_diycode_github), "trends/Android");
        DIY_CODE_TITLE_PATH_MAPPING.put(context.getString(R.string.fragment_title_diycode_sites), "sites");
    }

    private FragmentMapper() {}

    public static RecyclerPageFragment titleToFragment(String title) {
        final Context context = SimpleApp.getAppComponent().getContext();
        RecyclerPageFragment fragment;
        if (TextUtils.equals(context.getString(R.string.fragment_title_zhihu), title)) {
            fragment = new ZhiHuFragment();
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_tech), title)) {
            fragment = new TechFragment();
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_blog), title)) {
            fragment = new BlogFragment();
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_gank), title)) {
            fragment = new GankFragment();
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_android), title)) {
            fragment = new AndroidFragment();
        } else if (DIY_CODE_TITLE_PATH_MAPPING.keySet().contains(title)) {
            if (context.getString(R.string.fragment_title_diycode_sites).equals(title)) {
                fragment = new DiyCodeGridFragmentBuilder(DIY_CODE_TITLE_PATH_MAPPING.get(title)).build();
            } else {
                fragment = new DiyCodeFragmentBuilder(DIY_CODE_TITLE_PATH_MAPPING.get(title)).build();
            }
        } else {
            fragment = new ImageFragmentBuilder(title).build();
        }
        return fragment;
    }
}
