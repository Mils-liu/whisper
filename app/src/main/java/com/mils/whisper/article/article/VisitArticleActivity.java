package com.mils.whisper.article.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.article.AppBarStateChangeListener;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.user.UserVisitActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.mils.whisper.util.ResourceUtil.getResString;
import static com.mils.whisper.util.ToastUtil.ToastShort;


/**
 * Created by Administrator on 2018/11/30.
 */

public class VisitArticleActivity extends BaseActivity {

    @BindView(R.id.img_articleCover_visit)
    ImageView cover;
    @BindView(R.id.txt_title_visit)
    TextView title;
    @BindView(R.id.txt_content_visit)
    TextView content;
    @BindView(R.id.txt_creation_visit)
    TextView createAt;
    @BindView(R.id.fb_collect)
    FloatingActionButton fb_collect;
    @BindView(R.id.btn_like)
    Button btn_like;
    @BindView(R.id.ci_author_head)
    CircleImageView ci_head;
    @BindView(R.id.txt_username_visit)
    TextView txt_username;
    @BindView(R.id.collapsing_toolbar_visit)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBar_visit)
    AppBarLayout appBarLayout;
    @BindView(R.id.btn_back_aav)
    Button btn_back;
    @BindView(R.id.txt_articleTitle_vcp)
    TextView txt_toolbarTitile;
    @BindView(R.id.ntScroll_visit)
    NestedScrollView ntScroll_visit;

    private String objectId;
    private User author;
    private User currentUser;
    private Article visitArticle;
    private List<String> collectList;
    private List<String> likeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_visit);
        setSteepStatusBar(true);/*设置沉浸式状态栏*/
    }

    @Override
    public void initData() {
        Bundle bundle = this.getIntent().getExtras();
        objectId = bundle.getString("ObjectId");
        Log.d(TAG,"objectId:"+objectId);
        currentUser = BmobUser.getCurrentUser(User.class);

        final BmobQuery<Article> artcleQuery = new BmobQuery<Article>();
        artcleQuery.include("author");
        artcleQuery.getObject(objectId, new QueryListener<Article>() {
            @Override
            public void done(Article article,BmobException e) {
                if(e==null){
                    visitArticle=article;
                    title.setText(visitArticle.getTitle());
                    txt_toolbarTitile.setText(visitArticle.getTitle());
                    createAt.setText(visitArticle.getUpdatedAt());
                    content.setText(visitArticle.getContent());
                    Glide.with(VisitArticleActivity.this).load(visitArticle.getArticleCover().getFileUrl()).into(cover);
                    fb_collect.setVisibility(View.VISIBLE);
                    btn_like.setVisibility(View.VISIBLE);
                    author = visitArticle.getAuthor();
                    Log.d(TAG,"username:"+author.getUsername());
                    txt_username.setText(author.getUsername());
                    Glide.with(getApplicationContext()).load(author.getHead()).into(ci_head);
                    initArticleState();
                }else{
                    ToastShort("加载失败！");
                }
            }
        });
    }

    @Override
    public void initView() {
        /*if (isBottom(ntScroll_visit)){
            ntScroll_visit.setNestedScrollingEnabled(false);
        }else {
            ntScroll_visit.setNestedScrollingEnabled(true);
        }*/
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    btn_back.setBackgroundResource(R.drawable.back);
                    txt_toolbarTitile.setTextColor(getResources().getColor(R.color.expendTitleColor));
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    btn_back.setBackgroundResource(R.drawable.back_collapse);
                    txt_toolbarTitile.setTextColor(getResources().getColor(R.color.collapsedTitleTextColor));
                }else {
                    //中间状态
                }
            }
        });
    }

    @OnClick(R.id.btn_back_aav)
    public void back(){
        finish();
    }

    @OnClick(R.id.ci_author_head)
    public void checkUser(){
        if (!author.getObjectId().equals(currentUser.getObjectId())){
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",author);
            startActivity(UserVisitActivity.class,bundle);
        }
    }

    @OnClick(R.id.fb_collect)
    public void collectArticle(){
        Log.d(TAG,"tag:"+fb_collect.getTag());
        if (fb_collect.getTag().toString().equals(getResources().getString(R.string.uncollect))){
            Log.d(TAG,"doCollect");
            doCollect();
        }else if (fb_collect.getTag().toString().equals(getResources().getString(R.string.collect))){
            Log.d(TAG,"cancelCollect");
            cancelCollect();
        }
        Log.d(TAG,"doNothing");
    }

    @OnClick(R.id.btn_like)
    public void likeArticle(){
        if (btn_like.getTag().equals(getResources().getString(R.string.unlike))){
            doLike();
        }else {
            cancelLike();
        }
    }

    /*收藏文字*/
    private void doCollect(){
        Article article = new Article();
        article.setObjectId(visitArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(currentUser);
        article.setCollects(relation);
        article.increment("collectNum",1);
        article.update(visitArticle.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userAddCollect();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error1:"+e);
                }
            }
        });
    }
    /*取消收藏文字*/
    private void cancelCollect(){
        Article article = new Article();
        article.setObjectId(visitArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(currentUser);
        article.setCollects(relation);
        article.increment("collectNum",-1);
        article.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userRemoveCollect();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error2:"+e);
                }
            }
        });
    }

    private void userAddCollect(){
        Log.d(TAG,"userAddCollect");
        collectList = currentUser.getCollectList();
        if(null==collectList){
            collectList = new ArrayList<String>();
        }
        User user = new User();
        collectList.add(visitArticle.getObjectId());
        Log.d(TAG,"collectList:"+collectList.get(0));
        user.setCollectList(collectList);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    collectState();
                    LoadSuccess();
                    ToastShort(getResString(R.string.success));
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error3:"+e);
                }
            }
        });
    }

    private void userRemoveCollect(){
        List<String> collectList = currentUser.getCollectList();
        if (null!=collectList){
            Log.d(TAG,"userRemoveCollect");
            for (String objectId : collectList) {
                if (objectId.equals(visitArticle.getObjectId())){
                    collectList.remove(objectId);
                    break;
                }
            }
        }
        User user = new User();
        user.setCollectList(collectList);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    uncollectState();
                    ToastShort(getResString(R.string.success));
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error4:"+e);
                }
            }
        });
    }

    /*点赞文字*/
    private void doLike(){
        Article article = new Article();
        article.setObjectId(visitArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(currentUser);
        article.setLikes(relation);
        article.increment("likeNum",1);
        article.update(visitArticle.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userAddLike();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error5:"+e);
                }
            }
        });
    }
    /*取消点赞文字*/
    private void cancelLike(){
        Article artile = new Article();
        artile.setObjectId(visitArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(currentUser);
        artile.setLikes(relation);
        artile.increment("likeNum",-1);
        artile.update(visitArticle.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userRemoveLike();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error6:"+e);
                }
            }
        });
    }

    private void userAddLike(){
        likeList = currentUser.getLikeList();
        if (null==likeList){
            likeList = new ArrayList<String>();
        }
        User user = new User();
        likeList.add(visitArticle.getObjectId());
        user.setLikeList(likeList);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    likeState();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error7:"+e);
                }
            }
        });
    }

    private void userRemoveLike(){
        likeList = currentUser.getLikeList();
        if (null!=likeList){
            for (String objectId : likeList) {
                if (objectId.equals(visitArticle.getObjectId())){
                    likeList.remove(objectId);
                    break;
                }
            }
        }
        User user = new User();
        user.setLikeList(likeList);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    unlikeState();
                    LoadSuccess();
                }else {
                    ToastShort(getResString(R.string.error));
                    Log.d(TAG,"error8:"+e);
                }
            }
        });
    }

    /*加载点赞和收藏图标*/
    private void initArticleState(){
        /*点赞图标*/
        likeList = currentUser.getLikeList();
        if (null!=likeList){
            for (String objectId : likeList) {
                if (objectId.equals(visitArticle.getObjectId())){
                    likeState();
                }
            }
        }
        /*收藏图标*/
        collectList = currentUser.getCollectList();
        if (null!=collectList){
            for (String objectId : collectList) {
                if (objectId.equals(visitArticle.getObjectId())){
                    collectState();
                }
            }
        }
    }

    private void likeState(){
        btn_like.setBackgroundResource(R.drawable.like);
        btn_like.setTag(getResources().getString(R.string.like));
    }

    private void unlikeState(){
        btn_like.setBackgroundResource(R.drawable.unlike);
        btn_like.setTag(getResources().getString(R.string.unlike));
    }

    private void collectState(){
        fb_collect.setImageResource(R.drawable.collection_cancel);
        fb_collect.setTag(getResources().getString(R.string.collect));
    }

    private void uncollectState(){
        fb_collect.setImageResource(R.drawable.collection);
        fb_collect.setTag(getResources().getString(R.string.uncollect));
    }
}
