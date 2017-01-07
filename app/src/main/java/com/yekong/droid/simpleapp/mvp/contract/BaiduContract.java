package com.yekong.droid.simpleapp.mvp.contract;

import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.multitype.Baidu;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 16/11/27.
 */

public interface BaiduContract {

    interface Image {
        interface View extends BaseView<List<Baidu.Image>> {
        }

        class Presenter extends BasePagePresenter<Image.View, Baidu.Image> {

            private static final int PAGE_SIZE = 10;

            private static final String[] COLS = new String[]{
                    "美女"
            };

            private static final String[] TAGS = new String[]{
                    "性感", "大胸", "清纯", "韩国", "长腿", "长发",
                    "日本", "可爱", "熟女", "私房", "写真", "街拍",
                    "时尚", "美臀", "美眉", "自拍", "气质", "甜美"
            };

            private void updateSearchArgs(boolean refresh) {
                if (refresh) {
                    int index = new Random().nextInt(COLS.length);
                    mCol = COLS[index];
                    index = new Random().nextInt(TAGS.length);
                    mTag = TAGS[index];

                    mPage = 1;
                    if (mTotalNums.containsKey(mCol + mTag)) {
                        mTotalNum = mTotalNums.get(mCol + mTag);
                        mStartIndex = new Random().nextInt(mTotalNum - PAGE_SIZE + 1);
                    } else {
                        mStartIndex = 1;
                    }
                } else {
                    mPage += 1;
                    mStartIndex = (mPage - 1) * PAGE_SIZE + 1;
                }
            }

            private String mCol;
            private String mTag;
            private int mTotalNum;
            private int mStartIndex;
            private Map<String, Integer> mTotalNums = new HashMap<>();

            void searchImages(String col, String tag, int startIndex, int returnNumber) {
                SimpleApp.getAppComponent().getBaiduService().searchImages(col, tag, startIndex, returnNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> {
                            mTotalNums.put(col + tag, response.totalNum);
                            List<Baidu.Image> imgs = new ArrayList<Baidu.Image>();
                            for (Baidu.Image img : response.imgs) {
                                if (img.thumbnailUrl != null && img.imageUrl != null) {
                                    imgs.add(img);
                                }
                            }
                            return imgs;
                        })
                        .subscribe(imgs -> {
                            final int page = (startIndex - 1) / PAGE_SIZE + 1;
                            onPageData(super.getPageKey(page), imgs);
                        }, error -> {
                            onPageError(error);
                        });
            }

            @Override
            public void onRefreshData() {
                super.onRefreshData();
                updateSearchArgs(true);
                searchImages(mCol, mTag, mStartIndex, PAGE_SIZE);
            }

            @Override
            public void onLoadMoreData() {
                super.onLoadMoreData();
                updateSearchArgs(false);
                searchImages(mCol, mTag, mStartIndex, PAGE_SIZE);
            }
        }
    }
}
