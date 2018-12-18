package com.mils.whisper.article.article_edit.interactor;

import android.util.Log;

import com.mils.whisper.article.article_edit.presenter.ArticleEditInterface;
import com.mils.whisper.bean.Article;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/7/8.
 */

public class ArticleEditInteractor implements ArticleEditInterface.Interactor{

    private String TAG = "ArticleEditInteractor";

    @Override
    public void doArticleUpdate(Article article, final UpdateCallback callback) {
        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    callback.onSuccess();
                }else {
                    callback.onFailure();
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

}
