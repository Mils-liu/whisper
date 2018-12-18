package com.mils.whisper.accountsetting.interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mils.whisper.R;
import com.mils.whisper.accountsetting.presenter.SettingInterface;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static com.mils.whisper.dialogfragment.PictureDialogFragment.ACTION_CHOOSE;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.TAKE_PHOTO;

/**
 * Created by Administrator on 2018/6/19.
 */

public class SettingInteractor implements SettingInterface.Interactor{

    private String TAG = "SettingInteractor";

    @Override
    public void doOpenAlbum(Context context) {
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        /*打开文件*/
        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity)context).startActivityForResult(intent2, ACTION_CHOOSE);
        Log.d(TAG,"doOpenAlbum");
    }

    @Override
    public void doOpenCamera(Context context, Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        ((Activity)context).startActivityForResult(intent, TAKE_PHOTO); //启动照相
        Log.d(TAG,"doOpenCamera");
    }

    @Override
    public void doCrop(Context context, Uri sourceUri, File outputFile, int x, int y, int maxX, int maxY) {
        Uri outputUri = Uri.fromFile(outputFile);
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(context.getResources().getColor(R.color.darkgreen));
        // 修改状态栏颜色
        options.setStatusBarColor(context.getResources().getColor(R.color.darkgreen));
        //修改底部控件颜色
        options.setActiveWidgetColor(context.getResources().getColor(R.color.darkgreen));
        /*设置隐藏底部容器，默认显示*/
        /*options.setHideBottomControls(true);*/
        // 设置图片压缩质量
        options.setCompressionQuality(100);
        UCrop.of(sourceUri, outputUri)
                .withAspectRatio(x, y)
                .withMaxResultSize(maxX, maxY)
                .withOptions(options)
                .start((Activity) context);
    }
}
