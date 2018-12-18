package com.mils.whisper.article.article_edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.mils.whisper.R;
import com.mils.whisper.article.article_edit.presenter.ArticleEditInterface;
import com.mils.whisper.article.article_edit.presenter.ArticleEditPresenter;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;
import com.mils.whisper.dialogfragment.PictureDialogFragment;
import com.mils.whisper.dialogfragment.SaveWarningDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnArticleSaveListener;
import com.mils.whisper.dialogfragment.listener.OnPicSelectListener;
import com.mils.whisper.util.FileUtil;
import com.mils.whisper.util.TimeUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ACTION_CHOOSE;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ALBUM;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.CAMERA;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.TAKE_PHOTO;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageFile_article;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageFile_bg;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;

/**
 * Created by Administrator on 2018/6/2.
 */

public class EditArticleActivity extends BaseActivity implements OnPicSelectListener,OnArticleSaveListener,ArticleEditInterface.View{

    @BindView(R.id.fl_addcover)
    public FrameLayout fl_addcover;
    @BindView(R.id.img_addCover)
    public ImageView img_addcover;
    @BindView(R.id.edt_addtitle)
    public EditText edt_addtitle;
    @BindView(R.id.edt_edit)
    public EditText edt_edit;
    @BindView(R.id.txt_save)
    public TextView txt_save;
    @BindView(R.id.txt_release)
    public TextView txt_release;
    @BindView(R.id.txt_addtextHint)
    public TextView txt_addtextHint;

    private ArticleEditPresenter mPresenter;
    private PictureDialogFragment picDialog;
    private SaveWarningDialogFragment saveDialog;
    private File articleCover;
    private int width;
    private int height;
    public static Boolean coverExistence = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);
        mPresenter = new ArticleEditPresenter(this);

        /*动态设置封面高度*/
        ViewTreeObserver vto = fl_addcover.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            public void onGlobalLayout() {
                width = fl_addcover.getWidth();
                height = width * 309 / 500;
                fl_addcover.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
            }
        });
    }

    @Override
    public void initView() {
        picDialog = new PictureDialogFragment();
        saveDialog = new SaveWarningDialogFragment();
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_addcover)
    public void addcover(){
        if(TimeUtil.fastClick()){
            picDialog.show(getFragmentManager(), "pic_dialog");
            picDialog.setOnPicSelectListener(this);
        }
    }

    @Override
    public void OnSetPic(int pic) {
        switch (pic){
            case ALBUM:
                mPresenter.openAlbum(this);
                break;
            case CAMERA:
                mPresenter.openCamera(this,imageFile_article);
                break;
        }
    }

    @Override
    public void OnArticleSave() {

    }

    @Override
    public void OnArticleQuit() {
        saveDialog.dismiss();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.d(TAG,"NULL");
            return;
        }else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d(TAG, "error:" + cropError);
        }
        switch (requestCode) {
            case TAKE_PHOTO:  //拍照的回调
                mPresenter.cropImage(this,imageUri, imageFile_bg,
                        width, height, width, height);
                break;
            case ACTION_CHOOSE:  //选择照片的回调
                imageUri = data.getData();
                Log.d(TAG,"imgUri:"+imageUri);
                mPresenter.cropImage(this,imageUri, imageFile_bg,
                        width, height, width, height);
                break;
            case UCrop.REQUEST_CROP:
                Log.d(TAG,"cropreturn");
                Uri resultUri = UCrop.getOutput(data);
                articleCover = FileUtil.getFileByUri(resultUri);
                Glide.with(this)
                        .load(articleCover)
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(img_addcover);
                txt_addtextHint.setVisibility(View.GONE);
                coverExistence = true;
                Log.d(TAG,"coverExistence:"+coverExistence);
            default:
                break;
        }
    }

    @OnClick(R.id.txt_release)
    public void doRelease(){
        String title = edt_addtitle.getText().toString();
        String content = edt_edit.getText().toString();
        User user = BmobUser.getCurrentUser(User.class);
        Log.d(TAG,"username:"+user.getUsername());
        mPresenter.onArticleUpdate(articleCover, title, content, user, new ArticleEditInterface.Presenter.ArticleUpdateCallback() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onFailure() {

            }
        });
        Log.d(TAG,"release");
    }

    @Override
    public void loading(String loadMsg) {
        Loading(loadMsg);
    }

    @Override
    public void loadFailure(String loadfailMsg) {
        LoadFailure(loadfailMsg);
    }

    @Override
    public void loadSuccess() {
        LoadSuccess();
    }

    @Override
    public void onBackPressed() {
        if (checkArticleEmpty()){
            saveDialog.show(getFragmentManager(), "save_dialog");
            saveDialog.setListener(this);
        }else {
            super.onBackPressed();
        }
    }

    private Boolean checkArticleEmpty(){
        if (!isEmpty(edt_addtitle.getText().toString())||!isEmpty(edt_edit.getText().toString())||coverExistence){
            return true;
        }else {
            return false;
        }
    }
}
