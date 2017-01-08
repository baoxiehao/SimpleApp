package com.yekong.droid.simpleapp.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.piasy.biv.view.BigImageView;
import com.github.piasy.biv.view.ImageSaveCallback;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.util.Eventer;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageActivity extends AppCompatActivity {
    public static final String ACTION = "com.yekong.droid.simpleapp.action.VIEW_IMAGE";
    public static final String EXTRA_TITLES = "EXTRA_TITLES";
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

    @BindView(R.id.recyclerViewPager)
    RecyclerViewPager mRecyclerViewPager;

    @BindView(R.id.saveImageButton)
    Button mSaveImageButton;

    @BindView(R.id.bottom_controls)
    View mControlsBottomView;

    private void setFullScreen() {
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setFullscreenWithActionBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private final Runnable mEnterFullscreenRunnable = () -> {
        // Delayed removal of status and navigation bar
        setFullScreen();
    };

    private final Runnable mExitFullscreenRunnable = () -> {
        setFullscreenWithActionBar();
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

        final ArrayList<String> titles = getIntent().getStringArrayListExtra(EXTRA_TITLES);
        final ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_URLS);
        final int pos = getIntent().getIntExtra(EXTRA_POS, 0);

        getSupportActionBar().setTitle(titles.get(pos));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPager.setLayoutManager(layout);

        BaseQuickAdapter adapter = new ImageAdapter(urls);
        adapter.openLoadAnimation();
        mRecyclerViewPager.setAdapter(adapter);
        mRecyclerViewPager.scrollToPosition(pos);
        mRecyclerViewPager.addOnPageChangedListener((currPos, newPos) -> {
            getSupportActionBar().setTitle(titles.get(newPos));
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        mSaveImageButton.setOnTouchListener(mDelayHideTouchListener);
    }

    @OnClick(R.id.saveImageButton)
    public void onClick(View view) {
        BigImageView bigImageView = (BigImageView) mRecyclerViewPager.getChildAt(
                mRecyclerViewPager.getCurrentPosition()).findViewById(R.id.bigImageView);
        bigImageView.saveImageIntoGallery();
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
        mRecyclerViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mEnterFullscreenRunnable);
        mHideHandler.postDelayed(mExitFullscreenRunnable, UI_ANIMATION_DELAY);

        mHideHandler.postDelayed(() -> hide(), UI_ANIMATION_DELAY + AUTO_HIDE_DELAY_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Eventer.PositionEvent.send(mRecyclerViewPager.getCurrentPosition());
    }

    class ImageAdapter extends BaseQuickAdapter<String> {

        public ImageAdapter(List<String> data) {
            super(R.layout.item_image, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String s) {
            BigImageView bigImageView = baseViewHolder.getView(R.id.bigImageView);
            bigImageView.showImage(Uri.parse(s));
            // Set up the user interaction to manually show or hide the system UI.
            bigImageView.setOnClickListener(view -> toggle());
            bigImageView.setOnLongClickListener((view) -> {
                bigImageView.saveImageIntoGallery();
                return true;
            });
            bigImageView.setImageSaveCallback(new ImageSaveCallback() {
                @Override
                public void onSuccess(String uri) {
                    Toaster.quick("Save done!");
                }

                @Override
                public void onFail(Throwable t) {
                    Toaster.quick("Save failed!");
                }
            });
        }
    }
}
