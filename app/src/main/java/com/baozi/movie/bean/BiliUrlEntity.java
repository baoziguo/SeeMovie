package com.baozi.movie.bean;

import java.io.Serializable;
import java.util.List;

/**
 * by:baozi
 * create on 2017/4/1 14:20
 */

public class BiliUrlEntity implements Serializable {

    /**
     * accept_format : flv,hdmp4,mp4
     * accept_quality : [3,2,1]
     * cid : http://comment.bilibili.com/15723303.xml
     * durl : [{"backup_url":["http://cn-jswx-cmcc-v-01.acgvideo.com/vg5/6/01/15723303-1.mp4?expires=1491035400&platform=pc&ssig=hoX99SmzlRGVPbTN9ga3wQ&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1","http://cn-fjxm2-dx-v-05.acgvideo.com/vg1/a/77/15723303-1.mp4?expires=1491035400&platform=pc&ssig=-iZEYJptQZsz8vuTYp6-8A&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1"],"length":256697,"order":1,"size":18583898,"url":"http://cn-tj2-cu.acgvideo.com/vg20/d/fd/15723303-1.mp4?expires=1491035400&platform=pc&ssig=OS5uKIqkO1Ox_iwfeDb5Xw&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1"}]
     * format : mp4
     * from : local
     * img : https://i1.hdslb.com/bfs/archive/ac44967ed71998466b83985d7798f7378df60395.jpg
     * result : suee
     * seek_param : start
     * seek_type : second
     * timelength : 256697
     */

    private String accept_format;
    private String cid;
    private String format;
    private String from;
    private String img;
    private String result;
    private String seek_param;
    private String seek_type;
    private int timelength;
    private List<Integer> accept_quality;
    private List<DurlBean> durl;

    public String getAccept_format() {
        return accept_format;
    }

    public void setAccept_format(String accept_format) {
        this.accept_format = accept_format;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSeek_param() {
        return seek_param;
    }

    public void setSeek_param(String seek_param) {
        this.seek_param = seek_param;
    }

    public String getSeek_type() {
        return seek_type;
    }

    public void setSeek_type(String seek_type) {
        this.seek_type = seek_type;
    }

    public int getTimelength() {
        return timelength;
    }

    public void setTimelength(int timelength) {
        this.timelength = timelength;
    }

    public List<Integer> getAccept_quality() {
        return accept_quality;
    }

    public void setAccept_quality(List<Integer> accept_quality) {
        this.accept_quality = accept_quality;
    }

    public List<DurlBean> getDurl() {
        return durl;
    }

    public void setDurl(List<DurlBean> durl) {
        this.durl = durl;
    }

    public static class DurlBean {
        /**
         * backup_url : ["http://cn-jswx-cmcc-v-01.acgvideo.com/vg5/6/01/15723303-1.mp4?expires=1491035400&platform=pc&ssig=hoX99SmzlRGVPbTN9ga3wQ&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1","http://cn-fjxm2-dx-v-05.acgvideo.com/vg1/a/77/15723303-1.mp4?expires=1491035400&platform=pc&ssig=-iZEYJptQZsz8vuTYp6-8A&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1"]
         * length : 256697
         * order : 1
         * size : 18583898
         * url : http://cn-tj2-cu.acgvideo.com/vg20/d/fd/15723303-1.mp4?expires=1491035400&platform=pc&ssig=OS5uKIqkO1Ox_iwfeDb5Xw&oi=3054637986&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1
         */

        private int length;
        private int order;
        private int size;
        private String url;
        private List<String> backup_url;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getBackup_url() {
            return backup_url;
        }

        public void setBackup_url(List<String> backup_url) {
            this.backup_url = backup_url;
        }
    }
}
