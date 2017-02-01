package com.yekong.droid.simpleapp.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yekong.droid.simpleapp.util.Logger;

/**
 * Created by baoxiehao on 17/1/26.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().post(() -> onFirstFrameDisplayed());
    }

    protected void onFirstFrameDisplayed() {
        Logger.d("onFirstFrameDisplayed");
    }
}
