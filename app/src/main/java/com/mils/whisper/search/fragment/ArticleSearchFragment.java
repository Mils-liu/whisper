package com.mils.whisper.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.article.article.VisitArticleActivity;
import com.mils.whisper.article.articlelist.ArticleAdapter;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.bean.Article;
import com.mils.whisper.search.ArticleSearchAdapter;
import com.mils.whisper.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2018/6/3.
 */

public class ArticleSearchFragment extends BaseFragment implements SearchActivity.OnArticleSearchListener{

    @BindView(R.id.rv_search_article)
    RecyclerView rv_articlesearch;
    @BindView(R.id.txt_end_article)
    TextView end;
    private String TAG = "ArticleSearchFragment";
    private List<Article> articleList = new ArrayList<Article>();
    private ArticleSearchAdapter articleSearchAdapter;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_search_article;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SearchActivity)context).setOnArticleSearchListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void doArticleSearch(final String searchText, final ArticleSearchCallback callback) {
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Article>() {

            @Override
            public void done(List<Article> objects, BmobException e) {
                if(e==null){
                    Log.d("doSearch","startArticleSearch");
                    if(objects.size()>0){
                        Log.d("doSearch","doArticleSearch");

                        if (articleList.size()>0){
                            articleList.clear();
                        }
                        for (Article article : objects) {
                            if (article.getTitle().indexOf(searchText)!=-1){
                                articleList.add(article);
                            }
                        }
                        if (articleList.size()>0){
                            end.setVisibility(View.INVISIBLE);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            rv_articlesearch.setLayoutManager(layoutManager);
                            articleSearchAdapter = new ArticleSearchAdapter(articleList);
                            rv_articlesearch.setAdapter(articleSearchAdapter);

                            callback.onSuccess();
                            Log.d("doSearch","searchArticleSuccess");

                            articleSearchAdapter.setOnRecyclerViewListener(new ArticleAdapter.OnRecyclerViewListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    switch (view.getId()){
                                        case R.id.rl_article_search:
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ObjectId",articleList.get(position).getObjectId());
                                            Log.d(TAG,"ObjectId:"+articleList.get(position).getObjectId());
                                            startActivity(VisitArticleActivity.class,bundle);
                                            break;
                                    }
                                }
                            });
                        }else {
                            articleSearchAdapter.notifyDataSetChanged();
                            callback.onSuccess();
                            end.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (articleList.size()>0){
                            articleList.clear();
                        }
                        articleSearchAdapter.notifyDataSetChanged();
                        end.setVisibility(View.VISIBLE);
                    }
                }else{
                    end.setVisibility(View.VISIBLE);
                    callback.onFailure(e.getErrorCode());
                    Log.d("doSearch","searchError1:"+e);
                }
            }

        });
    }
}
