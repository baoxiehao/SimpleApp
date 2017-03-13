package com.yekong.droid.simpleapp.util;

import android.content.Context;
import android.net.Uri;

import com.yekong.droid.simpleapp.R;
import com.yekong.droid.simpleapp.app.SimpleApp;
import com.yekong.droid.simpleapp.model.RssItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by baoxiehao on 17/3/11.
 */

public class RssUtils {

    public static Observable<List<RssItem>> parseIfanrItems() {
        final Context context = SimpleApp.getAppComponent().getContext();
        return Observable.create(subscriber -> {
            try {
                final String url = "http://www.ifanr.com";
                final String source = context.getString(R.string.rss_source_ifanr);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.article-item.article-item--card");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.source = source;
                    rssItem.title = element.select("h3").first().text();
                    rssItem.link = compensateLink(url,
                            element.select(".article-link").first().attr("href"));
                    rssItem.image = compensateImageLink(RegexUtils.matchJoin(
                            element.select(".article-image.cover-image").first().attr("style"),
                            "background-image: url\\('(.*)'\\);"));
                    rssItem.time = DateUtils.parseRssDateTime(element.select("time").first().text());
                    Logger.d("parseIfanrItems(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseGeekParkItems() {
        final Context context = SimpleApp.getAppComponent().getContext();
        return Observable.create(subscriber -> {
            try {
                final String url = "http://www.geekpark.net";
                final String source = context.getString(R.string.rss_source_geekpark);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("article.article-item");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.source = source;
                    rssItem.title = element.select("div.responsive-img > img").first().attr("alt");
                    rssItem.link = compensateLink(url,
                            element.select("a.dib-top.img-cover-wrap").first().attr("href"));
                    rssItem.image = element.select("div.responsive-img > img").first().attr("data-src");
                    rssItem.desc = element.select("p.article-description").first().text();
                    rssItem.time = DateUtils.parseRssDateTime(element.select("a.article-time").first().attr("title"));
                    Logger.d("parseGeekParkItems(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseQdailyItems() {
        final Context context = SimpleApp.getAppComponent().getContext();
        return Observable.create(subscriber -> {
            try {
                final String url = "http://www.qdaily.com";
                final String source = context.getString(R.string.rss_source_qdaily);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.packery-item.article");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.source = source;
                    rssItem.title = element.select("h3").first().text();
                    rssItem.link = compensateLink(url,
                            element.select("a").first().attr("href"));
                    rssItem.image = element.select("div.imgcover.pic > img").first().attr("data-src");
                    rssItem.time = DateUtils.parseRssDateTime(element.select("span.smart-date").first().attr("data-origindate"));
                    Logger.d("parseQdailyItems(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseDiycodeTopics(final String path, final int page) {
        return Observable.create(subscriber -> {
            try {
                final String url = String.format("%s/%s/?page=%d", "http://www.diycode.cc", path, page);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.panel-body div.topic.media");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.title = element.select("div.title > a").first().attr("title");
                    rssItem.link = compensateLink(url,
                            element.select("div.title > a").first().attr("href"));
                    rssItem.image = element.select("div.avatar > a > img").first().attr("src");
                    rssItem.time = DateUtils.parseRssDateTime(element.select("abbr.timeago").first().attr("title"));
                    Logger.d("parseDiycodeTopics(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseDiycodeProjects(final String path, final int page) {
        return Observable.create(subscriber -> {
            try {
                final String url = String.format("%s/%s/?page=%d", "http://www.diycode.cc", path, page);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.panel-body div.media");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.title = element.select("div.media-heading > a").first().text();
                    rssItem.link = compensateLink(url,
                            element.select("div.media-heading > a").first().attr("href"));
                    rssItem.image = element.select("div.avatar > a > img").first().attr("src");
                    rssItem.desc = element.select("div.info").first().text();
                    Logger.d("parseDiycodeProjects(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseDiycodeTrends(final String path, final int page) {
        return Observable.create(subscriber -> {
            try {
                final String url = String.format("%s/%s/?page=%d", "http://www.diycode.cc", path, page);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.panel-body a.list-group-item");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.title = element.select("span.hidden-xs").first().text();
                    rssItem.link = compensateLink(url,
                            element.attr("href"));
                    rssItem.image = element.select("img").first().attr("src");
                    rssItem.desc = element.select("span.stargazers_count").first().text();
                    Logger.d("parseDiycodeTrends(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<List<RssItem>> parseDiycodeSites(final String path, final int page) {
        return Observable.create(subscriber -> {
            try {
                final String url = String.format("%s/%s/?page=%d", "http://www.diycode.cc", path, page);
                List<RssItem> rssItems = new ArrayList<>();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("div.panel-body div.site");
                for (Element element : elements) {
                    RssItem rssItem = new RssItem();
                    rssItem.title = element.select("a").first().text();
                    rssItem.link = compensateLink(url,
                            element.select("a").first().attr("href"));
                    rssItem.image = element.select("img").first().attr("src");
                    Logger.d("parseDiycodeSites(): %s", rssItem);
                    rssItems.add(rssItem);
                }
                subscriber.onNext(rssItems);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private static String compensateLink(final String url, final String link) {
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            Uri uri = Uri.parse(url);
            return String.format("%s://%s%s", uri.getScheme(), uri.getHost(), link);
        }
        return link;
    }

    private static String compensateImageLink(final String imageLink) {
        if (imageLink.startsWith("//")) {
            return "http:" + imageLink;
        }
        return imageLink;
    }
}
