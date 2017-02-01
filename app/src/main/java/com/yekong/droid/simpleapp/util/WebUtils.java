package com.yekong.droid.simpleapp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.LinkItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by baoxiehao on 17/1/25.
 */

public class WebUtils {
    private WebUtils() {
    }

    public static Pair<File, Boolean> downloadGlideImage(final String url) {
        try {
            File dir = new File(String.format("%s/%s",
                    Environment.getExternalStorageDirectory().getAbsolutePath(), "SimpleApp"));
            if (dir.exists() || dir.mkdirs()) {
                String fileName = Uri.parse(url).getLastPathSegment();
                fileName = fileName.replaceFirst(QiniuUtils.PREFIX_SHARE, "");
                File targetFile = new File(dir, fileName);
                if (targetFile.exists()) {
                    Logger.d("downloadGlideImage(): file existing!");
                    return new Pair<>(targetFile, true);
                }
                File downloadFile = Glide.with(SimpleApp.getAppComponent().getContext())
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                Logger.d("downloadGlideImage(): downloadFile = %s", downloadFile.getAbsolutePath());
                if (FileUtils.copyFile(downloadFile, targetFile)) {
                    return new Pair<>(targetFile, false);
                }
            } else {
                Logger.w("downloadGlideImage(): create dir failed!");
            }
        } catch (final Exception e) {
            Logger.e("downloadGlideImage error", e);
        }
        return null;
    }

    public static Observable<List<LinkItem>> parseLinks(final String url, final String selector) {
        return Observable.create(subscriber -> {
            try {
                List<LinkItem> linkItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                final String docTitle = doc.title();
                final String host = Uri.parse(url).getHost();
                Logger.d("parseLinks(): host = %s, title = %s", host, docTitle);
                Elements elements = doc.select(selector);
                for (Element element : elements) {
                    final String elementTitle = element.text();
                    String elementUrl = element.attr("href");
                    if (elementUrl.startsWith("/")) {
                        elementUrl = url + elementUrl;
                    }
                    Logger.d("parseLinks(): docTitle = %s, element: %s", docTitle, element.outerHtml());
                    if (TextUtils.isEmpty(elementTitle)) {
                        continue;
                    }
                    linkItems.add(LinkItem.create(elementTitle, elementUrl, docTitle));
                }
                subscriber.onNext(linkItems);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static File uriToFile(Uri uri) {
        final String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            File file = new File(uri.getPath());
            Logger.d("uriToFile(): file %s", file.getAbsolutePath());
            return file;
        } else if ("content".equals(scheme)) {
            final Context context = SimpleApp.getAppComponent().getContext();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                        Logger.d("uriToFile(): content file %s", file.getAbsolutePath());
                        return file;
                    }
                } finally {
                    cursor.close();
                }
            }
            return null;
        } else {
            File file = new File(Environment.getExternalStorageDirectory(), uri.getLastPathSegment());
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                URL url = new URL(uri.toString());
                URLConnection conn = url.openConnection();
                InputStream inputStream = conn.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, read);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.d("uriToFile(): other file %s", file.getAbsolutePath());
            return file;
        }
    }
}
