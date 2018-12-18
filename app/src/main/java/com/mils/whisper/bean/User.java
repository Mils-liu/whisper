package com.mils.whisper.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/6/4.
 */

public class User extends BmobUser implements Serializable {
    //头像文件
    private String  head;
    //个人封面文件
    private String userBackground;
    //性别
    private Integer sex;
    //个人简介
    private String brief;
    //关注数
    private Integer  focusNum;
    //关注
    BmobRelation focusUser;
    //收藏的文字
    BmobRelation collectArticle;
    //点赞的文字
    BmobRelation likeArticle;
    //粉丝
    Fans fans;
    //收藏的文字ObjectId
    List<String> collectList;
    //点赞的文字ObjectId
    List<String> likeList;

    public List<String> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<String> collectList) {
        this.collectList = collectList;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public BmobRelation getCollectArticle() {
        return collectArticle;
    }

    public void setCollectArticle(BmobRelation collectArticle) {
        this.collectArticle = collectArticle;
    }

    public BmobRelation getLikeArticle() {
        return likeArticle;
    }

    public void setLikeArticle(BmobRelation likeArticle) {
        this.likeArticle = likeArticle;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getUserBackground() {
        return userBackground;
    }

    public void setUserBackground(String userBackground) {
        this.userBackground = userBackground;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Fans getFans() {
        return fans;
    }

    public void setFans(Fans fans) {
        this.fans = fans;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public BmobRelation getFocusUser() {
        return focusUser;
    }

    public void setFocusUser(BmobRelation focusUser) {
        this.focusUser = focusUser;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
