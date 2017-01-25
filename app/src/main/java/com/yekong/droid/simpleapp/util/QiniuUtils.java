package com.yekong.droid.simpleapp.util;


import android.util.Pair;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by baoxiehao on 17/1/25.
 */

public class QiniuUtils {

    private static final String ACCESS_KEY = "VVWBCG4yu-Gc4U8d5tld2b8qreFK_eroJHRVSTCd";
    private static final String SECRET_KEY = "xrOO_itzG2cl8DOGYbdzM7Qbv9vvmFl7PEfYBuOx";

    private static final String BUCKET_NAME = "elixon-image";
    private static final String BUCKET_DOMAIN = "http://okd9fys0f.bkt.clouddn.com";

    private static final int LIMIT = 30;

    private QiniuUtils() {
    }

    public static Observable<Pair<String, List<String>>> parseBucketKeys(final String marker, final String prefix) {

        return Observable.create(subscriber -> {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            Zone zone = Zone.zone0();
            Configuration conf = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth, conf);

            try {
                FileListing fileListing = null;

                List<String> urls = new ArrayList<>();
                fileListing = bucketManager.listFiles(BUCKET_NAME, prefix, marker, LIMIT, null);
                FileInfo[] items = fileListing.items;
                for (FileInfo fileInfo : items) {
                    final String url = String.format("%s/%s", BUCKET_DOMAIN, fileInfo.key);
                    urls.add(url);
                }
                subscriber.onNext(new Pair<>(fileListing.marker, urls));

                subscriber.onCompleted();
            } catch (QiniuException e) {
                subscriber.onError(e);
                Logger.e(e.response.toString(), e);
            }
        });
    }
}
