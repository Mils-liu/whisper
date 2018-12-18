package com.mils.whisper.register.presenter;

import com.mils.whisper.register.interactor.RegisterInteractor;
import com.mils.whisper.util.SharedPreferencesUtil;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.dialogfragment.SexDialogFragment.FEMALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.MALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.UNCERTAIN;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/5.
 */

public class RegisterPresenter implements RegisterInterface.Presenter {

    private RegisterInterface.View mView;
    private RegisterInterface.Interactor mInteractor;
    private SharedPreferencesUtil prefUtil;

    public RegisterPresenter(RegisterInterface.View view) {
        mView = view;
        mInteractor = new RegisterInteractor();
        prefUtil = new SharedPreferencesUtil("GlideSignature");
    }

    @Override
    public void onRegister(String username, String password,
                           String password_confirm, Integer sex) {
        if (isEmpty(username)) {
            ToastShort("用户名不能为空");
        } else if (isEmpty(password)) {
            ToastShort("密码不能为空");
        } else if (!password.equals(password_confirm)) {
            ToastShort("密码不一致");
        } else if (sex != UNCERTAIN && sex != MALE && sex != FEMALE) {
            ToastShort("性别不能为空");
        } else {
            mView.registerLoading();
            mInteractor.doRegister(username, password, sex,
                    new RegisterInterface.Interactor.RegisterCallback() {
                        @Override
                        public void onSuccess() {
                            prefUtil.put("headSignature",String.valueOf(System.currentTimeMillis()));
                            prefUtil.put("bgSignature",String.valueOf(System.currentTimeMillis()));
                            mView.toHome();//进入主界面
                            ToastShort("欢迎加入轻语^_^");
                            mView.registerEnd("");
                        }

                        @Override
                        public void onFailure() {
                            mView.registerEnd("注册失败");
                        }
                    });
        }
    }
}
