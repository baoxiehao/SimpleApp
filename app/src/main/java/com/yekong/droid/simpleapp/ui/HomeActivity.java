package com.yekong.droid.simpleapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.ogaclejapan.arclayout.ArcLayout;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.ui.base.BaseActivity;
import com.yekong.droid.simpleapp.util.ArcLayoutUtils;
import com.yekong.droid.simpleapp.util.EventUtils;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.QiniuUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    FragmentAdapter mAdapter;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @BindView(R.id.bottomNavView)
    BottomNavigationView mBottomNavView;

    @BindView(R.id.menu_layout)
    View mMenuLayout;

    @BindView(R.id.arc_layout)
    ArcLayout mArcLayout;

    int mReadIndex;
    int mGalleryIndex;
    String[] mBucketPrefixes;

    @Override
    protected void onFirstFrameDisplayed() {
        super.onFirstFrameDisplayed();
        Logger.d("onFirstFrameDisplayed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        initDrawerLayout();
        initNavView();
        initViewPager();
        initBottomNavView();
        initArcLayout();
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            View navAuthorContainer = mNavView.findViewById(R.id.nav_author_container);
            if (navAuthorContainer != null) {
                navAuthorContainer.setOnClickListener(v -> {
                    UserCase.showWebView(getString(R.string.nav_author_desc));
                    new Handler().postDelayed(() -> mDrawerLayout.closeDrawer(GravityCompat.START), 200);
                });
            }
        });
    }

    private void initNavView() {
        mNavView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_news) {
                mBottomNavView.selectTab(0);
            } else if (id == R.id.nav_pics) {
                mBottomNavView.selectTab(1);
            } else if (id == R.id.nav_share) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://fir.im/simpleapp");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Share SimpleApp"));
            } else if (id == R.id.nav_setting) {
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initViewPager() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(mViewPager);
        }
        showReadFragments();
    }

    private void initBottomNavView() {
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_read),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_news)
        );
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_gallery),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_pics)
        );
        mBottomNavView.setOnBottomNavigationItemClickListener((index) -> {
            if (index == 0) {
                showReadFragments();
            } else if (index == 1) {
                showGalleryFragments();
            }
        });
        mBottomNavView.selectTab(0);
    }

    private void initArcLayout() {
        mMenuLayout.setOnTouchListener((view, motionEvent) -> {
                if (mMenuLayout.getVisibility() != View.INVISIBLE) {
                    if (mFab.isSelected()) {
                        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
                    }
                }
                return false;
            }
        );
    }

    private void showReadFragments() {
        mGalleryIndex = mViewPager.getCurrentItem();

        mAdapter.addFragments(
                getString(R.string.fragment_title_zhihu),
                getString(R.string.fragment_title_blog),
                getString(R.string.fragment_title_tech),
                getString(R.string.fragment_title_gank),
                getString(R.string.fragment_title_android));
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mReadIndex);
    }

    private void showGalleryFragments() {
        mReadIndex = mViewPager.getCurrentItem();

        if (mBucketPrefixes != null) {
            mAdapter.addFragments(mBucketPrefixes);
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mGalleryIndex);
        } else {
            QiniuUtils.parseBucketPrefixes()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bucketPrefixes -> {
                        mBucketPrefixes = bucketPrefixes.toArray(new String[0]);
                        mAdapter.addFragments(mBucketPrefixes);
                        mAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mGalleryIndex);
                    });
        }
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
//        Snackbar.make(mFab, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", view -> Toaster.quick("Action done")).show();
    }

    @OnClick(R.id.fab_up)
    void onFabUpClick() {
        EventUtils.TopEvent.send();
        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
    }

    @OnClick(R.id.fab_search)
    void onFabSearchClick() {
        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
    }

    @OnClick(R.id.fab_favorite)
    void onFabFavoriteClick() {
        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
    }

    @OnClick(R.id.fab_setting)
    void onFabSettingClick() {
        ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
