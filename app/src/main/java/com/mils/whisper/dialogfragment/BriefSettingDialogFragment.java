package com.mils.whisper.dialogfragment;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnBriefSettingListener;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Administrator on 2018/6/25.
 */

public class BriefSettingDialogFragment extends BaseDialogFragment {

    @BindView(R.id.edt_brief)
    public EditText edt_brief;
    @BindView(R.id.txt_ensure_brief)
    public TextView txt_ensure;
    @BindView(R.id.txt_cancel_brief)
    public TextView txt_cancel;

    private OnBriefSettingListener listener;
    private String TAG = "BriefSetting";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialog_gravity(Gravity.CENTER);
        setWidthPercent(0.8f);
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_brief_setting;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    @Override
    protected void initData(Bundle bundle) {
        super.initData(bundle);
        String brief = bundle.getString("brief");
        edt_brief.setText(brief);
        if(!isEmpty(brief)){
            edt_brief.setSelection(brief.length());
        }
    }

    @Override
    protected Boolean setOntouchCancel() {
        return false;
    }

    public void setOnBriefSettingListener(OnBriefSettingListener listener){
        this.listener = listener;
    }

    @OnClick(R.id.txt_ensure_brief)
    public void briefSetting(){
        String brief = edt_brief.getText().toString();
        listener.OnSetBrief(brief);
    }

    @OnClick(R.id.txt_cancel_brief)
    public void briefCancel(){
        dismiss();
    }
}
