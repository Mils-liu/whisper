package com.mils.whisper.focus;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mils.whisper.R;
import com.mils.whisper.bean.Fans;
import com.mils.whisper.bean.User;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/12/19.
 */

public class Focus {

    private static String TAG = getContext().getClass().getSimpleName();

    /*关注*/
    public static void doFocus(final User myUser, final User focusUser, final Button btn_focus) {
        Log.d(TAG, "userObjectId:" + focusUser.getObjectId());
        /*添加关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(focusUser);
        user.setFocusUser(relation);
                /*关注数加1*/
        user.increment("focusNum", 1);
        user.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null || e.getErrorCode() == 9015) {
                    addFan(myUser, focusUser, btn_focus);
                } else {
                    Log.d(TAG, "error1:" + e.getMessage());
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*取关*/
    public static void cancelFocus(final User myUser, final User focusUser, final Button btn_focus) {
                /*删除关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(focusUser);
        user.setFocusUser(relation);
                /*关注数减1*/
        user.increment("focusNum", -1);
        user.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    deleteFan(myUser, focusUser,btn_focus);
                } else {
                    Log.d(TAG, "error1:" + e.getMessage());
                    ToastShort("取关失败！");
                }
            }
        });
    }

    /*被关注用户添加粉丝*/
    public static void addFan(User myUser, User focusUser, final Button btn_focus) {
        Fans fans = new Fans();
        String fansObject = focusUser.getFans().getObjectId();
        fans.setObjectId(fansObject);
        Log.d(TAG, "fans_objectId:" + fansObject);
                /*User user = new User();
                user.setObjectId(myUser.getObjectId());*/
        Log.d(TAG, "myUser_objectId:" + myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum", 1);
        fans.update(fansObject, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    focus(btn_focus);
                    ToastShort("关注成功");
                } else {
                    Log.d(TAG, "error2:" + e);
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*被关注用户删除粉丝*/
    public static void deleteFan(final User myUser, final User focusUser, final Button btn_focus) {
        Fans fans = new Fans();
        fans.setObjectId(focusUser.getFans().getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum", -1);
        fans.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    unFocus(btn_focus);
                    ToastShort("取关成功");
                } else {
                    Log.d(TAG, "error2:" + e);
                    ToastShort("取关失败！");
                }
            }
        });
    }

    public static void focus(Button btn_focus) {
        btn_focus.setBackgroundResource(R.drawable.shape_rounded_rectangle_unselect);
        btn_focus.setTextColor(getContext().getResources().getColor(R.color.darkgray));
        btn_focus.setText(getContext().getResources().getString(R.string.cancelfocus));
    }

    public static void unFocus(Button btn_focus) {
        btn_focus.setBackgroundResource(R.drawable.shape_rounded_rectangle_select);
        btn_focus.setTextColor(getContext().getResources().getColor(R.color.white));
        btn_focus.setText(getContext().getResources().getString(R.string.dofocus));
    }
}
