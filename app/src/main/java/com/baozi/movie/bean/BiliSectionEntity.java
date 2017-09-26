package com.baozi.movie.bean;

import java.io.Serializable;
import cn.bmob.v3.BmobObject;

/**
 * by:baozi
 * create on 2017/3/31 18:10
 */

public class BiliSectionEntity extends BmobObject implements Serializable{
    private String title;
    private String url;
    private String imageUrl;
    private String cid;
    private String type;
    private String pcUrl;

    public String getPcUrl() {
        return pcUrl;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
