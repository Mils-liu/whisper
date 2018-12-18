package com.mils.whisper.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.app.MyApplicant;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;
import com.mils.whisper.home.HomeActivity;
import com.mils.whisper.register.RegisterActivity;
import com.mils.whisper.resetpassword.ResetPasswordActivity;
import com.mils.whisper.util.SharedPreferencesUtil;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/5/21.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_username_login)
    public EditText edt_username;
    @BindView(R.id.edt_password_login)
    public EditText edt_password;
    @BindView(R.id.btn_login)
    public Button btn_login;
    @BindView(R.id.txt_password_forget)
    public TextView txt_password_forget;
    @BindView(R.id.txt_register)
    public TextView txt_register;

    private SharedPreferencesUtil prefUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSteepStatusBar(true);

    }

    @OnClick(R.id.txt_register)
    public void toRegister(){
        if(TimeUtil.fastClick()){
            startActivity(RegisterActivity.class);
        }
    }
    @OnClick(R.id.txt_password_forget)
    public void toReset(){
        if(TimeUtil.fastClick()){
            startActivity(ResetPasswordActivity.class);
        }
    }
    @OnClick(R.id.btn_login)
    public void toHome(){
        if(TimeUtil.fastClick()){
            loagin(edt_username.getText().toString(),edt_password.getText().toString());
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {}

    private void loagin(String username, String password){
        if (isEmpty(username)){
            ToastShort("用户名不能为空");
        }else if(isEmpty(password)){
            ToastShort("密码不能为空");
        }else {
            Loading("登录中...");
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(user!=null){
                        MyApplicant.user = BmobUser.getCurrentUser(User.class);
                        saveGlideSignature();
                        startActivity(HomeActivity.class);
                        LoadSuccess();
                        finish();
                    }else {
                        Log.d(TAG,"LoginError:"+e);
                        LoadFailure("手机号与密码不匹配");
                    }
                }
            });
        }
    }

    /*保存Glide更新时间标识*/
    private void saveGlideSignature(){
        prefUtil = new SharedPreferencesUtil("GlideSignature");
        prefUtil.put("headSignature",String.valueOf(System.currentTimeMillis()));
        prefUtil.put("bgSignature",String.valueOf(System.currentTimeMillis()));
    }
}
