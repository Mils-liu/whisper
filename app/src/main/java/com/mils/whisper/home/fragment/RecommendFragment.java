package com.mils.whisper.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.article.article.VisitArticleActivity;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.config.LayoutConfig;
import com.mils.whisper.home.RecommendArticleAdapter;
import com.mils.whisper.imageloader.GlideImageLoader;
import com.mils.whisper.search.SearchActivity;
import com.mils.whisper.util.TimeUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.mils.whisper.bean.Article.PROGRESS_VIEW;

/**
 * Created by Administrator on 2018/6/1.
 */

public class RecommendFragment extends BaseFragment implements OnBannerListener {

    @BindView(R.id.rv_article_recommend)
    public RecyclerView rv_recommend;
    @BindView(R.id.view_statusBar_re)
    View view_statusBar;
    @BindView(R.id.banner)
    public Banner banner;
    @BindView(R.id.edt_search)
    public TextView edt_search;
    @BindView(R.id.sr)
    public SwipeRefreshLayout sr;

    private String TAG = "RecommendFragment";

    private List<String> titleList;
    private List<String> coverList;
    private List<Article> banArticleList = new ArrayList<Article>();
    private List<Article> recArticleList = new ArrayList<Article>();
    private BmobUser user;
    private boolean flag = false;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
    }

    @OnClick(R.id.edt_search)
    public void toSearch(){
        startActivity(SearchActivity.class);
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        user = BmobUser.getCurrentUser(User.class);
        initBannerArticleList();
    }

    @Override
    protected void initView() {
        super.initView();
        initStatusBar(view_statusBar, LayoutConfig.RELATIVE_LAYOUT);
        initRecommendArticleList();
        sr.setColorSchemeResources(R.color.darkgreen);
    }

    @Override
    public void OnBannerClick(int position) {
        if (TimeUtil.fastClick()){
            Bundle bundle = new Bundle();
            bundle.putString("ObjectId",banArticleList.get(position).getObjectId());
            startActivity(VisitArticleActivity.class,bundle);
        }
    }

    /*初始化轮播图*/
    private void initBanner(){
        coverList=new ArrayList<String>();
        titleList=new ArrayList<String>();
        for (Article article : banArticleList) {
            Log.d(TAG,"bannerCover:"+article.getArticleCover().getFileUrl());
            Log.d(TAG,"bannerTitle:"+article.getTitle());
            coverList.add(article.getArticleCover().getFileUrl());
            titleList.add(article.getTitle());
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(coverList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titleList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        /*设置点击事件*/
        banner.setOnBannerListener(this);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /*初始化轮播图上的文字*/
    private void initBannerArticleList(){
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e==null){
                    for(int i=0;i<3;i++){
                        banArticleList.add(list.get(i));
                        Log.d(TAG,"articleTitle:"+list.get(i).getTitle());
                    }
                    initBanner();
                }else {
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    /*初始化推荐文字*/
    private void initRecommendArticleList(){
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e==null){
                    for (Article article : list) {
                        recArticleList.add(article);
                        Log.d(TAG,"recArticleTitle:"+article.getTitle());
                    }
                    initAdapter(recArticleList);
                }else {
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    private void initAdapter(List<Article> articleList){
        Log.d(TAG,"initAdapter");
        if (articleList.size()>0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rv_recommend.setLayoutManager(layoutManager);
            final RecommendArticleAdapter adapter = new RecommendArticleAdapter(articleList);
            rv_recommend.setAdapter(adapter);

            adapter.setOnRecyclerViewListener(new RecommendArticleAdapter.OnRecyclerViewListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ObjectId",recArticleList.get(position).getObjectId());
                    startActivity(VisitArticleActivity.class,bundle);
                }
            });

            /*上拉加载更多*/
            rv_recommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isSlideToBottom(recyclerView)&&(flag==false)){
                        Article article = new Article(PROGRESS_VIEW);
                        recArticleList.add(article);
                        adapter.notifyDataSetChanged();
                        flag=true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recArticleList.remove(recArticleList.size()-1);
                                adapter.notifyDataSetChanged();
                                flag=false;
                            }
                        }, 2000);
                    }
                }
            });
        }
    }

    /*判断recyclerview是否到达底部*/
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
