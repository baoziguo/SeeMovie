package com.baozi.movie.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * by:baozi
 * create on 2017/3/31 18:10
 */

public class WeiBo extends BmobObject implements Serializable{
    private String weibourl;

    public String getUrl() {
        return weibourl;
    }

    public void setUrl(String weibourl) {
        this.weibourl = weibourl;
    }

}
