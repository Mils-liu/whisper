package com.mils.whisper.article.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.article.AppBarStateChangeListener;
import com.mils.whisper.article.article_edit.ElseChooseDialogFragment;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/11/23.
 */

public class UserArticleActivity extends BaseActivity implements ElseChooseDialogFragment.OnArticleDeleteListener{

    @BindView(R.id.img_cover_article)
    public ImageView cover;
    @BindView(R.id.txt_title_article)
    public TextView title;
    @BindView(R.id.txt_creation_article)
    public TextView createAt;
    @BindView(R.id.txt_content_article)
    public TextView content;
    @BindView(R.id.btn_else_choose)
    public Button btn_else;
    @BindView(R.id.btn_back_aau)
    public Button btn_back;
    @BindView(R.id.collapsing_toolbar_user)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBar_user)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_articleTitle_ucp)
    TextView txt_toolbarTitile;
    @BindView(R.id.ntScroll_user)
    NestedScrollView ntScroll_user;
    private ElseChooseDialogFragment elseChooseDialogFragment;
    private String objectId;
    private String TAG = "UserArticleActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_user);
        setSteepStatusBar(true);/*设置沉浸式状态栏*/
        elseChooseDialogFragment = new ElseChooseDialogFragment();
    }

    @Override
    public void initData() {
        Bundle bundle = this.getIntent().getExtras();
        objectId = bundle.getString("ObjectId");
        Log.d(TAG,"objectId:"+objectId);

        final BmobQuery<Article> artcleQuery = new BmobQuery<Article>();
        artcleQuery.getObject(objectId, new QueryListener<Article>() {
            @Override
            public void done(Article article,BmobException e) {
                if(e==null){
                    title.setText(article.getTitle());
                    txt_toolbarTitile.setText(article.getTitle());
                    createAt.setText(article.getUpdatedAt());
                    content.setText(article.getContent());
                    Glide.with(getApplicationContext()).load(article.getArticleCover().getFileUrl()).into(cover);
                    Log.d(TAG,"title:"+article.getTitle());
                    User user = article.getAuthor();
                    Log.d(TAG,"userid:"+user.getObjectId());
                    Log.d(TAG,"userSex:"+user.getSex());
                    Log.d(TAG,"userName:"+user.getUsername());
                    Log.d(TAG,"userBrief:"+user.getBrief());
                }else{
                    ToastShort("加载失败！");
                }
            }
        });
    }

    @Override
    public void initView() {
        /*if (isBottom(ntScroll_user)){
            ntScroll_user.setNestedScrollingEnabled(false);
        }else {
            ntScroll_user.setNestedScrollingEnabled(true);
        }*/
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    btn_back.setBackgroundResource(R.drawable.back);
                    btn_else.setBackgroundResource(R.drawable.else_choose);
                    txt_toolbarTitile.setTextColor(getResources().getColor(R.color.expendTitleColor));
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    btn_else.setBackgroundResource(R.drawable.else_choose__collapse);
                    btn_back.setBackgroundResource(R.drawable.back_collapse);
                    txt_toolbarTitile.setTextColor(getResources().getColor(R.color.collapsedTitleTextColor));
                }else {
                    //中间状态
                }
            }
        });
    }

    @OnClick(R.id.btn_else_choose)
    public void elseChoose(){
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        elseChooseDialogFragment.setArguments(bundle);
        elseChooseDialogFragment.show(getFragmentManager(),"else_choose");
        elseChooseDialogFragment.setOnArticleDeleteListener(this);
    }

    @OnClick(R.id.btn_back_aau)
    public void back(){
        finish();
    }

    @Override
    public void onArticleDelete() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (isBottom(ntScroll_user)){
            ntScroll_user.setNestedScrollingEnabled(false);
        }else {
            ntScroll_user.setNestedScrollingEnabled(true);
        }*/
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){

        }
    }*/


    public static boolean isBottom(NestedScrollView ntScroll){
        int scrollY = ntScroll.getScrollY();
        Log.d("UserArticleActivityS","scrollY:"+scrollY);
        View onlyChild = ntScroll.getChildAt(0);
        Log.d("UserArticleActivityS","onlyChild:"+onlyChild.getHeight());
        Log.d("UserArticleActivityS","ntScroll:"+ntScroll.getHeight());
        if (onlyChild.getHeight() <= scrollY + ntScroll.getHeight()) {   // 如果满足就是到底部了
            return true;
        }
        return false;
    }
}
