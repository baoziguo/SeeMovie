package com.baozi.movie.bean;

import java.io.Serializable;

/**
 * by:baozi
 * create on 2017/5/16 16:15
 */

public class TiebaPost implements Serializable{

    private String tiebaFid;//贴吧id
    private String tiebaTid;//帖子id
    private String tiebaFname;//贴吧名称
    private String jump_url;//最后显示页面url
    private String title;//帖子标题

    @Override
    public String toString() {
        return "TiebaPost{" +
                "tiebaFid='" + tiebaFid + '\'' +
                ", tiebaTid='" + tiebaTid + '\'' +
                ", tiebaFname='" + tiebaFname + '\'' +
                ", jump_url='" + jump_url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getTiebaFid() {
        return tiebaFid;
    }

    public void setTiebaFid(String tiebaFid) {
        this.tiebaFid = tiebaFid;
    }

    public String getTiebaTid() {
        return tiebaTid;
    }

    public void setTiebaTid(String tiebaTid) {
        this.tiebaTid = tiebaTid;
    }

    public String getTiebaFname() {
        return tiebaFname;
    }

    public void setTiebaFname(String tiebaFname) {
        this.tiebaFname = tiebaFname;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
