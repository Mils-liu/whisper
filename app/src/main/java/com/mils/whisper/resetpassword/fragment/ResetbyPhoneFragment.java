package com.mils.whisper.resetpassword.fragment;

import android.widget.Button;
import android.widget.EditText;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.resetpassword.GetCodeActivity;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/30.
 */

public class ResetbyPhoneFragment extends BaseFragment {

    @BindView(R.id.edt_phoneNumber)
    public EditText edt_phoneNumber;
    @BindView(R.id.btn_sendSMS)
    public Button btn_sendSMS;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_reset_password_phone;
    }

    @OnClick(R.id.btn_sendSMS)
    public void toResetPassword(){
        if (TimeUtil.fastClick()){
            startActivity(GetCodeActivity.class);
        }
    }
}
