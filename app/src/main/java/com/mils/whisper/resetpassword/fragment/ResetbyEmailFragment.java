package com.mils.whisper.resetpassword.fragment;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/30.
 */

public class ResetbyEmailFragment extends BaseFragment {

    @BindView(R.id.edt_emailAdress)
    public EditText edt_emailAdress;
    @BindView(R.id.btn_sendmail)
    public Button btn_sendmail;
    @BindView(R.id.txt_hint)
    public TextView txt_hint;

    FragmentListener fragmentListener;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_reset_password_email;
    }

    @Override
    protected void initView() {
        super.initView();
        String string="我们会即时向你的邮箱发送一封电子邮件\n" +
                "请点击邮箱里的链接进入重置密码的界面\n" +
                "根据提示输入新密码，点击确认即可重置";
        txt_hint.setText(string);
    }

    @OnClick(R.id.btn_sendmail)
    public void toResetPassword(){
        if(TimeUtil.fastClick()){
            mActivity.finish();
            Log.d(BaseActivity.TAG,BaseActivity.TAG);
        }
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
    }

    @Override
    protected void setListener() {
        super.setListener();
        fragmentListener= (FragmentListener) getActivity();
    }

    //设置用于修改文本的回调接口
    public interface FragmentListener{
        void ActivityFinish();
    }
}
