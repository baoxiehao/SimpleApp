package com.yekong.droid.simpleapp.model;

/**
 * Created by baoxiehao on 17/1/27.
 */

public class RssItem {
    public String title;
    public String link;
    public String desc;
    public String image;
    public String time;

    public static RssItem create(String title, String link, String desc, String image, String time) {
        RssItem rssItem = new RssItem();
        rssItem.title = title;
        rssItem.link = link;
        rssItem.desc = desc;
        rssItem.image = image;
        rssItem.time = time;
        return rssItem;
    }

    @Override
    public String toString() {
        return String.format("title=%s, link=%s, desc=%s, image=%s, time=%s", title, link, desc, image, time);
    }
}
