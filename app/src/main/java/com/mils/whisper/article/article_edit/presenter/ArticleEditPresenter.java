package com.mils.whisper.article.article_edit.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mils.whisper.accountsetting.interactor.SettingInteractor;
import com.mils.whisper.accountsetting.presenter.SettingInterface;
import com.mils.whisper.article.article_edit.interactor.ArticleEditInteractor;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.home.user.interactor.UserInteractor;
import com.mils.whisper.home.user.presenter.UserInterface;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.article.article_edit.EditArticleActivity.coverExistence;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/7/8.
 */

public class ArticleEditPresenter implements ArticleEditInterface.Presenter {

    private ArticleEditInterface.View mView;
    private ArticleEditInterface.Interactor mInteractor;
    private SettingInterface.Interactor settingInteractor;
    private UserInterface.Interactor userInteractor;
    private String TAG = "ArticleEditPresenter";

    public ArticleEditPresenter(ArticleEditInterface.View view) {
        mView = view;
        mInteractor = new ArticleEditInteractor();
        settingInteractor = new SettingInteractor();
        userInteractor = new UserInteractor();
    }

    @Override
    public void openAlbum(Context context) {
        settingInteractor.doOpenAlbum(context);
    }

    @Override
    public void openCamera(Context context, File imageFile) {
        imageUri = Uri.fromFile(imageFile);
        settingInteractor.doOpenCamera(context, imageUri);
        Log.d(TAG, "context:" + context);
    }

    @Override
    public void cropImage(Context context, Uri sourceUri, File outputFile, int aspextX, int aspextY, int maxX, int maxY) {
        settingInteractor.doCrop(context, sourceUri, outputFile, aspextX, aspextY, maxX, maxY);
    }

    @Override
    public void onUpload(final File articleCover, int type) {

        userInteractor.doUpload(articleCover, new UserInterface.Interactor.UploadCallback() {
            @Override
            public void onSuccess(String url) {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void onArticleUpdate(File articleCover, final String title, final String content, final User author, final ArticleUpdateCallback callback) {

        Log.d(TAG, "onArticleUpdate");
        if (isEmpty(title)) {
            ToastShort("标题不能为空");
        } else if (isEmpty(content)) {
            ToastShort("内容不能为空");
        } else if (!coverExistence) {
            ToastShort("封面不能为空");
        } else {
            mView.loading("正在上传...");
            Log.d(TAG,"username1:"+author.getUsername());
            final Article article = new Article();
            article.setContent(content);
            article.setTitle(title);
            article.setAuthor(author);
            article.setCollectNum(0);
            article.setLikeNum(0);
            article.setCollects(null);
            article.setLikes(null);
            article.setAuthorObjectId(author.getObjectId());
            Log.d(TAG,"file:"+articleCover.getAbsolutePath());
            userInteractor.doUploadFile(articleCover, new UserInterface.Interactor.UploadFileCallback() {
                @Override
                public void onSuccess(BmobFile file) {
                    article.setArticleCover(file);
                    mInteractor.doArticleUpdate(article, new ArticleEditInterface.Interactor.UpdateCallback() {
                        @Override
                        public void onSuccess() {
                            ToastShort("发布成功");
                            mView.loadSuccess();
                            callback.onSuccess();
                            Log.d(TAG,"username:"+article.getAuthor().getUsername());
                        }

                        @Override
                        public void onFailure() {
                            mView.loadFailure("发布失败");
                            callback.onFailure();
                        }
                    });
                }

                @Override
                public void onFailure() {
                    mView.loadFailure("发布失败");
                    callback.onFailure();
                }
            });
        }
    }
}
