package com.baozi.movie.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by baozi on 2016/7/16 0016.
 */
public class weiXin extends BmobObject {
    private String title;
    private String url;
    private String imgUrl;
    private String describe;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
