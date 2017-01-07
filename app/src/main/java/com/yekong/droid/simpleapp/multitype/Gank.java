package com.yekong.droid.simpleapp.multitype;

import java.util.Date;
import java.util.List;

import me.drakeet.multitype.Item;

/**
 * Created by baoxiehao on 16/11/28.
 */

/**
 * {
 *   "error": false,
 *   "results": [
 *     {
 *       "_id": "575cd951421aa90d6d6a3367",
 *       "createdAt": "2016-06-12T11:38:57.849Z",
 *       "desc": "6.12",
 *       "publishedAt": "2016-06-20T12:31:26.789Z",
 *       "source": "chrome",
 *       "type": "\u798f\u5229",
 *       "url": "http://ww3.sinaimg.cn/large/610dc034jw1f4saelbb4oj20zk0qoage.jpg",
 *       "used": true,
 *       "who": "\u4ee3\u7801\u5bb6"
 *     },
 *     ...
 *   ]
 * }
 */
public class Gank {

    public class ArticleResponse {
        public String error;
        public List<Article> results;
    }

    public class FuliResponse {
        public String error;
        public List<Fuli> results;
    }

    private class Entity implements Item {
        public String who;
        public String createdAt;
        public Date publishedAt;
        public String desc;
        public String url;
        public String type;
    }

    public class Fuli extends Entity {
    }

    public class Article extends Entity {
        public List<String> images;
    }
}