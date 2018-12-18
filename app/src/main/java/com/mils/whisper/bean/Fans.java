package com.mils.whisper.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/12/12.
 */

public class Fans extends BmobObject{
    String user_ObjectId;
    Integer fansNum;
    BmobRelation fansList;

    public String getUser_ObjectId() {
        return user_ObjectId;
    }

    public void setUser_ObjectId(String user_ObjectId) {
        this.user_ObjectId = user_ObjectId;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public BmobRelation getFansList() {
        return fansList;
    }

    public void setFansList(BmobRelation fansList) {
        this.fansList = fansList;
    }
}
