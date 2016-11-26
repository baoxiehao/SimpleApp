package com.yekong.droid.simpleapp.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.multitype.News;
import com.yekong.droid.simpleapp.multitype.NewsViewProvider;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


public class NewsFragment extends BaseFragment {

    private static final String ARG_TITLE = "ARG_TITLE";

    private String mTitle;

    @BindView(R.id.list)
    RecyclerView mList;

    public NewsFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("unused")
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void onDataLoaded() {
        Items items = new Items();
        for (int i = 0; i < 20; i++) {
            items.add(new News("News title" + i, "subTitle" + i, "url" + i));
        }
        MultiTypeAdapter adapter = new MultiTypeAdapter(items);
        adapter.register(News.class, new NewsViewProvider());
        mList.setAdapter(adapter);
    }
}
