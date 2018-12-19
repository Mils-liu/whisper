package com.mils.whisper.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mils.whisper.R;
import com.mils.whisper.article.article.VisitArticleActivity;
import com.mils.whisper.article.articlelist.ArticleAdapter;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.config.LayoutConfig;
import com.mils.whisper.home.DynamicsAdapter;
import com.mils.whisper.user.UserVisitActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.view.View.GONE;
import static com.mils.whisper.bean.Article.END_VIEW;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/1.
 */

public class DynamicsFragment extends BaseFragment {

    @BindView(R.id.rv_dynamics)
    RecyclerView rv_dynamics;
    @BindView(R.id.view_statusBar_dy)
    View view_statusBar;
    Button btn_like;
    @BindView(R.id.rl_loading_dy)
    RelativeLayout rl_loading;

    int skipTime=0;
    final int limitNum=5;
    private String TAG = "DynamicsFragment";
    private boolean flag = false;
    DynamicsAdapter dynamicsAdapter;
    private List<User> focusList = new ArrayList<>();
    private List<Article> articleList = new ArrayList<>();
    private User currentUser;
    private List<String> likeList;

    @Override
    public void onStart() {
        super.onStart();
        initFocus();
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_dynamics;
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    @Override
    protected void initView() {
        super.initView();
        initStatusBar(view_statusBar, LayoutConfig.RELATIVE_LAYOUT);
    }

    private void initFocus(){
        BmobQuery<User> query = new BmobQuery<User>();
        User user = new User();
        user.setObjectId(currentUser.getObjectId());
        query.addWhereRelatedTo("focusUser", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    focusList = list;
                    initDynamics(focusList,false);
                }else{
                    Log.d(TAG,"error:"+e);
                    rl_loading.setVisibility(GONE);
                }
            }
        });
    }

    private void initDynamics(List<User> focusList, final boolean loadmore){
        Log.d(TAG,"doInitDynamics");
        ArrayList<String> focusUserId = new ArrayList<>();
        for (User user : focusList) {
            focusUserId.add(user.getObjectId());
            Log.d(TAG,"username:"+user.getObjectId());
        }
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereContainedIn("authorObjectId",focusUserId);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.setLimit(limitNum);
        query.setSkip(skipTime*limitNum);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e==null){
                    Log.d(TAG,"title:"+list.get(0).getTitle());
                    articleList.addAll(list);
                    skipTime++;
                    if (loadmore){
                        if (null==list||list.size()<5){
                            Article article = new Article(END_VIEW);
                            articleList.add(article);
                            flag=false;
                        }
                        dynamicsAdapter.notifyDataSetChanged();
                    }else {
                        initAdapter(articleList);
                    }
                }else {
                    Log.d(TAG,"error:"+e);
                    rl_loading.setVisibility(GONE);
                }
            }
        });
    }

    private void initAdapter(final List<Article> list){
        Log.d(TAG,"doInitAdapter");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_dynamics.setLayoutManager(layoutManager);
        dynamicsAdapter = new DynamicsAdapter(list,currentUser);
        rv_dynamics.setAdapter(dynamicsAdapter);
        rl_loading.setVisibility(GONE);
        dynamicsAdapter.setOnRecyclerViewListener(new DynamicsAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.rl_article_dynamics:
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("ObjectId",list.get(position).getObjectId());
                        startActivity(VisitArticleActivity.class,bundle1);
                        break;
                    case R.id.rl_inform:
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("user",list.get(position).getAuthor());
                        bundle2.putString("from","dynamics");
                        startActivity(UserVisitActivity.class,bundle2);
                        break;
                    case R.id.btn_like_dy:
                        btn_like = (Button)view.findViewById(R.id.btn_like_dy);
                        Log.d(TAG,"Like");
                        if (btn_like.getTag().toString().equals(getResources().getString(R.string.unlike))){
                            Log.d(TAG,"doLike");
                            doLike(list.get(position));
                        }else {
                            Log.d(TAG,"cancelLike");
                            cancelLike(list.get(position));
                        }
                        Log.d(TAG,"doNothing");
                        break;
                }
            }
        });

        rv_dynamics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)&&(flag==false)){
                    initDynamics(focusList,true);
                }
            }
        });
    }

    private void doLike(final Article likeArticle){
        Loading("点赞文字...");
        Article article = new Article();
        article.setObjectId(likeArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(currentUser);
        article.setLikes(relation);
        article.increment("likeNum",1);
        article.update(likeArticle.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userAddLike(likeArticle);
                }else {
                    LoadFailure("点赞失败");
                    Log.d(TAG,"error1:"+e);
                }
            }
        });
    }

    private void cancelLike(final Article likeArticle){
        Loading("取消点赞");
        Article artile = new Article();
        artile.setObjectId(likeArticle.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(currentUser);
        artile.setLikes(relation);
        artile.increment("likeNum",-1);
        artile.update(likeArticle.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    userRemoveLike(likeArticle);
                }else {
                    LoadFailure("取消点赞失败");
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    private void userAddLike(Article likeArticle){
        likeList = currentUser.getLikeList();
        if (null==likeList){
            likeList = new ArrayList<String>();
        }
        User user = new User();
        likeList.add(likeArticle.getObjectId());
        user.setLikeList(likeList);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    likeState();
                    LoadSuccess();
                    ToastShort("点赞成功");
                }else {
                    LoadFailure("点赞失败");
                }
            }
        });
    }

    private void userRemoveLike(Article likeArticle){
        likeList = currentUser.getLikeList();
        if (null!=likeList){
            for (String objectId : likeList) {
                if (objectId.equals(likeArticle.getObjectId())){
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
                    ToastShort("取消点赞成功");
                }else {
                    LoadFailure("取消点赞失败");
                }
            }
        });
    }

    private void likeState(){
        btn_like.setBackgroundResource(R.drawable.like);
        btn_like.setTag(getResources().getString(R.string.like));
    }

    private void unlikeState(){
        btn_like.setBackgroundResource(R.drawable.unlike);
        btn_like.setTag(getResources().getString(R.string.unlike));
    }

    /*判断recyclerview是否到达底部*/
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
