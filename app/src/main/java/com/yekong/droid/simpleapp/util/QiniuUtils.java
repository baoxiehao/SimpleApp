package com.yekong.droid.simpleapp.util;


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

    public static final int LIMIT = 30;

    private QiniuUtils() {
    }

    public static Observable<List<String>> parseBucketMarkers(final String baseMarker, final String prefix) {

        return Observable.create(subscriber -> {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            Zone zone = Zone.zone0();
            Configuration conf = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth, conf);

            try {
                FileListing fileListing = null;

                List<String> markers = new ArrayList<>();
                String marker = baseMarker;
                do {
                    fileListing = bucketManager.listFiles(BUCKET_NAME, prefix, marker, LIMIT, null);
                    marker = fileListing.marker;
                    if (marker != null) {
                        markers.add(0, marker);
                        Logger.d("prefix = %s, add marker %s", prefix, marker);
                    } else {
                        Logger.d("prefix = %s, add marker done", prefix);
                    }
                } while (marker != null);
                subscriber.onNext(markers);

                subscriber.onCompleted();
            } catch (QiniuException e) {
                subscriber.onError(e);
                Logger.e(e.response.toString(), e);
            }
        });
    }

    public static Observable<List<String>> parseBucketUrls(final String marker, final String prefix) {

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
                subscriber.onNext(urls);

                subscriber.onCompleted();
            } catch (QiniuException e) {
                subscriber.onError(e);
                Logger.e(e.response.toString(), e);
            }
        });
    }
}
