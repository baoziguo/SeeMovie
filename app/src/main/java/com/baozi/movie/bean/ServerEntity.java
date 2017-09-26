package com.baozi.movie.bean;

import java.io.Serializable;
import java.util.List;

/**
 * by:baozi
 * create on 2017/5/4 13:45
 */

public class ServerEntity implements Serializable {

    /**
     * code : 1
     * data : [{"game":"jx3","game_id":"3724","server":"长安城","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"龙争虎斗","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"神龙摆尾","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"海誓山盟","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"幽雨坪","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"翠烟山","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"逍遥林","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"枫华谷","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"义薄云天","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"晴昼海","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"金水镇","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"昆仑","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"扬州城","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"巴陵县","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"南屏山","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"洛道","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"洛阳城","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"龙门荒漠","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"萍踪侠影","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"独步天下","status":"1","zone":"电信一区","zone_id":"z01"},{"game":"jx3","game_id":"3724","server":"夜幕星河","status":"1","zone":"电信一区","zone_id":"z01"}]
     * msg : SUCCESS
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * game : jx3
         * game_id : 3724
         * server : 长安城
         * status : 1
         * zone : 电信一区
         * zone_id : z01
         */

        private String game;
        private String game_id;
        private String server;
        private String status;
        private String zone;
        private String zone_id;

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getZone_id() {
            return zone_id;
        }

        public void setZone_id(String zone_id) {
            this.zone_id = zone_id;
        }

    }
}
