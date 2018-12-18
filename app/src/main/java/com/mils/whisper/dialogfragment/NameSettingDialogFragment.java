package com.mils.whisper.dialogfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnNameSettingListener;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/23.
 */

public class NameSettingDialogFragment extends BaseDialogFragment {

    @BindView(R.id.edt_name)
    public EditText edt_name;
    @BindView(R.id.txt_ensure_name)
    public TextView txt_ensure;
    @BindView(R.id.txt_cancel_name)
    public TextView txt_cancel;

    private OnNameSettingListener listener;
    private String TAG = "NameSetting";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialog_gravity(Gravity.CENTER);
        setWidthPercent(0.8f);
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_name_setting;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    @Override
    protected void initData(Bundle bundle) {
        super.initData(bundle);
        String username = bundle.getString("username");
        Log.d(TAG,"username:"+username);
        edt_name.setText(username);
        /*将光标设置到内容最后*/
        edt_name.setSelection(username.length());
        Log.d(TAG,"editname:"+edt_name.getText());
    }

    public void setOnNameSettingListener(OnNameSettingListener listener){
        this.listener = listener;
    }

    @Override
    protected Boolean setOntouchCancel() {
        return false;
    }

    @OnClick(R.id.txt_ensure_name)
    public void usernameSetting(){
        String username = edt_name.getText().toString();
        if(!isEmpty(username)){
            listener.onSetName(username);
        }else {
            ToastShort(getResources().getString(R.string.username_warn));
        }
    }

    @OnClick(R.id.txt_cancel_name)
    public void usernameCancel(){
        dismiss();
    }
}
