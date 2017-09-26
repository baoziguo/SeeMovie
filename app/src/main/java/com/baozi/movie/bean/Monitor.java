package com.baozi.movie.bean;

/**
 * Created by baozi on 2016/12/2.
 */

public class Monitor {

    /**
     * id : 8
     * area : 电信七区
     * name : 雪月天枪
     * status : 2
     * latest : 2016-12-01 16:33
     * history : 2016-12-01 10:45
     * ip : 121.14.64.145
     * isTop : 0
     * isCollect : 0
     * isSubscribe : 0
     * backgroundcolor :
     */

    private int id;
    private String area;
    private String name;
    private int status;
    private String latest;
    private String history;
    private String ip;
    private int isTop;
    private int isCollect;
    private int isSubscribe;
    private String backgroundcolor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }
}
