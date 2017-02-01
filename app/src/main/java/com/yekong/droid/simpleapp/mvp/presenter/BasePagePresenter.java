package com.yekong.droid.simpleapp.mvp.presenter;

import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.Toaster;

import org.jsoup.HttpStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mi on 16-11-29.
 */

public abstract class BasePagePresenter<V extends BaseView, M> extends BasePresenter<V> {

    private List<M> mData = new ArrayList<>();

    protected List<String> mPages = new ArrayList<>();

    protected HashMap<String, List<M>> mPageDataMap = new HashMap<>();

    /**
     * Should be overridden by non numeric page,
     * so you should override this method when overriding isNumericPage() which returns false
     */
    protected String getPageKey(Object... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No page key arguments!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(arg != null ? arg.toString() : "null");
            }
            return sb.toString();
        }
    }

    protected void updatePageKey(String page) {
        updatePageKey(page, true);
    }

    protected void updatePageKey(String page, boolean appendIfMissing) {
        final int index = mPages.indexOf(page);
        mPages.remove(page);
        if (index < 0) {
            if (appendIfMissing) {
                mPages.add(page);
            } else {
                mPages.add(0, page);
            }
        } else {
            mPages.add(index, page);
        }
    }

    public boolean onRefreshData() {
        Logger.d("onRefreshData(): tag=%s", tag());
        return false;
    }

    public boolean onLoadMoreData() {
        Logger.d("onLoadMoreData(): tag=%s", tag());
        return false;
    }

    protected void onPageData(String page, List<M> data) {
        Logger.d("onPageData(): tag=%s, page=%s, count=%s", tag(), page, data.size());
        mPageDataMap.put(page, data);
        updateData();
        showDataOrError(new Exception("No Data"));
    }

    protected void onPageError(Throwable throwable) {
        Logger.e(String.format("onPageError(): tag=%s", tag()), throwable);
        showDataOrError(throwable);
    }

    private void updateData() {
        mData.clear();
        for (String p : mPages) {
            List<M> pageData = mPageDataMap.get(p);
            Logger.d("onPageData(): tag=%s, page=%s, count=%s", tag(), p, pageData != null ? pageData.size() : 0);
            if (pageData != null) {
                mData.addAll(pageData);
            }
        }
    }

    private void showDataOrError(Throwable throwable) {
        BaseView view = getView();
        if (view != null) {
            if (!mData.isEmpty()) {
                view.setData(mData);
            } else {
                if (throwable instanceof HttpStatusException) {
                    onLoadMoreData();
                } else {
                    Toaster.quick(throwable.toString());
                    view.showError(throwable, false);
                }
            }
        }
    }

    private String tag() {
        String tag = getClass().toString();
        return tag.substring(tag.lastIndexOf(".") + 1);
    }
}
