package com.mils.whisper;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;
import com.mils.whisper.home.HomeActivity;
import com.mils.whisper.login.LoginActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/5/21.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setSteepStatusBar(true);
        toWhere();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    private void toWhere() {
        final BmobUser user = BmobUser.getCurrentUser(User.class);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (user != null) {
                    Log.d(TAG,"UNSER");
                    startActivity(HomeActivity.class);
                } else {
                    Log.d(TAG,"UNSERNULL");
                    startActivity(LoginActivity.class);
                }
                finish();
            }
        }, 3000);
    }
}
