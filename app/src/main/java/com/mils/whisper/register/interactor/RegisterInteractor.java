package com.mils.whisper.register.interactor;

import android.util.Log;

import com.mils.whisper.bean.User;
import com.mils.whisper.register.presenter.RegisterInterface;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/6/5.
 */

public class RegisterInteractor implements RegisterInterface.Interactor {

    private String TAG = "RegisterInteractor";
    /*用户注册*/
    @Override
    public void doRegister(final String username, final String password,
                           Integer sex, final RegisterCallback callback) {

        User user = new User();
        user.setBrief("这个人很懒，什么都没留下");
        user.setUserBackground("http://bmob-cdn-19980.b0.upaiyun.com/2018/06/28/04e8f3b659eb4c16bdd10bd2126547a3.jpg");
        user.setHead("http://bmob-cdn-19980.b0.upaiyun.com/2018/06/20/316196f5f674468896a553c6ebfcf412.jpg");
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setFocusNum(0);
        user.setFans(null);
        user.setFocusUser(null);
        user.setCollectArticle(null);
        user.setLikeArticle(null);
        user.signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e==null){
                                callback.onSuccess();
                            }else {
                                Log.d(TAG,"loginError:"+e);
                                callback.onFailure();
                            }
                        }
                    });
                } else {
                    Log.d(TAG,"registerError:"+e);
                    callback.onFailure();
                }
            }
        });
    }
}
