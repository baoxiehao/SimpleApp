package com.yekong.droid.simpleapp.multitype;

import java.util.Date;
import java.util.List;

import me.drakeet.multitype.Item;

/**
 * Created by baoxiehao on 16/11/27.
 */
public class BaiduImage implements Item {

    public class Response {
        public String col;
        public String tag;
        public int startIndex;
        public int returnNumber;
        public int totalNum;
        public List<Entity> imgs;
    }

    public class Entity implements Item {
        public String title;
        public String desc;
        public Date date;
        public int imageWidth;
        public int imageHeight;
        public String imageUrl;
        public int thumbnailWidth;
        public int thumbnailHeight;
        public String thumbnailUrl;
        public String fromUrl;
    }
}