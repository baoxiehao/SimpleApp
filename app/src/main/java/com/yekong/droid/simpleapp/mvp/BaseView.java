package com.yekong.droid.simpleapp.mvp;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by baoxiehao on 16/11/28.
 */

public interface BaseView<M> extends MvpLceView<M> {
    void onPageData(int page, M data);
}
