package com.baozi.movie.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class kePao extends BmobObject {
    private String name;
    private String url;
    private BmobFile fileUrl;

    public BmobFile getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(BmobFile fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
