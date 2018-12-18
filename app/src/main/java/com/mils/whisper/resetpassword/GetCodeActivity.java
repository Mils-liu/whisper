package com.mils.whisper.resetpassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/31.
 */

public class GetCodeActivity extends BaseActivity {

    @BindView(R.id.edt_code)
    public EditText edt_code;
    @BindView(R.id.btn_sendcode)
    public Button btn_sendcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getcode);
        setSteepStatusBar(true);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.btn_sendcode)
    public void toDoReset(){
        startActivity(DoResetActivity.class);
    }
}
