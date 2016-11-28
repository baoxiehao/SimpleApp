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
import com.yekong.droid.simpleapp.util.EventUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/26.
 */

public abstract class RecyclerPageFragment<M, V extends BaseView<M>, P extends BasePagePresenter<V, M>>
        extends MvpLceViewStateFragment<MaterialRefreshLayout, M, V, P> {

    @BindView(R.id.contentView)
    MaterialRefreshLayout mSwipeLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    protected M mData;
    protected MultiTypeAdapter mAdapter;

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
        initSwipeLayout();
    }

    private void initSwipeLayout() {
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
    public LceViewState<M, V> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public M getData() {
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
    public void setData(M data) {
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
    public void onEventMainThread(EventUtils.TopEvent event) {
        LinearLayoutManager llm = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        llm.scrollToPosition(0);
    }

    protected abstract MultiTypeAdapter setupAdapter();
}
