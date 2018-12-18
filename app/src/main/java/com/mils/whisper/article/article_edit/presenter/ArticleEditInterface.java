package com.mils.whisper.article.article_edit.presenter;

import android.content.Context;
import android.net.Uri;

import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;

import java.io.File;

/**
 * Created by Administrator on 2018/7/8.
 */

public interface ArticleEditInterface {

    interface View{
        void loading(String loadMsg);
        void loadSuccess();
        void loadFailure(String loadfailMsg);
    }

    interface Presenter{
        void openAlbum(Context context);
        void openCamera(Context context, File imageFile);
        void cropImage(Context context, Uri sourceUri, File outputFile, int aspextX, int aspextY, int maxX, int maxY);
        void onUpload(File articleCover, int type);
        void onArticleUpdate(File articleCover, String title, String content, User author, ArticleUpdateCallback callback);
        interface ArticleUpdateCallback{
            void onSuccess();
            void onFailure();
        }
    }

    interface Interactor{
        void doArticleUpdate(Article article, UpdateCallback callback);
        interface UpdateCallback{
            void onSuccess();
            void onFailure();
        }
    }

}
