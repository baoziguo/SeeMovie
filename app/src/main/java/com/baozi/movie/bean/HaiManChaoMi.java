package com.baozi.movie.bean;

/**
 * Created by user on 2016/8/15.
 */
public class HaiManChaoMi {

    /**
     * id : 235874
     * prefix : <span class="sticky">【置顶】</span>
     * time : 27分钟前
     * time_point : 1471238006
     * content : <em>#FAQ</em> 此贴必读，侵权/三俗/恶意贴子删除关入小黑屋，漏删请圈我，内附详细规则说明。
     ...<span class="view-full">[查看全部]</span>
     * server : 梦江南
     * image1 : null
     * num_click : 18697
     * num_report : 10
     * num_comment : 555
     * num_praise : 110
     * user : {"id":1,"name":"海鳗鳗(军爷)<i class=\"fa fa-check\"><\/i>","prof_id":3,"score_label":"<em class=\"level-5\">Lv5.3<\/em>","name_normal":"军爷"}
     * icon : {"report":"fa-warning-o","comment":"fa-comment-o","praise":"fa-thumbs-o-up"}
     * flag : {"fire":"<i class=\"fa fa-fire\" aria-hidden=\"true\"><\/i>","voice":"","location":""}
     */

    private int id;
    private String prefix;
    private String time;
    private int time_point;
    private String content;
    private String server;
    private Object image1;
    private int num_click;
    private String num_report;
    private String num_comment;
    private String num_praise;
    /**
     * id : 1
     * name : 海鳗鳗(军爷)<i class="fa fa-check"></i>
     * prof_id : 3
     * score_label : <em class="level-5">Lv5.3</em>
     * name_normal : 军爷
     */

    private UserBean user;
    /**
     * report : fa-warning-o
     * comment : fa-comment-o
     * praise : fa-thumbs-o-up
     */

    private IconBean icon;
    /**
     * fire : <i class="fa fa-fire" aria-hidden="true"></i>
     * voice :
     * location :
     */

    private FlagBean flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTime_point() {
        return time_point;
    }

    public void setTime_point(int time_point) {
        this.time_point = time_point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Object getImage1() {
        return image1;
    }

    public void setImage1(Object image1) {
        this.image1 = image1;
    }

    public int getNum_click() {
        return num_click;
    }

    public void setNum_click(int num_click) {
        this.num_click = num_click;
    }

    public String getNum_report() {
        return num_report;
    }

    public void setNum_report(String num_report) {
        this.num_report = num_report;
    }

    public String getNum_comment() {
        return num_comment;
    }

    public void setNum_comment(String num_comment) {
        this.num_comment = num_comment;
    }

    public String getNum_praise() {
        return num_praise;
    }

    public void setNum_praise(String num_praise) {
        this.num_praise = num_praise;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public IconBean getIcon() {
        return icon;
    }

    public void setIcon(IconBean icon) {
        this.icon = icon;
    }

    public FlagBean getFlag() {
        return flag;
    }

    public void setFlag(FlagBean flag) {
        this.flag = flag;
    }

    public static class UserBean {
        private int id;
        private String name;
        private int prof_id;
        private String score_label;
        private String name_normal;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProf_id() {
            return prof_id;
        }

        public void setProf_id(int prof_id) {
            this.prof_id = prof_id;
        }

        public String getScore_label() {
            return score_label;
        }

        public void setScore_label(String score_label) {
            this.score_label = score_label;
        }

        public String getName_normal() {
            return name_normal;
        }

        public void setName_normal(String name_normal) {
            this.name_normal = name_normal;
        }
    }

    public static class IconBean {
        private String report;
        private String comment;
        private String praise;

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }
    }

    public static class FlagBean {
        private String fire;
        private String voice;
        private String location;

        public String getFire() {
            return fire;
        }

        public void setFire(String fire) {
            this.fire = fire;
        }

        public String getVoice() {
            return voice;
        }

        public void setVoice(String voice) {
            this.voice = voice;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
