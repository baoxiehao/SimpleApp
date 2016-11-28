package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.multitype.GithubRepo;
import com.yekong.droid.simpleapp.multitype.GithubRepoViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GithubContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/26.
 */

public class GithubFragment extends MvpLceViewStateFragment<MaterialRefreshLayout,
        List<GithubRepo>, GithubContract.View, GithubContract.Presenter>
        implements GithubContract.View {

    @BindView(R.id.contentView)
    MaterialRefreshLayout mSwipeLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    MultiTypeAdapter mAdapter;
    List<GithubRepo> mUserRepos;
    int mPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_github, container, false);
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initSwipeLayout();
    }

    @Override
    public LceViewState<List<GithubRepo>, GithubContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<GithubRepo> getData() {
        return mUserRepos;
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
    public GithubContract.Presenter createPresenter() {
        return new GithubContract.Presenter();
    }

    @Override
    public void setData(List<GithubRepo> data) {
        mUserRepos = data;
        if (mUserRepos.isEmpty()) {
            this.showError(new Throwable("No data"), false);
            return;
        } else {
            setupAdapter();
            mSwipeLayout.finishRefresh();
            mSwipeLayout.finishRefreshLoadMore();
            this.showContent();
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        this.showLoading(pullToRefresh);
//        this.presenter.loadUserRepos(mPage);
        this.presenter.searchRepos("Android");
    }

    @Override
    public void onUserRepos(int page, List<GithubRepo> repos) {
        if (page == 1) {
            setData(repos);
        } else {
            List<GithubRepo> newUserRepos = new ArrayList<>(mUserRepos);
            newUserRepos.addAll(repos);
            setData(newUserRepos);
        }
    }

    @Override
    public void onSearchRepos(List<GithubRepo> repos) {
        setData(repos);
    }

    private void initSwipeLayout() {
        mSwipeLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mPage = 1;
                loadData(true);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mPage += 1;
                loadData(true);
            }
        });
    }

    private void setupAdapter() {
        Items items = new Items();
        items.addAll(mUserRepos);
        mAdapter = new MultiTypeAdapter(items);
        mAdapter.register(GithubRepo.class, new GithubRepoViewProvider());
        mRecyclerView.setAdapter(mAdapter);
    }
}
