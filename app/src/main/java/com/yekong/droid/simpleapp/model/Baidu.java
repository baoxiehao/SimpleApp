package com.yekong.droid.simpleapp.model;

import java.util.Date;
import java.util.List;

/**
 * Created by baoxiehao on 16/11/27.
 */
public class Baidu {

    public class ImagesResponse {
        public String col;
        public String tag;
        public int startIndex;
        public int returnNumber;
        public int totalNum;
        public List<Image> imgs;
    }

    public class Image {
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