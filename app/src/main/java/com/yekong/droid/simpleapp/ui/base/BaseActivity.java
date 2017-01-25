package com.yekong.droid.simpleapp.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by baoxiehao on 17/1/26.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getWindow().getDecorView().post(() -> onFirstFrameDisplayed());
    }

    protected void onFirstFrameDisplayed() {
    }
}
