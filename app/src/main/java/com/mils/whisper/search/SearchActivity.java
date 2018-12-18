package com.mils.whisper.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.config.LayoutConfig;
import com.mils.whisper.search.fragment.ArticleSearchFragment;
import com.mils.whisper.search.fragment.UserSearchFragment;
import com.mils.whisper.util.WindowAttr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/6/3.
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.view_statusBar_search)
    View view_statusBar_search;
    @BindView(R.id.vp_content_search)
    ViewPager vp_search;
    @BindView(R.id.tl_tab_search)
    TabLayout tab_search;
    @BindView(R.id.edt_dosearch)
    EditText edt_search;
    @BindView(R.id.btn_search_empty)
    Button btn_search_empty;
    OnArticleSearchListener onArticleSearchListener;
    OnUserSearchListener onUserSearchListener;
    int searchTime = 0;
    int searchErrorTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setSteepStatusBar(true);

        /*搜索*/
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    Loading("正在搜索...");
                    onUserSearchListener.doUserSearch(edt_search.getText().toString().trim(),
                            new OnUserSearchListener.UserSearchCallback() {
                                @Override
                                public void onSuccess() {
                                    searchTime++;
                                    if (searchTime==2){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadSuccess();
                                    }else if (searchTime==1&&searchErrorTime==1){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }
                                    Log.d("doSearch","searchTime:"+searchTime);
                                    Log.d("doSearch","searchErrorTime:"+searchErrorTime);
                                }

                                @Override
                                public void onFailure(int errorCode) {
                                    if (errorCode==9015){
                                        searchTime++;
                                    }else {
                                        searchErrorTime++;
                                    }
                                    if (searchErrorTime==2){
                                        searchTime = 0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }else if (searchTime==1&&searchErrorTime==1){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }else if (searchTime==2){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadSuccess();
                                    }
                                    Log.d("doSearch","searchTime:"+searchTime);
                                    Log.d("doSearch","searchErrorTime:"+searchErrorTime);
                                }
                            });
                    onArticleSearchListener.doArticleSearch(edt_search.getText().toString().trim(),
                            new OnArticleSearchListener.ArticleSearchCallback() {
                                @Override
                                public void onSuccess() {
                                    searchTime++;
                                    if (searchTime==2){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadSuccess();
                                    }else if (searchTime==1&&searchErrorTime==1){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }
                                    Log.d("doSearch","searchTime:"+searchTime);
                                    Log.d("doSearch","searchErrorTime:"+searchErrorTime);
                                }

                                @Override
                                public void onFailure(int errorCode) {
                                    if (errorCode==9015){
                                        searchTime++;
                                    }else {
                                        searchErrorTime++;
                                    }
                                    if (searchErrorTime==2){
                                        searchTime = 0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }else if (searchTime==1&&searchErrorTime==1){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadFailure("搜索失败，请检查网络");
                                    }else if (searchTime==2){
                                        searchTime=0;
                                        searchErrorTime=0;
                                        LoadSuccess();
                                    }
                                    Log.d("doSearch","searchTime:"+searchTime);
                                    Log.d("doSearch","searchErrorTime:"+searchErrorTime);
                                }
                            });
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //ViewPage滑动界面
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    public void setOnArticleSearchListener(OnArticleSearchListener onArticleSearchListener) {
        this.onArticleSearchListener = onArticleSearchListener;
    }

    public void setOnUserSearchListener(OnUserSearchListener onUserSearchListener) {
        this.onUserSearchListener = onUserSearchListener;
    }

    @Override
    public void initView() {
        Log.d(TAG, "PhoneHeight:" + WindowAttr.getStateBarHeight(this) + "");
        initViewpager();
        initStatusBar(view_statusBar_search, LayoutConfig.RELATIVE_LAYOUT);
    }

    @Override
    public void initData() {}

    @OnClick(R.id.btn_search_empty)
    public void searchEmpty(){
        edt_search.setText("");
    }

    private void initViewpager() {
        ArticleSearchFragment fg_article_search = new ArticleSearchFragment();
        UserSearchFragment fg_user_search = new UserSearchFragment();

        tabIndicators = new ArrayList<>();
        tabIndicators.add("文字");
        tabIndicators.add("用户");
        tabFragments = new ArrayList<>();
        tabFragments.add(fg_article_search);
        tabFragments.add(fg_user_search);

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vp_search.setAdapter(contentAdapter);

        tab_search.setTabMode(TabLayout.MODE_FIXED);
        tab_search.setupWithViewPager(vp_search);
        tab_search.getTabAt(0).select();
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    public interface OnArticleSearchListener {
        void doArticleSearch(String searchText, ArticleSearchCallback callback);
        interface ArticleSearchCallback{
            void onSuccess();
            void onFailure(int ErrorCode);
        }
    }

    public interface OnUserSearchListener {
        void doUserSearch(String searchText, UserSearchCallback callback);
        interface UserSearchCallback{
            void onSuccess();
            void onFailure(int ErrorCode);
        }
    }
}
