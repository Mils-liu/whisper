package com.mils.whisper.accountsetting.presenter;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2018/6/19.
 */

public interface SettingInterface {

    interface View{
        void reloadUserInform();
        void loading(String loadMsg);
        void loadSuccess();
        void loadFailure(String loadfailMsg);
    }

    interface Presenter{
        void openAlbum(Context context);
        void openCamera(Context context, File imageFile);
        void cropImage(Context context, Uri sourceUri, File outputFile,
                       int aspextX, int aspextY, int maxX, int maxY);
        void onUpload(File file, int setting);
        void onUserUpdateString(String inform, int setting);
        void onUserUpdateInteger(Integer inform, int setting);
    }

    interface Interactor{
        void doOpenAlbum(Context context);
        void doOpenCamera(Context context, Uri imageUri);
        void doCrop(Context context, Uri sourceUri, File outputFile, int aspextX, int aspextY, int maxX, int maxY);
    }
}
