package com.yekong.droid.simpleapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yekong.droid.simpleapp.ui.base.RecyclerPageFragment;

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
        String title = getPageTitle(position).toString();
        RecyclerPageFragment fragment = FragmentMapper.titleToFragment(title);
        if (fragment != null) {
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
