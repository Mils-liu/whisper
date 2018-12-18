package com.mils.whisper.home.user.presenter;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.net.URL;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/6/14.
 */

public interface UserInterface {

    interface View {
        void loading(String loadMsg);
        void loadSuccess();
        void loadFailure(String loadfailMsg);
        void reloadUserInform();
    }

    interface Presenter {
        void onUpload(File file, int setting);
        void openAlbum(Context context);
        void openCamera(Context context, File imageFile);
        void cropImage(Context context, Uri sourceUri, File outputFile, int aspextX, int aspextY, int maxX, int maxY);
    }

    interface Interactor {
        void doUpload(File file, UploadCallback callback);
        interface UploadCallback{
            void onSuccess(String url);
            void onFailure();
        }
        void doUploadFile(File file, UploadFileCallback callback);
        interface UploadFileCallback{
            void onSuccess(BmobFile file);
            void onFailure();
        }
        void doUserUpdate(String inform, int setting, UpdataCallback callback);
        interface UpdataCallback{
            void onStringSuccess(String inform);
            void onIntegerSuccess(Integer inform);
            void onFailure();
        }
        void doUserUpdateInteger(Integer inform, int setting, UpdataCallback callback);
        void fetchUserInfo(FetchCallback callback);
        interface FetchCallback{
            void onSuccess();
            void onFailure();
        }
    }
}
