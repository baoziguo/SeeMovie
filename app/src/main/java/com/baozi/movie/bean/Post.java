package com.baozi.movie.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by baozi on 2016/8/23.
 */
public class Post extends BmobObject {

    private String title;//帖子标题

    private String content;// 帖子内容

    private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private String imageUrl;//帖子图片

    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户

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

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
