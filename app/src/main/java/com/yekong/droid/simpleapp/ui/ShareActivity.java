package com.yekong.droid.simpleapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.ui.base.BaseActivity;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.QiniuUtils;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baoxiehao on 17/2/1.
 */

public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        Logger.d("onCreate(): action = %s, type = %s", action, type);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image")) {
                handleSendImage(intent);
            } else if (type.startsWith("text")) {
                handleSendText(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image")) {
                handleSendImages(intent);
            }
        } else {
            Toaster.quick("Unsupported action!");
            finish();
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            QiniuUtils.uploadBucketFile(imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        Logger.d("handleSendImage(): uri = %s, success = %s", imageUri, success);
                        Toaster.quick("Share %s!", success ? "successful" : "failed");
                        finish();
                    });
        }
    }

    private void handleSendImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            final int count = imageUris.size();
            final int[] indexAndCount = new int[2];
            indexAndCount[0] = 0;
            indexAndCount[1] = 0;

            QiniuUtils.uploadBucketFiles(imageUris)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                                if (success) {
                                    indexAndCount[1]++;
                                }
                                Logger.d("handleSendImages(): uri = %s, success = %s", imageUris.get(indexAndCount[0]), success);
                                indexAndCount[0]++;
                            },
                            error -> Toaster.quick(error.toString()),
                            () -> {
                                Toaster.quick("Shared %s of %s", indexAndCount[1], count);
                                finish();
                            });
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            final Uri imageUri = Uri.parse(sharedText);
            QiniuUtils.uploadBucketFile(imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        Logger.d("handleSendText(): uri = %s, success = %s", imageUri, success);
                        Toaster.quick("Share %s!", success ? "successful" : "failed");
                        finish();
                    });
        }
    }
}
