package com.mils.whisper.resetpassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/30.
 */

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.btn_sendSMS)
    public Button btn_sendSMS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reset_password_phone);
        setSteepStatusBar(true);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.btn_sendSMS)
    public void toResetPassword(){
        if (TimeUtil.fastClick()){
            startActivity(DoResetActivity.class);
        }
    }
}
