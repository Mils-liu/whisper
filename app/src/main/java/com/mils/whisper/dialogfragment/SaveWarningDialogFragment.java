package com.mils.whisper.dialogfragment;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnArticleSaveListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/9.
 */

public class SaveWarningDialogFragment extends BaseDialogFragment {

    @BindView(R.id.txt_ensure_savewarning)
    public TextView txt_ensure;
    @BindView(R.id.txt_quit_savewarning)
    public TextView txt_quit;

    private OnArticleSaveListener listener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialog_gravity(Gravity.CENTER);
        setWidthPercent(0.8f);
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_articlesave_warning;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    @Override
    protected Boolean setOntouchCancel() {
        return true;
    }

    public void setListener(OnArticleSaveListener listener){
        this.listener = listener;
    }

    @OnClick(R.id.txt_ensure_savewarning)
    public void articleSave(){
        listener.OnArticleSave();
    }

    @OnClick(R.id.txt_quit_savewarning)
    public void articleQuit(){
        listener.OnArticleQuit();
    }
}
