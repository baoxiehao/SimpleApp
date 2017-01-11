package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.mvp.presenter.BasePagePresenter;
import com.yekong.droid.simpleapp.mvp.view.BaseView;
import com.yekong.droid.simpleapp.util.Eventer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baoxiehao on 16/11/26.
 */

public abstract class RecyclerPageFragment<M, V extends BaseView<List<M>>, P extends BasePagePresenter<V, M>>
        extends MvpLceViewStateFragment<MaterialRefreshLayout, List<M>, V, P> {

    @BindView(R.id.contentView)
    MaterialRefreshLayout mSwipeLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    protected List<M> mData;
    protected BaseAdapter mAdapter;
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
        mSwipeLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                presenter.onRefreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.onLoadMoreData();
            }
        });
    }

    @Override
    public LceViewState<List<M>, V> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
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
        mSwipeLayout.finishRefresh();
        mSwipeLayout.finishRefreshLoadMore();
        this.showContent();

        LinearLayoutManager llm = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        final int firstPosition = llm.findFirstVisibleItemPosition();
        View startView = mRecyclerView.getChildAt(0);
        final int firstTop = (startView == null) ? 0 : (startView.getTop() - mRecyclerView.getPaddingTop());

        mAdapter = setupAdapter();
        mRecyclerView.setAdapter(mAdapter);

        llm.scrollToPositionWithOffset(firstPosition, firstTop);
    }

    @Subscribe
    public void onEventMainThread(Eventer.TopEvent event) {
        LinearLayoutManager llm = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        llm.scrollToPosition(0);
    }

    @Subscribe
    public void onEventMainThread(Eventer.PositionEvent event) {
        if (!mShowingDetail) {
            return;
        }
        LinearLayoutManager llm = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        llm.scrollToPosition(event.pos);
        mShowingDetail = false;
    }

    protected abstract BaseAdapter setupAdapter();
}
