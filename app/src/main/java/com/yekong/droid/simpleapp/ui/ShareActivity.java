package com.yekong.droid.simpleapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.ui.base.BaseActivity;
import com.yekong.droid.simpleapp.ui.base.BaseAdapter;
import com.yekong.droid.simpleapp.util.Logger;
import com.yekong.droid.simpleapp.util.QiniuUtils;
import com.yekong.droid.simpleapp.util.Toaster;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by baoxiehao on 17/2/1.
 */

public class ShareActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.prefixList)
    RecyclerView mPrefixList;

    String mPrefix;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        addDisposable(QiniuUtils.parseBucketPrefixes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(prefixList -> {
                    ListAdapter adapter = new ListAdapter(prefixList);
                    mPrefixList.setAdapter(adapter);
                    mPrefixList.setLayoutManager(new LinearLayoutManager(ShareActivity.this));

                    adapter.setOnRecyclerViewItemClickListener((view, pos) -> {
                        mPrefix = prefixList.get(pos);

                        mPrefixList.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);

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
                    });
                }));
    }

    class ListAdapter extends BaseAdapter<String> {

        public ListAdapter(List<String> data) {
            super(R.layout.item_text, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String prefix) {
            baseViewHolder.setText(R.id.primaryText, prefix);
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            addDisposable(QiniuUtils.uploadBucketFile(mPrefix, imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        Logger.d("handleSendImage(): uri = %s, success = %s", imageUri, success);
                        Toaster.quick(getString(success ? R.string.toast_share_image_successful : R.string.toast_share_image_failed));
                        finish();
                    }));
        }
    }

    private void handleSendImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            final int count = imageUris.size();
            final int[] indexAndCount = new int[2];
            indexAndCount[0] = 0;
            indexAndCount[1] = 0;

            addDisposable(QiniuUtils.uploadBucketFiles(mPrefix, imageUris)
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
                                Toaster.quick(String.format(getString(R.string.toast_share_image_result),
                                        indexAndCount[1], count));
                                finish();
                            }));
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            final Uri imageUri = Uri.parse(sharedText);
            addDisposable(QiniuUtils.uploadBucketFile(mPrefix, imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        Logger.d("handleSendText(): uri = %s, success = %s", imageUri, success);
                        Toaster.quick(getString(success ? R.string.toast_share_image_successful : R.string.toast_share_image_failed));
                        finish();
                    }));
        }
    }
}
