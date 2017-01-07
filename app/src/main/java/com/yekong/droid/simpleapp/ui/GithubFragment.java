package com.yekong.droid.simpleapp.ui;

import com.yekong.droid.simpleapp.multitype.GithubRepo;
import com.yekong.droid.simpleapp.multitype.GithubRepoViewProvider;
import com.yekong.droid.simpleapp.mvp.contract.GithubContract;

import java.util.ArrayList;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by baoxiehao on 16/11/28.
 */

public class GithubFragment extends RecyclerPageFragment<
        GithubRepo, GithubContract.View, GithubContract.Presenter>
        implements GithubContract.View {

    @Override
    public GithubContract.Presenter createPresenter() {
        return new GithubContract.Presenter();
    }

    @Override
    protected MultiTypeAdapter setupAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData != null ? mData : new ArrayList<GithubRepo>());
        adapter.register(GithubRepo.class, new GithubRepoViewProvider());
        return adapter;
    }
}
