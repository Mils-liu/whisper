package com.mils.whisper.home;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.home.fragment.DynamicsFragment;
import com.mils.whisper.home.fragment.RecommendFragment;
import com.mils.whisper.home.fragment.UserFragment;
import com.mils.whisper.receiver.NetWorkReceiver;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.mils.whisper.activitymanager.ActivityCollector.ACTIVITY_REGISTER;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ACTION_CHOOSE;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.TAKE_PHOTO;

/**
 * Created by Administrator on 2018/5/31.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.tl_tab_home)
    public TabLayout tab_home;
    @BindView(R.id.vp_content_home)
    public ViewPager vp_home;

    String[] title = {"个人", "推荐", "动态"};
    long exitTime=0;

    private IntentFilter intentFilter;
    private NetWorkReceiver netWorkReceiver;

    //ViewPage滑动界面
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    private UserFragment userFragment;
    private RecommendFragment recommendFragment;
    private DynamicsFragment dynamicsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSteepStatusBar(true);
        initNetwork();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getInt("from") == ACTIVITY_REGISTER) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);
    }

    @Override
    public void initView() {
        initContent();
        initTab();
    }

    @Override
    public void initData() {}

    /*动态注册广播，监控手机网络变化*/
    private void initNetwork() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, intentFilter);
    }

    private void initTab() {
        tab_home.setTabMode(TabLayout.MODE_FIXED);
        tab_home.setSelectedTabIndicatorHeight(0);
        ViewCompat.setElevation(tab_home, getResources().getDimension(R.dimen.elevation_5));
        vp_home.setOffscreenPageLimit(2);/*设置当前页面预加载的相邻页面数量*/
        tab_home.setupWithViewPager(vp_home);
        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = tab_home.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        tab_home.getTabAt(1).select();
    }

    private View getTabView(int position) {
        TypedArray prodIcon_tab = getResources().obtainTypedArray
                (R.array.prodIcon_tab);//图片资源ID数组
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView tab_text = (TextView) view.findViewById(R.id.tab_text);
        tab_text.setText(title[position]);
        ImageView tab_icon = (ImageView) view.findViewById(R.id.tab_icon);
        tab_icon.setImageResource(prodIcon_tab.getResourceId(position, 0));
        return view;
    }

    private void initContent() {
        tabIndicators = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            tabIndicators.add(title[i]);
        }
        userFragment = new UserFragment();
        recommendFragment = new RecommendFragment();
        dynamicsFragment = new DynamicsFragment();

        tabFragments = new ArrayList<>();
        tabFragments.add(userFragment);
        tabFragments.add(recommendFragment);
        tabFragments.add(dynamicsFragment);

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vp_home.setAdapter(contentAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult:"+"HomeResult");
        Log.d(TAG,"requestCode:"+requestCode);
        if(requestCode == TAKE_PHOTO || requestCode == ACTION_CHOOSE || requestCode==UCrop.REQUEST_CROP){

            if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                Log.d(TAG, "error:" + cropError);
                return;
            }
            Log.d(TAG,"requestCode:"+requestCode);

            userFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    //双击退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit(){//退出程序
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
