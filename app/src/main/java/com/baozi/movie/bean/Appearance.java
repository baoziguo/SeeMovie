package com.baozi.movie.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by baozi on 2016/8/23.
 */
public class Appearance extends BmobObject {

    private String title;//帖子标题

    private String content;// 帖子内容

    private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private String imageUrl;//帖子图片

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
