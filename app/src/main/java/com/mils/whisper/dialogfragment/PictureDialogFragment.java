package com.mils.whisper.dialogfragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnPicSelectListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/8.
 */

public class PictureDialogFragment extends BaseDialogFragment {

    @BindView(R.id.fl_camera)
    public FrameLayout fl_camera;
    @BindView(R.id.fl_album)
    public FrameLayout fl_album;
    @BindView(R.id.img_red_camera)
    public ImageView img_red;
    @BindView(R.id.img_camera)
    public ImageView img_camera;
    @BindView(R.id.img_blue_albume)
    public ImageView img_blue;
    @BindView(R.id.img_albume)
    public ImageView img_albume;
    @BindView(R.id.txt_ensure_pic)
    public TextView txt_ensure;

    private int pic_select;
    private OnPicSelectListener listener;

    public static File imageFile_head;
    public static File imageFile_bg;
    public static File imageFile_article;
    public static Uri imageUri;

    public static final int CAMERA = 0;
    public static final int ALBUM = 1;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int ACTION_CHOOSE=3;
    public static final int BACKGROUND=1000;
    public static final int HEAD=1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialog_gravity(Gravity.CENTER);
        setWidthPercent(0.8f);
    }

    @Override
    protected void initData(Bundle bundle) {
        super.initData(bundle);
        imageFile_bg = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "background.jpg");
        imageFile_head = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "head.jpg");
        imageFile_article = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "article.jpg");
        pic_select = CAMERA;
        PicSelect(pic_select);
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_picture_from;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    @Override
    protected Boolean setOntouchCancel() {
        return true;
    }

    public void setOnPicSelectListener(OnPicSelectListener listener){
        this.listener = listener;
    }

    @OnClick(R.id.fl_album)
    public void album_choose(){
        pic_select = ALBUM;
        PicSelect(pic_select);
    }

    @OnClick(R.id.fl_camera)
    public void camera_choose(){
        pic_select = CAMERA;
        PicSelect(pic_select);
    }

    @OnClick(R.id.txt_ensure_pic)
    public void pic_ensure(){
        listener.OnSetPic(pic_select);
        dismiss();
    }

    protected void PicSelect(int pic){

        img_camera.setSelected(false);
        img_blue.setSelected(false);
        img_albume.setSelected(false);
        img_red.setSelected(false);

        switch (pic){
            case CAMERA:
                img_camera.setSelected(true);
                img_red.setSelected(true);
                break;
            case ALBUM:
                img_albume.setSelected(true);
                img_blue.setSelected(true);
                break;
            default:
                break;
        }
    }
}
