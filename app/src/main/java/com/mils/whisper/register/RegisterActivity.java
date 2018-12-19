package com.mils.whisper.register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.activitymanager.ActivityCollector;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.dialogfragment.PictureDialogFragment;
import com.mils.whisper.dialogfragment.SexDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnSexSelectListener;
import com.mils.whisper.home.HomeActivity;
import com.mils.whisper.register.presenter.RegisterInterface;
import com.mils.whisper.register.presenter.RegisterPresenter;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.activitymanager.ActivityCollector.ACTIVITY_REGISTER;
import static com.mils.whisper.dialogfragment.SexDialogFragment.FEMALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.MALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.UNCERTAIN;
import static com.mils.whisper.util.EditFilterUtil.setEditTextInhibitInputSpace;
import static com.mils.whisper.util.ToastUtil.ToastShort;

public class  RegisterActivity extends BaseActivity implements OnSexSelectListener,RegisterInterface.View{

    @BindView(R.id.edt_user_name)
    public EditText edt_username;
    @BindView(R.id.edt_password)
    public EditText edt_password;
    @BindView(R.id.edt_password_confirm)
    public EditText edt_password_confirm;
    @BindView(R.id.txt_sex_choose)
    public TextView txt_sex_choose;
    @BindView(R.id.btn_register)
    public Button btn_register;

    private SexDialogFragment sexDialog;
    private RegisterInterface.Presenter mPresenter;
    private int sex_choose = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        setSteepStatusBar(true);
        mPresenter = new RegisterPresenter(this);
        /*用户名禁止输入空格和换行键*/
        setEditTextInhibitInputSpace(edt_username);
    }

    @OnClick(R.id.btn_register)
    public void doRegister(){
        if(TimeUtil.fastClick()){
            mPresenter.onRegister(edt_username.getText().toString(), edt_password.getText().toString(),
                    edt_password_confirm.getText().toString(), sex_choose);
        }
    }

    @OnClick(R.id.txt_sex_choose)
    public void sexChoose(){
        if(TimeUtil.fastClick()){
            sexDialog.show(getFragmentManager(),"dialog_fragment");
            Bundle bundle = new Bundle();
            if(sex_choose == -1){
                bundle.putInt("sex", UNCERTAIN);
            }else {
                bundle.putInt("sex", sex_choose);
            }
            sexDialog.setArguments(bundle);
            sexDialog.setOnSexSelectListener(this);
        }
    }

    @Override
    public void initView() {
        sexDialog = new SexDialogFragment();
    }

    @Override
    public void initData() {

    }

    @Override
    public void toHome() {
        Bundle bundle = new Bundle();
        bundle.putInt("from",ACTIVITY_REGISTER);
        startActivity(HomeActivity.class,bundle);
        ActivityCollector.finishAll();
    }

    public void registerLoading(){
        Loading("注册中。。。");
    }

    @Override
    public void registerEnd(String msg) {
        if(isEmpty(msg)){
            LoadSuccess();//注册成功
        }else {
            LoadFailure(msg);//注册失败
        }
    }

    @Override
    public void OnSetSex(int sex) {
        switch(sex){
            case MALE:
                txt_sex_choose.setText(R.string.male);
                ToastShort("性别："+getResources().getString(R.string.male));
                sex_choose = sex;
                break;
            case FEMALE:
                txt_sex_choose.setText(R.string.female);
                ToastShort("性别："+getResources().getString(R.string.female));
                sex_choose = sex;
                break;
            case UNCERTAIN:
                txt_sex_choose.setText(R.string.uncertain);
                ToastShort("性别："+getResources().getString(R.string.uncertain));
                sex_choose = sex;
                break;
            default:
                break;
        }
    }
}
