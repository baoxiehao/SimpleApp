package com.yekong.droid.simpleapp.model;

/**
 * Created by baoxiehao on 17/1/27.
 */

public class LinkItem {
    public String title;
    public String url;
    public String desc;
    public String source;

    public static LinkItem create(final String title, final String url, final String desc, final String source) {
        LinkItem linkItem = new LinkItem();
        linkItem.title = title;
        linkItem.url = url;
        linkItem.desc = desc;
        linkItem.source = source;
        return linkItem;
    }

    @Override
    public String toString() {
        return String.format("title=%s, url=%s, desc=%s, source=%s", title, url, desc, source);
    }
}
