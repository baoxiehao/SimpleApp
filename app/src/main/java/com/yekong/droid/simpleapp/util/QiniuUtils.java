package com.yekong.droid.simpleapp.util;


import android.net.Uri;
import android.text.TextUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by baoxiehao on 17/1/25.
 */

public class QiniuUtils {

    public static final String PREFIX_SHARE = "发现";

    public static final int LIMIT = 30;

    private static final String ACCESS_KEY = "VVWBCG4yu-Gc4U8d5tld2b8qreFK_eroJHRVSTCd";
    private static final String SECRET_KEY = "xrOO_itzG2cl8DOGYbdzM7Qbv9vvmFl7PEfYBuOx";

    private static final String BUCKET_NAME = "elixon-image";
    private static final String BUCKET_DOMAIN = "http://okd9fys0f.bkt.clouddn.com";

    private static final String BUCKET_PREFIX_KEY = "prefix";

    private QiniuUtils() {
    }

    public static Observable<List<String>> parseBucketPrefixes() {

        return Observable.create(subscriber -> {
            BufferedReader bufferedReader = null;
            try {
                List<String> prefixes = new ArrayList<>();

                URL prefixFileUrl = new URL(String.format("%s/%s", BUCKET_DOMAIN, BUCKET_PREFIX_KEY));
                bufferedReader = new BufferedReader(new InputStreamReader(prefixFileUrl.openStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    prefixes.add(inputLine);
                }
                Logger.d("parseBucketPrefix(): prefixes = %s", TextUtils.join(", ", prefixes));

                subscriber.onNext(prefixes);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
                Logger.e(e.toString(), e);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static Observable<List<String>> parseBucketMarkers(final String baseMarker, final String prefix) {

        return Observable.create(subscriber -> {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            Zone zone = Zone.autoZone();
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
                        Logger.d("parseBucketMarkers(): prefix = %s, add marker %s", prefix, marker);
                    } else {
                        Logger.d("parseBucketMarkers(): prefix = %s, add marker done", prefix);
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
            Zone zone = Zone.autoZone();
            Configuration conf = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth, conf);

            try {
                FileListing fileListing = null;

                List<String> urls = new ArrayList<>();
                fileListing = bucketManager.listFiles(BUCKET_NAME, prefix, marker, LIMIT, null);
                FileInfo[] fileInfos = fileListing.items;
                // Files are ordered by their names in ascend order, so the last file has final marker.
                // Now reverse them so that late ordered files first, i.e. we are returning files in descend order.
                for (int n = fileInfos.length, i = n - 1; i >= 0; i--) {
                    final String url = String.format("%s/%s", BUCKET_DOMAIN, fileInfos[i].key);
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

    public static Observable<Boolean> uploadBucketFile(Uri uri) {

        return Observable.create(subscriber -> {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            Zone zone = Zone.autoZone();
            Configuration conf = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(conf);

            File file = WebUtils.uriToFile(uri);
            String key = getUploadKey(file);
            try {
                String uploadToken = auth.uploadToken(BUCKET_NAME, key);
                Response response = uploadManager.put(file, key, uploadToken);
                Logger.d("uploadBucketFile(): key = %s, response = %s", key, response.toString());

                if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                    Logger.d("uploadBucketFile(): delete tmp file for uri %s", uri);
                    file.delete();
                }

                subscriber.onNext(response.isOK());
                subscriber.onCompleted();
            } catch (QiniuException e) {
                subscriber.onError(e);
                Logger.e(e.response.toString(), e);
            }
        });
    }

    public static Observable<Boolean> uploadBucketFiles(List<Uri> uris) {

        return Observable.create(subscriber -> {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            Zone zone = Zone.autoZone();
            Configuration conf = new Configuration(zone);
            UploadManager uploadManager = new UploadManager(conf);
            try {
                for (Uri uri : uris) {
                    final File file = WebUtils.uriToFile(uri);

                    final String key = getUploadKey(file);
                    String uploadToken = auth.uploadToken(BUCKET_NAME, key);
                    Response response = uploadManager.put(file, key, uploadToken);
                    Logger.d("uploadBucketFiles(): key = %s, response = %s", key, response.toString());

                    if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                        Logger.d("uploadBucketFile(): delete tmp file for uri %s", uri);
                        file.delete();
                    }

                    subscriber.onNext(response.isOK());
                }
                subscriber.onCompleted();
            } catch (QiniuException e) {
                subscriber.onError(e);
                Logger.e(e.response.toString(), e);
            }
        });
    }

    private static String getUploadKey(File file) {
        return String.format("%s%s", PREFIX_SHARE, DateUtils.timeToFileName(file.lastModified(), file.getName()));
    }
}
