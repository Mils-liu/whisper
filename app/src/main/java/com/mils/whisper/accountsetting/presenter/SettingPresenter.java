package com.mils.whisper.accountsetting.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.mils.whisper.R;
import com.mils.whisper.accountsetting.interactor.SettingInteractor;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.home.user.interactor.UserInteractor;
import com.mils.whisper.home.user.presenter.UserInterface;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.config.UserInformConfig.USERNAME;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;

/**
 * Created by Administrator on 2018/6/19.
 */

public class SettingPresenter implements SettingInterface.Presenter{

    private SettingInterface.View mView;
    private SettingInterface.Interactor mInteractor;
    private UserInterface.Interactor userInteractor;

    private String TAG = "SettingPresenter";

    public SettingPresenter(SettingInterface.View view) {
        mView = view;
        mInteractor = new SettingInteractor();
        userInteractor = new UserInteractor();
    }

    @Override
    public void onUpload(File file, final int setting) {
        if (file != null){
            mView.loading(getContext().getResources().getString(R.string.loading_data));
            userInteractor.doUpload(file, new UserInterface.Interactor.UploadCallback() {
                @Override
                public void onSuccess(String url) {
                    Log.d(TAG, "UploadSuccess");
                    Log.d(TAG, "url:" + url);

                    userInteractor.doUserUpdate(url, setting, new UserInterface.Interactor.UpdataCallback() {
                        @Override
                        public void onStringSuccess(final String url) {
                            Log.d(TAG, "UpdataSuccess");

                            userInteractor.fetchUserInfo(new UserInterface.Interactor.FetchCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG,"fetchSuccess");
                                    mView.reloadUserInform();
                                    mView.loadSuccess();
                                }

                                @Override
                                public void onFailure() {}
                            });
                        }

                        @Override
                        public void onIntegerSuccess(Integer inform) {

                        }

                        @Override
                        public void onFailure() {
                            Log.d(TAG, "UpdataFail");
                            mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
                        }
                    });
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "UploadFail");
                    mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
                }
            });

        }
    }

    @Override
    /*更新用户String类型数据*/
    public void onUserUpdateString(String inform, final int setting) {
        mView.loading(getContext().getResources().getString(R.string.loading_data));
        userInteractor.doUserUpdate(inform, setting, new UserInterface.Interactor.UpdataCallback() {
            @Override
            public void onStringSuccess(final String inform) {
                userInteractor.fetchUserInfo(new UserInterface.Interactor.FetchCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"fetchSuccess");
                        if (setting==USERNAME){

                        }
                        mView.reloadUserInform();
                        mView.loadSuccess();
                    }

                    @Override
                    public void onFailure() {
                        mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
                    }
                });
            }

            @Override
            public void onIntegerSuccess(Integer inform) {}

            @Override
            public void onFailure() {
                mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
            }
        });
    }

    @Override
    /*更新用户Integer类型数据*/
    public void onUserUpdateInteger(Integer inform, final int setting) {
        mView.loading(getContext().getResources().getString(R.string.loading_data));
        userInteractor.doUserUpdateInteger(inform, setting, new UserInterface.Interactor.UpdataCallback() {
            @Override
            public void onStringSuccess(String inform) {}

            @Override
            public void onIntegerSuccess(final Integer inform) {
                Log.d(TAG,"sex:"+inform);
                userInteractor.fetchUserInfo(new UserInterface.Interactor.FetchCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"sex:"+inform);
                        mView.reloadUserInform();
                        mView.loadSuccess();
                    }

                    @Override
                    public void onFailure() {
                        mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
                    }
                });
            }

            @Override
            public void onFailure() {
                mView.loadFailure(getContext().getResources().getString(R.string.load_failure));
            }
        });
    }

    @Override
    /*调用相机*/
    public void openCamera(Context context, File imageFile) {
        imageUri = Uri.fromFile(imageFile);
        mInteractor.doOpenCamera(context,imageUri);
    }

    @Override
    /*打开相册*/
    public void openAlbum(Context context) {
        mInteractor.doOpenAlbum(context);
    }

    /*剪裁图片*/
    public void cropImage(Context context,Uri sourceUri, File outputFile,int aspextX, int aspextY,
                          int maxX, int maxY){
        mInteractor.doCrop(context,sourceUri,outputFile,aspextX,aspextY,maxX,maxY);
    }

    private void changeAuthorName(){
        BmobUser currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser!=null){
            BmobQuery<Article> query = new BmobQuery<>();
            query.addWhereEqualTo("author", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            query.findObjects(new FindListener<Article>() {

                @Override
                public void done(List<Article> articleList, BmobException e) {
                    if (e == null) {

                    } else {
                        Log.d(TAG,"error:"+e);
                    }
                }

            });
        }
    }
}
