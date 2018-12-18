package com.mils.whisper.article.articlelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.article.article.UserArticleActivity;
import com.mils.whisper.article.article.VisitArticleActivity;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.config.LayoutConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/6/3.
 */

public class ArticleListActivity extends BaseActivity {

    @BindView(R.id.view_statusBar_au)
    public View statusBar;
    @BindView(R.id.rv_myarticle)
    public RecyclerView rv_myarticle;
    @BindView(R.id.txt_myarticle_num)
    public TextView txt_articlenum;
    @BindView(R.id.btn_back_myarticle)
    public Button btn_back;
    @BindView(R.id.txt_myarticle)
    TextView txt_artile;

    private List<Article> articleList = new ArrayList<>();
    private User user;
    private String from;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articlelist_user);
        setSteepStatusBar(true);
        Log.d(TAG, "nickname:"+user.getUsername());
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        from = bundle.getString("from");
        type = bundle.getString("type");
    }

    @Override
    public void initView() {
        initStatusBar(statusBar, LayoutConfig.RELATIVE_LAYOUT);
        if (type.equals("article")){
            if (from.equals("user")){
                txt_artile.setText("我的文字");
            }else if(from.equals("search")){
                txt_artile.setText("他的文字");
            }
            initArticle();
        }else if (type.equals("collection")){
            if (from.equals("user")){
                txt_artile.setText("我的最爱");
            }else if (from.equals("search")){
                txt_artile.setText("他的最爱");
            }
            initCollection();
        }
    }

    @OnClick(R.id.btn_back_myarticle)
    public void back(){
        finish();
    }

    private void initArticle(){
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有文字
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Article>() {

            @Override
            public void done(List<Article> articleList,BmobException e) {
                if(e==null){
                    Log.d(TAG,"title:"+articleList.get(0).getTitle());
                    initAdapter(articleList);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
    }

    private void initCollection(){
        List<String> collectList = user.getCollectList();
        if (null!=collectList){
            BmobQuery<Article> query = new BmobQuery<>();
            query.addWhereContainedIn("objectId",collectList);
            query.findObjects(new FindListener<Article>() {
                @Override
                public void done(List<Article> articleList, BmobException e) {
                    if (e==null){
                        initAdapter(articleList);
                    }else {
                        Log.d(TAG,"error:"+e);
                    }
                }
            });
        }
    }

    private void initAdapter(final List<Article> articleList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_myarticle.setLayoutManager(layoutManager);
        ArticleAdapter articleAdapter = new ArticleAdapter(articleList);
        rv_myarticle.setAdapter(articleAdapter);
        Log.d(TAG,"articleSize:"+articleList.size());
        txt_articlenum.setText("("+articleList.size()+")");

        articleAdapter.setOnRecyclerViewListener(new ArticleAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.rl_article:
                        Bundle bundle = new Bundle();
                        bundle.putString("ObjectId",articleList.get(position).getObjectId());
                        if (from.equals("search")||type.equals("collection")){
                            bundle.putSerializable("user",user);
                            startActivity(VisitArticleActivity.class,bundle);
                        }else {
                            startActivity(UserArticleActivity.class,bundle);
                        }
                        break;
                }
            }
        });
    }
}
