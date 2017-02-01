package com.yekong.droid.simpleapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arasthel.asyncjob.AsyncJob;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.ui.base.BaseActivity;
import com.yekong.droid.simpleapp.util.EventUtils;
import com.yekong.droid.simpleapp.util.Toaster;
import com.yekong.droid.simpleapp.util.UiUtils;
import com.yekong.droid.simpleapp.util.WebUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class ImageActivity extends BaseActivity {
    public static final String ACTION = "com.yekong.droid.simpleapp.action.VIEW_IMAGE";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URLS = "EXTRA_URLS";
    public static final String EXTRA_POS = "EXTRA_POS";

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 200;

    private final Handler mHideHandler = new Handler();

    private boolean mVisible;

    private String mTitle;
    private ArrayList<String> mUrls;
    private int mPos;

    @BindView(R.id.recyclerViewPager)
    RecyclerViewPager mRecyclerViewPager;

    @BindView(R.id.saveImageButton)
    Button mSaveImageButton;

    @BindView(R.id.bottom_controls)
    View mControlsBottomView;

    private final Runnable mEnterFullscreenRunnable = () -> {
        // Delayed removal of status and navigation bar
        UiUtils.enterFullScreen(getWindow().getDecorView());
    };

    private final Runnable mExitFullscreenRunnable = () -> {
        UiUtils.enterFullscreenWithActionBar(getWindow().getDecorView());
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        mControlsBottomView.setVisibility(View.VISIBLE);
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = (view, motionEvent) -> {
        if (AUTO_HIDE) {
            mHideHandler.postDelayed(() -> hide(), AUTO_HIDE_DELAY_MILLIS);
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        setupContentView();
    }

    private void setupContentView() {
        mVisible = true;

        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mUrls = getIntent().getStringArrayListExtra(EXTRA_URLS);
        mPos = getIntent().getIntExtra(EXTRA_POS, 0);

        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Make the action bar transparent
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_overlay_transparent));

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPager.setLayoutManager(layout);

        BaseQuickAdapter adapter = new ImageAdapter(mUrls);
        adapter.openLoadAnimation();
        mRecyclerViewPager.setAdapter(adapter);
        mRecyclerViewPager.addOnPageChangedListener((fromPos, toPos) -> {
            mPos = toPos;
        });
        mRecyclerViewPager.scrollToPosition(mPos);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        mSaveImageButton.setOnTouchListener(mDelayHideTouchListener);

        RxView.clicks(mSaveImageButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Void -> downloadImage(mUrls.get(mRecyclerViewPager.getCurrentPosition())));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsBottomView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mExitFullscreenRunnable);
        mHideHandler.postDelayed(mEnterFullscreenRunnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mRecyclerViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mEnterFullscreenRunnable);
        mHideHandler.postDelayed(mExitFullscreenRunnable, UI_ANIMATION_DELAY);

        mHideHandler.postDelayed(() -> hide(), UI_ANIMATION_DELAY + AUTO_HIDE_DELAY_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventUtils.PositionEvent.send(mRecyclerViewPager.getCurrentPosition());
    }

    class ImageAdapter extends BaseQuickAdapter<String> {

        public ImageAdapter(List<String> data) {
            super(R.layout.item_big_image, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String url) {
            ImageView imageView = baseViewHolder.getView(R.id.imageView);
            UiUtils.loadImage(imageView, url,
                    mPos == mUrls.indexOf(url) ? Priority.IMMEDIATE : Priority.NORMAL,
                    new RequestListener() {
                        @Override
                        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                            Toaster.quick("Load failed!");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                            baseViewHolder.getView(R.id.loadingView).setVisibility(View.GONE);
                            return false;
                        }
                    });

            // Set up the user interaction to manually show or hide the system UI.
            RxView.clicks(imageView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Void -> toggle());
        }
    }

    private void downloadImage(final String url) {
        AsyncJob.doInBackground(() -> {
            Pair<File, Boolean> result = WebUtils.downloadGlideImage(url);
            AsyncJob.doOnMainThread(() -> {
                if (result == null) {
                    Toaster.quick("Save failed!");
                } else if (result.second) {
                    Toaster.quick("Saved already!");
                } else {
                    Toaster.quick("Saved to %s", result.first.getAbsolutePath());
                }
            });
        });
    }
}
