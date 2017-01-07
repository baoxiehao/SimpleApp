package com.yekong.droid.simpleapp.api;

import com.yekong.droid.simpleapp.multitype.Baidu;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by baoxiehao on 16/11/26.
 */

/**
 * 以GET形式提交，返回JSON
 * URL：http://image.baidu.com/data/imgs?col=&tag=&sort=&pn=&rn=&p=channel&from=1
 *
 * 参数：col=大类&tag=分类&sort=0&pn=开始条数&rn=返回条数&p=channel&from=1
 *
 * 例子：
 *
 * http://image.baidu.com/data/imgs?col=美女&tag=小清新&sort=0&pn=10&rn=10&p=channel&from=1
 * http://image.baidu.com/data/imgs?col=美女&tag=韩国&pn=3&rn=10&from=1
 *
 * 备注: 返回的imgs列表可能有空的,所有需要过滤掉
 */
public class BaiduService {
    private BaiduApi mBaiduApi;

    public BaiduService(BaiduApi baiduApi) {
        mBaiduApi = baiduApi;
    }

    public Observable<Baidu.ImagesResponse> searchImages(String col, String tag, int startIndex, int returnNumber) {
        Map<String, String> options = new HashMap<>(5);
        options.put("col", col);
        options.put("tag", tag);
        options.put("pn", String.valueOf(startIndex));
        options.put("rn", String.valueOf(returnNumber));
        options.put("from", "1");
        return mBaiduApi.searchImages(options);
    }
}
