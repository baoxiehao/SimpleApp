package com.yekong.droid.simpleapp.mvp.presenter;

import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mi on 16-11-29.
 */

public abstract class BasePagePresenter<V extends BaseView, M> extends BasePresenter<V> {

    protected List<M> mData = new ArrayList<>();

    protected int mPage = 1;

    protected List<String> mPages = new ArrayList<>();

    protected HashMap<String, List<M>> mPageDataMap = new HashMap<>();

    protected boolean isNumericPage() {
        return true;
    }

    /**
     * Should be overridden by non numeric page,
     * so you should override this method when overriding isNumericPage() which returns false
     */
    protected String getPageKey(Object... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No args, should be overridden");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(arg.toString());
            }
            return sb.toString();
        }
    }

    public void onRefreshData() {
        Logger.d("onRefreshData(): tag=%s", tag());
        if (isNumericPage()) {
            mPage = 1;
        } else {
            mPages.clear();
            mPages.add(getPageKey());
        }
    }

    public void onLoadMoreData() {
        Logger.d("onLoadMoreData(): tag=%s", tag());
        if (isNumericPage()) {
            mPage += 1;
        } else {
            mPages.add(getPageKey());
        }
    }

    protected void onPageData(String page, List<M> data) {
        Logger.d("onPageData(): tag=%s, numericPage=%s, page=%s, count=%s", tag(), isNumericPage(), page, data.size());
        mPageDataMap.put(page, data);
        mData.clear();
        if (isNumericPage()) {
            for (int i = 1; i <= mPage; i++) {
                List<M> pageData = mPageDataMap.get(getPageKey(i));
                Logger.d("onPageData(): tag=%s, page=%s, count=%s", tag(), i, pageData != null ? pageData.size() : 0);
                if (pageData != null) {
                    mData.addAll(pageData);
                }
            }
        } else {
            for (String p : mPages) {
                List<M> pageData = mPageDataMap.get(p);
                Logger.d("onPageData(): tag=%s, page=%s, count=%s", tag(), p, pageData != null ? pageData.size() : 0);
                if (pageData != null) {
                    mData.addAll(pageData);
                }
            }
        }
        showDataOrError(new Exception("No Data"));
    }

    protected void onPageError(Throwable throwable) {
        Logger.e(String.format("onPageError(): tag=%s", tag()), throwable);
        Toaster.quick(throwable.toString());
        showDataOrError(throwable);
    }

    private void showDataOrError(Throwable throwable) {
        BaseView view = getView();
        if (view != null) {
            if (!mData.isEmpty()) {
                view.setData(mData);
            } else {
                view.showError(throwable, false);
            }
        }
    }

    private String tag() {
        String tag = getClass().toString();
        return tag.substring(tag.lastIndexOf(".") + 1);
   }
}
