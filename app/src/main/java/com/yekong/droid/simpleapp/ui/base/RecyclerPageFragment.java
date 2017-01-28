package com.yekong.droid.simpleapp.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.lcodecore.tkrefreshlayout.Footer.BottomProgressView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.EventUtils;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.Toaster;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baoxiehao on 16/11/26.
 */

public abstract class RecyclerPageFragment<M, V extends BaseView<List<M>>, P extends BasePagePresenter<V, M>>
        extends MvpLceViewStateFragment<TwinklingRefreshLayout, List<M>, V, P> {

    @BindView(R.id.contentView)
    TwinklingRefreshLayout mSwipeLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    protected List<M> mData;
    protected boolean mShowingDetail;

    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();
    }

    private void initViews() {
        mSwipeLayout.setAutoLoadMore(true);

        final ProgressLayout headerView = new ProgressLayout(getActivity());
        headerView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        headerView.setColorSchemeResources(R.color.primary);
        mSwipeLayout.setHeaderView(headerView);
        mSwipeLayout.setHeaderHeight(64);

        final BottomProgressView bottomView = new BottomProgressView(getActivity());
        bottomView.setNormalColor(getResources().getColor(R.color.primary));
        bottomView.setAnimatingColor(getResources().getColor(R.color.primary));
        mSwipeLayout.setBottomView(bottomView);
        mSwipeLayout.setBottomHeight(48);

        mSwipeLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                if (!presenter.onRefreshData()) {
                    new Handler().postDelayed(() -> {
                        mSwipeLayout.finishRefreshing();
                        Toaster.quick("Failed to refresh...");
                    }, 500);
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (!presenter.onLoadMoreData()) {
                    new Handler().postDelayed(() -> {
                        mSwipeLayout.finishLoadmore();
                        Toaster.quick("No more to load...");
                    }, 500);
                }
            }
        });
    }

    @Override
    public LceViewState<List<M>, V> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Logger.d("loadData(): pullToRefresh = %s", pullToRefresh);
        super.showLoading(pullToRefresh);
        super.presenter.onRefreshData();
    }

    @Override
    public List<M> getData() {
        return mData;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return getString(R.string.mvp_error_tip);
    }

    @Override
    protected void onErrorViewClicked() {
        loadData(true);
    }

    @Override
    public void setData(List<M> data) {
        mData = data;
        mSwipeLayout.finishRefreshing();
        mSwipeLayout.finishLoadmore();
        this.showContent();

        int firstPosition = 0;
        int firstTop = 0;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            View startView = mRecyclerView.getChildAt(0);
            firstTop = (startView == null) ? 0 : (startView.getTop() - mRecyclerView.getPaddingTop());
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(firstPosition, firstTop);
        } else if (layoutManager instanceof GridLayoutManager) {
            firstPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            View startView = mRecyclerView.getChildAt(0);
            firstTop = (startView == null) ? 0 : (startView.getTop() - mRecyclerView.getPaddingTop());
            ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(firstPosition, firstTop);
        }

        layoutManager = setupLayoutManager();
        RecyclerView.Adapter adapter = setupAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(firstPosition, firstTop);
        } else if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(firstPosition, firstTop);
        }
    }

    @Subscribe
    public void onEventMainThread(EventUtils.TopEvent event) {
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    @Subscribe
    public void onEventMainThread(EventUtils.PositionEvent event) {
        if (!mShowingDetail) {
            return;
        }
        mRecyclerView.getLayoutManager().scrollToPosition(event.pos);
        mShowingDetail = false;
    }

    protected abstract BaseAdapter setupAdapter();

    protected RecyclerView.LayoutManager setupLayoutManager() {
        return new LinearLayoutManager(getContext());
    }
}
