package com.mils.whisper.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/6/4.
 */

public class Article extends BmobObject{
    public static int PROGRESS_VIEW=1;
    public static int END_VIEW=2;
    //文字封面
    private BmobFile articleCover;
    //文字标题
    private String  title;
    //文字内容
    private String  content;
    //文字作者
    private User author;
    //点赞文字的数量
    private Integer  likeNum;
    //点赞文字的用户
    private BmobRelation likes;
    //收藏文字的数量
    private Integer  collectNum;
    //收藏文字的用户
    private BmobRelation collects;
    //文字类型
    private int type;
    //文字作者名
    private String authorName;
    //文字作者ObjectId
    private String authorObjectId;

    public String getAuthorObjectId() {
        return authorObjectId;
    }

    public void setAuthorObjectId(String authorObjectId) {
        this.authorObjectId = authorObjectId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Article(){}

    public Article(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BmobFile getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(BmobFile articleCoverCover) {this.articleCover = articleCoverCover;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public BmobRelation getCollects() {
        return collects;
    }

    public void setCollects(BmobRelation collects) {
        this.collects = collects;
    }
}
