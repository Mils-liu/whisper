package com.mils.whisper.home.user.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mils.whisper.accountsetting.interactor.SettingInteractor;
import com.mils.whisper.accountsetting.presenter.SettingInterface;
import com.mils.whisper.home.user.interactor.UserInteractor;

import java.io.File;

import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;

/**
 * Created by Administrator on 2018/6/14.
 */

public class UserPresenter implements UserInterface.Presenter {

    private UserInterface.View mView;
    private UserInterface.Interactor mInteractor;
    private SettingInterface.Interactor settingInteractor;
    private String TAG = "UserPresenter";

    public UserPresenter(UserInterface.View view) {
        mView = view;
        mInteractor = new UserInteractor();
        settingInteractor = new SettingInteractor();
    }

    @Override
    public void onUpload(File file,final int type) {
        if (file != null){
            mView.loading("加载数据...");

            mInteractor.doUpload(file, new UserInterface.Interactor.UploadCallback() {
                @Override
                public void onSuccess(String url) {
                    Log.d(TAG, "UploadSuccess");
                    Log.d(TAG, "url:" + url);

                    mInteractor.doUserUpdate(url, type, new UserInterface.Interactor.UpdataCallback() {
                        @Override
                        public void onStringSuccess(String url) {
                            Log.d(TAG, "UpdataSuccess");

                            mInteractor.fetchUserInfo(new UserInterface.Interactor.FetchCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG,"success");
                                    mView.loadSuccess();
                                    mView.reloadUserInform();
                                }

                                @Override
                                public void onFailure() {
                                    Log.d(TAG,"failure");
                                }
                            });
                        }

                        @Override
                        public void onIntegerSuccess(Integer inform) {}

                        @Override
                        public void onFailure() {
                            Log.d(TAG, "UpdataFail");
                            mView.loadFailure("加载失败");
                        }
                    });
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "UploadFail");
                    mView.loadFailure("加载失败");
                }
            });

        }
    }

    @Override
    public void openAlbum(Context context) {
        settingInteractor.doOpenAlbum(context);
        Log.d(TAG,"context:"+context);
    }

    @Override
    public void openCamera(Context context, File imageFile) {
        imageUri = Uri.fromFile(imageFile);
        settingInteractor.doOpenCamera(context,imageUri);
        Log.d(TAG,"context:"+context);
    }

    @Override
    public void cropImage(Context context, Uri sourceUri, File outputFile,int aspextX, int aspextY, int maxX, int maxY) {
        settingInteractor.doCrop(context,sourceUri,outputFile,aspextX,aspextY,maxX,maxY);
    }
}

