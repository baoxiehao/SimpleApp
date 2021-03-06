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
import android.view.MotionEvent;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.ogaclejapan.arclayout.ArcLayout;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.cache.CacheManager;
import com.yekong.droid.simpleapp.mvp.common.UserCase;
import com.yekong.droid.simpleapp.ui.base.BaseActivity;
import com.yekong.droid.simpleapp.util.ArcLayoutUtils;
import com.yekong.droid.simpleapp.util.Constants;
import com.yekong.droid.simpleapp.util.EventUtils;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.QiniuUtils;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    final static int NAV_COUNT = 4;
    final static int INDEX_INFO = 0;
    final static int INDEX_CODE = 1;
    final static int INDEX_GANK = 2;
    final static int INDEX_PICS = 3;

    int[] mIndexes = new int[NAV_COUNT];
    List<String> mBucketPrefixes;

    @Override
    protected void onFirstFrameDisplayed() {
        super.onFirstFrameDisplayed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_HomeActivity);
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
            View navImageView = mNavView.findViewById(R.id.navImageView);
            if (navImageView != null) {
                addSubscription(RxView.clicks(navImageView)
                        .buffer(1, TimeUnit.SECONDS)
                        .filter(clicks -> clicks.size() > 0)
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(clicks -> {
                            if (clicks.size() >= 5) {
                                if (CacheManager.contains(Constants.PREF_KEY_FULI)) {
                                    Boolean containsFuli = CacheManager.get(Constants.PREF_KEY_FULI);
                                    if (containsFuli) {
                                        Toaster.quick(getString(R.string.toast_fuli_turned_off));
                                        CacheManager.put(Constants.PREF_KEY_FULI, false);
                                    } else {
                                        Toaster.quick(getString(R.string.toast_fuli_turned_on));
                                        CacheManager.put(Constants.PREF_KEY_FULI, true);
                                    }
                                } else {
                                    Toaster.quick(getString(R.string.toast_fuli_turned_on));
                                    CacheManager.put(Constants.PREF_KEY_FULI, true);
                                }
                            }
                        }));
            }

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

            if (id == R.id.nav_info) {
                mBottomNavView.selectTab(INDEX_INFO);
            } else if (id == R.id.nav_pics) {
                mBottomNavView.selectTab(INDEX_PICS);
            } else if (id == R.id.nav_code) {
                mBottomNavView.selectTab(INDEX_CODE);
            } else if (id == R.id.nav_gank) {
                mBottomNavView.selectTab(INDEX_GANK);
            } else if (id == R.id.nav_share) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_app_download));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getString(R.string.dialog_title_share_app)));
            } else if (id == R.id.nav_setting) {
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initViewPager() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndexes[mBottomNavView.getCurrentItem()] = mViewPager.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        showInfoFragments();
    }

    private void initBottomNavView() {
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_info),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_info)
        );
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_code),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_code)
        );
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_gank),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_gank)
        );
        mBottomNavView.addTab(new BottomNavigationItem(
                getString(R.string.nav_menu_pics),
                getResources().getColor(R.color.primary),
                R.drawable.ic_nav_bottom_pics)
        );
        mBottomNavView.setOnBottomNavigationItemClickListener((index) -> {
            if (index == INDEX_INFO) {
                showInfoFragments();
            } else if (index == INDEX_CODE) {
                showCodeFragments();
            } else if (index == INDEX_GANK) {
                showGankFragments();
            } else if (index == INDEX_PICS) {
                showPicsFragments();
            }
        });
        mBottomNavView.selectTab(INDEX_INFO);
    }

    private void initArcLayout() {
        mFab.setOnTouchListener((view, motionEvent) -> {
            if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                if (mBottomNavView.getTranslationY() > 0) {
                    // If untouchable, return false to not to consume the following motion events
                    Logger.d("Fab is untouchable!");
                    return false;
                }
                return true;
            } else if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                ArcLayoutUtils.toggleArcLayout(mFab, mMenuLayout, mArcLayout);
            }
            return false;
        });
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

    private void showInfoFragments() {
        mAdapter.addFragments(
                getString(R.string.fragment_title_zhihu),
                getString(R.string.fragment_title_tech),
                getString(R.string.fragment_title_android),
                getString(R.string.fragment_title_blog));
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mIndexes[INDEX_INFO]);
    }

    private void showPicsFragments() {
        if (mBucketPrefixes != null) {
            if (CacheManager.contains(Constants.PREF_KEY_FULI)) {
                Boolean containsFuli = CacheManager.get(Constants.PREF_KEY_FULI);
                if (containsFuli) {
                    if (!mBucketPrefixes.contains(Constants.PREF_KEY_FULI)) {
                        mBucketPrefixes.add(0, Constants.PREF_KEY_FULI);
                    }
                } else {
                    mBucketPrefixes.remove(Constants.PREF_KEY_FULI);
                }
            }
            mAdapter.addFragments(mBucketPrefixes.toArray(new String[0]));
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mIndexes[INDEX_PICS]);
        } else {
            addDisposable(QiniuUtils.parseBucketPrefixes()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bucketPrefixes -> {
                        mBucketPrefixes = bucketPrefixes;
                        mAdapter.addFragments(mBucketPrefixes.toArray(new String[0]));
                        mAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mIndexes[INDEX_PICS]);
                    }));
        }
    }

    private void showCodeFragments() {
        mAdapter.addFragments(
                getString(R.string.fragment_title_diycode_topics),
                getString(R.string.fragment_title_diycode_projects),
                getString(R.string.fragment_title_diycode_news),
                getString(R.string.fragment_title_diycode_github),
                getString(R.string.fragment_title_diycode_sites));
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mIndexes[INDEX_CODE]);
    }

    private void showGankFragments() {
        mAdapter.addFragments(
                getString(R.string.fragment_title_gank),
                getString(R.string.fragment_title_fuli));
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mIndexes[INDEX_GANK]);
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
