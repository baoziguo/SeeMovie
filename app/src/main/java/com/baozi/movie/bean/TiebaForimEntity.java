package com.baozi.movie.bean;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * by:baozi
 * create on 2017/5/17 18:02
 */

public class TiebaForimEntity implements Serializable {

    /**
     * ctime : 0
     * error_code : 0
     * forum_list : {"non-gconforum":[{"avatar":"http://imgsrc.baidu.com/forum/pic/item/96dda144ad3459825c89afcf0df431adcbef8468.jpg","cur_score":"107","favo_type":"0","id":"2410411","level_id":"6","level_name":"血龙出渊","levelup_score":"200","name":"唯满侠","slogan":"电信5唯我独尊满江红侠客行贴吧欢迎您加入\u2026"},{"avatar":"http://imgsrc.baidu.com/forum/pic/item/d50735fae6cd7b890d8840b20d2442a7d9330e1e.jpg","cur_score":"65","favo_type":"0","id":"1850285","level_id":"5","level_name":"回头是岸","levelup_score":"100","name":"北少室","slogan":"剑网三少林门派玩家交流贴吧"}]}
     * has_more : 0
     * logid : 3357045869
     * server_time : 99042
     * time : 1495014957
     */

    private int ctime;
    private String error_code;
    private ForumListBean forum_list;
    private String has_more;
    private long logid;
    private String server_time;
    private int time;

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public ForumListBean getForum_list() {
        return forum_list;
    }

    public void setForum_list(ForumListBean forum_list) {
        this.forum_list = forum_list;
    }

    public String getHas_more() {
        return has_more;
    }

    public void setHas_more(String has_more) {
        this.has_more = has_more;
    }

    public long getLogid() {
        return logid;
    }

    public void setLogid(long logid) {
        this.logid = logid;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static class ForumListBean {
        @SerializedName("non-gconforum")
        private List<NongconforumBean> nongconforum;

        public List<NongconforumBean> getNongconforum() {
            return nongconforum;
        }

        public void setNongconforum(List<NongconforumBean> nongconforum) {
            this.nongconforum = nongconforum;
        }

        public static class NongconforumBean {
            /**
             * avatar : http://imgsrc.baidu.com/forum/pic/item/96dda144ad3459825c89afcf0df431adcbef8468.jpg
             * cur_score : 107
             * favo_type : 0
             * id : 2410411
             * level_id : 6
             * level_name : 血龙出渊
             * levelup_score : 200
             * name : 唯满侠
             * slogan : 电信5唯我独尊满江红侠客行贴吧欢迎您加入…
             */

            private String avatar;
            private String cur_score;
            private String favo_type;
            private String id;
            private String level_id;
            private String level_name;
            private String levelup_score;
            private String name;
            private String slogan;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCur_score() {
                return cur_score;
            }

            public void setCur_score(String cur_score) {
                this.cur_score = cur_score;
            }

            public String getFavo_type() {
                return favo_type;
            }

            public void setFavo_type(String favo_type) {
                this.favo_type = favo_type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLevel_id() {
                return level_id;
            }

            public void setLevel_id(String level_id) {
                this.level_id = level_id;
            }

            public String getLevel_name() {
                return level_name;
            }

            public void setLevel_name(String level_name) {
                this.level_name = level_name;
            }

            public String getLevelup_score() {
                return levelup_score;
            }

            public void setLevelup_score(String levelup_score) {
                this.levelup_score = levelup_score;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlogan() {
                return slogan;
            }

            public void setSlogan(String slogan) {
                this.slogan = slogan;
            }
        }
    }
}
