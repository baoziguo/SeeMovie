package com.baozi.movie.bean;

import java.io.Serializable;
import java.util.List;

/**
 * by:baozi
 * create on 2017/5/4 12:41
 */

public class ZoneEntity implements Serializable {

    /**
     * code : 1
     * data : [{"game_id":"3724","zone":"电信一区","zone_id":"z01","_token":"feb86cd946b2e174","sign":"GxuiwNyqBOexUDmOGvlgLA=="},{"game_id":"3724","zone":"电信二区","zone_id":"z02","sign":"XrtLgYSx1hAcfXMLG+QFSQ=="},{"game_id":"3724","zone":"电信三区","zone_id":"z03","sign":"7vWdhth+so0tsRG4weM35A=="},{"game_id":"3724","zone":"电信四区","zone_id":"z04","sign":"/eQjyLQU8qH7pX6Nhb/HJw=="},{"game_id":"3724","zone":"电信五区","zone_id":"z05","sign":"vidRMXw8qzv9Pr+Tg+il4Q=="},{"game_id":"3724","zone":"电信六区","zone_id":"z06","sign":"QCCpjojrADdeCyOLRbxlhg=="},{"game_id":"3724","zone":"电信七区","zone_id":"z07","sign":"wdsvdGyQ8zEjQY5njn54tw=="},{"game_id":"3724","zone":"电信八区","zone_id":"z08","sign":"4EkTxlTsLDXRMnqGBvlgyg=="},{"game_id":"3724","zone":"网通(一/二)区","zone_id":"z11","sign":"G66eR7+skxbk2mLZMG1SXw=="},{"game_id":"3724","zone":"网通三区","zone_id":"z13","sign":"bejeqtlG8ev7EFA9xG7cxg=="},{"game_id":"3724","zone":"比赛专区","zone_id":"z14","sign":"G/bidO66Y++mTgX9XsiU6Q=="},{"game_id":"3724","zone":"双线一区","zone_id":"z21","sign":"QBmMdvgW9IT/jFysvlIohg=="},{"game_id":"3724","zone":"双线二区","zone_id":"z22","sign":"Vp0OxLKpwhdQNZjYdwo/aQ=="}]
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

    public static class DataBean {
        /**
         * game_id : 3724
         * zone : 电信一区
         * zone_id : z01
         * _token : feb86cd946b2e174
         * sign : GxuiwNyqBOexUDmOGvlgLA==
         */

        private String game_id;
        private String zone;
        private String zone_id;
        private String _token;
        private String sign;

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
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

        public String get_token() {
            return _token;
        }

        public void set_token(String _token) {
            this._token = _token;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
