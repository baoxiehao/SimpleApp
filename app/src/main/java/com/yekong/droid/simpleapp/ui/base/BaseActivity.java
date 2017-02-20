package com.yekong.droid.simpleapp.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yekong.droid.simpleapp.util.Logger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by baoxiehao on 17/1/26.
 */

public class BaseActivity extends AppCompatActivity {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().post(() -> onFirstFrameDisplayed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
        mDisposables.clear();
    }

    protected void addSubscription(Subscription subscription) {
        mSubscriptions.add(subscription);
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    protected void onFirstFrameDisplayed() {
        Logger.d("onFirstFrameDisplayed");
    }
}
