package com.baozi.movie.bean;

import cn.bmob.v3.BmobObject;

/**
 * by:baozi
 * create on 2017/5/4 15:35
 */

public class ServerSign extends BmobObject {
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private String sign;
}
