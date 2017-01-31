package com.yekong.droid.simpleapp.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.LinkItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            Bitmap bitmap = Glide.with(SimpleApp.getAppComponent().getContext())
                    .load(url)
                    .asBitmap()
                    .into(-1, -1)
                    .get();
            if (bitmap != null) {
                File dir = new File(String.format("%s/%s",
                        Environment.getExternalStorageDirectory().getAbsolutePath(), "SimpleApp"));
                if (dir.exists() || dir.mkdirs()) {
                    File file = new File(dir, String.format("%s.jpg", url.hashCode()));
                    if (file.exists()) {
                        return new Pair<>(file, true);
                    }
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file))) {
                        return new Pair<>(file, false);
                    }
                }
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
                Logger.d("host = %s, title = %s", host, docTitle);
                Elements elements = doc.select(selector);
                for (Element element : elements) {
                    final String elementTitle = element.text();
                    String elementUrl = element.attr("href");
                    if (elementUrl.startsWith("/")) {
                        elementUrl = url + elementUrl;
                    }
                    Logger.d("docTitle = %s, element: %s", docTitle, element.outerHtml());
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
}
