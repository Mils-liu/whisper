package com.mils.whisper.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/5.
 */

public class RegisterByPhoneActivity extends BaseActivity {

    @BindView(R.id.edt_user_phonenumber)
    public EditText edt_phonenumber;
    @BindView(R.id.btn_getcode)
    public Button btn_getcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phonenumber);
        setSteepStatusBar(true);
    }

    @OnClick(R.id.btn_getcode)
    public void SendCode() {
        if (TimeUtil.fastClick()) {
            if (isEmpty(edt_phonenumber.getText())) {
                ToastShort("手机号不能为空");
            } else {
                BmobSMS.requestSMSCode(edt_phonenumber.getText().toString(), "",
                        new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                if (ex == null) {//验证码发送成功
                                    toRegister();
                                } else {
                                    Log.d(TAG,"registerError:"+ex);
                                    ToastShort("验证失败");
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    private void toRegister() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", edt_phonenumber.getText().toString());
        startActivity(RegisterActivity.class, bundle);
    }
}
