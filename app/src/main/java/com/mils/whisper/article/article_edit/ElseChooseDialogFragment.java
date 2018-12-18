package com.mils.whisper.article.article_edit;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.bean.Article;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/11/28.
 */

public class ElseChooseDialogFragment extends BaseDialogFragment {

    @BindView(R.id.btn_delete_article)
    public Button btn_delete;
    @BindView(R.id.btn_cancel_articel)
    public Button btn_cancel;
    private String objectId;
    private String TAG = "elsechoose";
    private OnArticleDeleteListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWidthPercent(1);
        setDialog_gravity(Gravity.BOTTOM);

        Bundle bundle = getArguments();
        if(bundle != null){
            objectId = bundle.getString("objectId");
            Log.d(TAG,"objectId:"+objectId);
        }
    }

    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(
                R.style.DialogAnimation);
    }

    public void setOnArticleDeleteListener(OnArticleDeleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean setOntouchCancel() {
        return true;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_else_choose;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    @OnClick(R.id.btn_cancel_articel)
    public void articleCancel(){
        dismiss();
    }

    @OnClick(R.id.btn_delete_article)
    public void articleDelete(){
        if (objectId != null){
            Article article = new Article();
            article.setObjectId(objectId);
            article.delete(new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        ToastShort("删除成功");
                        dismiss();
                        listener.onArticleDelete();
                    }else{
                        ToastShort("删除失败！！！");
                    }
                }

            });
        }
    }

    public interface OnArticleDeleteListener{
        void onArticleDelete();
    }
}
