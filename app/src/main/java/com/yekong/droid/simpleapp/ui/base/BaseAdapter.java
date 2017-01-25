package com.yekong.droid.simpleapp.ui.base;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by baoxiehao on 17/1/10.
 */

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T> {
    public BaseAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }
}
