package com.yekong.droid.simpleapp.model;

/**
 * Created by baoxiehao on 17/1/27.
 */

public class RssItem {
    public String title;
    public String url;
    public String source;

    public static RssItem create(final String title, final String url, final String source) {
        RssItem rssItem = new RssItem();
        rssItem.title = title;
        rssItem.url = url;
        rssItem.source = source;
        return rssItem;
    }

    @Override
    public String toString() {
        return String.format("title=%s, url=%s, source=%s", title, url, source);
    }
}
