package com.mils.whisper.home.user.interactor;

import android.util.Log;

import com.mils.whisper.bean.User;
import com.mils.whisper.home.user.presenter.UserInterface;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.mils.whisper.app.MyApplicant.user;
import static com.mils.whisper.config.UserInformConfig.BRIEF;
import static com.mils.whisper.config.UserInformConfig.FOCUSNUM;
import static com.mils.whisper.config.UserInformConfig.SEX;
import static com.mils.whisper.config.UserInformConfig.USERNAME;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.BACKGROUND;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.HEAD;

/**
 * Created by Administrator on 2018/6/14.
 */

public class UserInteractor implements UserInterface.Interactor{

    private String TAG = "UserInteractor";

    public void doUploadFile(File file, final UploadFileCallback callback){
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callback.onSuccess(bmobFile);
                }else {
                    callback.onFailure();
                }
            }
        });
    }

    /*上传文件*/
    @Override
    public void doUpload(File file, final UploadCallback callback) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    callback.onSuccess(bmobFile.getFileUrl());
                }else{
                    callback.onFailure();
                }

            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /*更新用户信息*/
    @Override
    public void doUserUpdate(final String inform,int setting,final UpdataCallback callback) {
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        User newUser=new User();
        Log.d(TAG,inform);
        if (setting == HEAD){
            newUser.setHead(inform);
        } else if(setting ==BACKGROUND){
            newUser.setUserBackground(inform);
        } else if (setting == USERNAME){
            newUser.setUsername(inform);
        } else if (setting == BRIEF){
            newUser.setBrief(inform);
        }
        if(currentUser!=null){
            newUser.update(currentUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        callback.onStringSuccess(inform);
                    }else {
                        callback.onFailure();
                    }
                }
            });
        }
    }

    @Override
    public void doUserUpdateInteger(final Integer inform, int setting, final UpdataCallback callback) {
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        User newUser=new User();
        if (setting == SEX){
            newUser.setSex(inform);
        }
        if (setting == FOCUSNUM){
            newUser.setFocusNum(inform);
        }
        if (currentUser != null){
            newUser.update(currentUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        callback.onIntegerSuccess(inform);
                    }else {
                        callback.onFailure();
                    }
                }
            });
        }
    }

    /*同步本地用户信息*/
    @Override
    public void fetchUserInfo(final FetchCallback callback) {
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser!=null){
            Log.d(TAG,"fetch");
            currentUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        callback.onSuccess();
                        Log.d(TAG,"fetchSuccess");
                    } else {
                        callback.onFailure();
                        Log.d(TAG,"fetchFailure");
                    }
                }
            });
        }
    }
}
