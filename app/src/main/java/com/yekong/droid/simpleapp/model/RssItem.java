package com.yekong.droid.simpleapp.model;

import com.google.gson.Gson;

/**
 * Created by baoxiehao on 17/1/27.
 */

public class RssItem {
    public static final Gson GSON = new Gson();

    public String title;
    public String link;
    public String desc;
    public String image;
    public String time;
    public String source;

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
