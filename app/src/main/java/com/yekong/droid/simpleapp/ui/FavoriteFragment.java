package com.yekong.droid.simpleapp.ui;


import android.support.v7.widget.RecyclerView;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.multitype.Favorite;
import com.yekong.droid.simpleapp.multitype.FavoriteViewProvider;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class FavoriteFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mList;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void onDataLoaded() {
        Items items = new Items();
        for (int i = 0; i < 20; i++) {
            items.add(new Favorite("Favorite title" + i, "subTitle" + i, "url" + i));
        }
        MultiTypeAdapter adapter = new MultiTypeAdapter(items);
        adapter.register(Favorite.class, new FavoriteViewProvider());
        mList.setAdapter(adapter);
    }
}
