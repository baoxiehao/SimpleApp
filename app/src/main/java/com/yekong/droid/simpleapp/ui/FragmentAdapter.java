package com.yekong.droid.simpleapp.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoxiehao on 16/1/29.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private final List<String> mTitles = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(String... titles) {
        mTitles.clear();
        for (String title : titles) {
            mTitles.add(title);
        }
    }

    @Override
    public Fragment getItem(int position) {
        final Context context = SimpleApp.getAppComponent().getContext();
        String title = getPageTitle(position).toString();
        if (TextUtils.equals(context.getString(R.string.fragment_title_zhihu), title)) {
            RecyclerPageFragment fragment = new ZhiHuFragment();
            fragment.setTitle(title);
            return fragment;
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_gank), title)) {
            RecyclerPageFragment fragment = new GankArticleFragment();
            fragment.setTitle(title);
            return fragment;
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_fuli), title)) {
            RecyclerPageFragment fragment = new GankFuliFragment();
            fragment.setTitle(title);
            return fragment;
        } else if (TextUtils.equals(context.getString(R.string.fragment_title_baidu), title)) {
            RecyclerPageFragment fragment = new BaiduImageFragment();
            fragment.setTitle(title);
            return fragment;
        }
        throw new IllegalArgumentException("Unknown position " + position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        RecyclerPageFragment fragment = (RecyclerPageFragment) object;
        String title = fragment.getTitle();
        int position = mTitles.indexOf(title);
        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }
}
