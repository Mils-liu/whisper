package com.mils.whisper.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;
import com.mils.whisper.login.LoginActivity;
import com.mils.whisper.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/5/30.
 */

public class DoResetActivity extends BaseActivity {

    @BindView(R.id.btn_reset_succed)
    Button btn_reset;
    @BindView(R.id.edt_newPassword)
    EditText edt_newPassword;
    @BindView(R.id.edt_newPassword_confirm)
    EditText edt_newPassword_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doreset);
        setSteepStatusBar(true);
    }

    @OnClick(R.id.btn_reset_succed)
    public void BackLogin(){
        Loading(getResources().getString(R.string.loading_data));
        doReset();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    private void doReset(){
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        User user = new User();
        String password = edt_newPassword.getText().toString().trim();
        String password_confirm = edt_newPassword_confirm.getText().toString().trim();

        if (isEmpty(password)|| isEmpty(password_confirm)){
            LoadFailure("密码不能为空");
        }else if (!password.equals(password_confirm)){
            LoadFailure("密码不一致");
        }else {
            if (password.length()<6){
                LoadFailure("密码长度需大于6");
            }else {
                user.setPassword(edt_newPassword_confirm.getText().toString());
                user.update(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            LoadSuccess();
                            ToastUtil.ToastShort("密码修改成功");
                            finish();
                        }else {
                            LoadFailure("密码修改失败，请检查网络！");
                            Log.d(TAG,"error:"+e);
                        }
                    }
                });
            }
        }
    }
}
