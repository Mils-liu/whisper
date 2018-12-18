package com.mils.whisper.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.login.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2018/5/30.
 */

public class DoResetActivity extends BaseActivity {

    @BindView(R.id.btn_reset_succed)
    public Button btn_reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doreset);
        setSteepStatusBar(true);
    }

    @OnClick(R.id.btn_reset_succed)
    public void BackLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
