package com.mils.whisper.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mils.whisper.activitymanager.ActivityCollector;
import com.mils.whisper.app.MyApplicant;
import com.mils.whisper.dialogfragment.LoadingDialogFragment;
import com.mils.whisper.util.WindowAttr;

import butterknife.ButterKnife;

import static com.mils.whisper.config.LayoutConfig.FRAME_LAYOUT;
import static com.mils.whisper.config.LayoutConfig.LINEAR_LAYOUT;
import static com.mils.whisper.config.LayoutConfig.RELATIVE_LAYOUT;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/5/31.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public static String TAG;
    public final PopupWindow popupWindow = new PopupWindow();
    /** 是否允许全屏 **/
    private boolean mAllowFullScreen = false;
    /** 是否沉浸状态栏 **/
    private boolean isSetStatusBar = false;
    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = true;

    private String tag="BaseActivity";

    //等待加载的DialogFragment
    private LoadingDialogFragment loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("BaseActivity","onCreate");

        ActivityCollector.addActivity(this);
        TAG=getClass().getSimpleName();

        loadingDialog = new LoadingDialogFragment();

        ActivityCollector.getActivityList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAllowFullScreen) {
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (isSetStatusBar) {
            steepStatusBar();
        }

        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.d(tag,"onDestroy");
    }

    /*初始化数据*/
    public abstract void initData();

    /*初始化控件*/
    public abstract void initView();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    public void getActivity(Class<?> activity){
        ActivityCollector.getActivity(activity);
    }

    /*用View充当statusBar*/
    public void initStatusBar(View view,int layout_config){
        if(layout_config==RELATIVE_LAYOUT){
            view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(this)));
        }else if(layout_config==LINEAR_LAYOUT){
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(this)));
        }else if(layout_config==FRAME_LAYOUT){
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(this)));
        }

    }

    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     *携带数据的页面跳转
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     *是否设置全屏
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * 是否设置沉浸状态栏
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * 是否设置屏幕旋转
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }

    /* View点击 *//*
    public abstract void widgetClick(View v);

    public void onClick(View v) {
        if (TimeUtil.fastClick())
            widgetClick(v);
    }*/

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /*用View充当statusBar*/
    public void initStatusBar(View view){
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                WindowAttr.getStateBarHeight(this)));
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 显示软键盘
     */
    public void showInputMethod(){
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
        }
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /*数据加载界面*/
    public void Loading(String msg){
        loadingDialog.show(getFragmentManager(),"dialog_fragment");
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        loadingDialog.setArguments(bundle);
    }

    /*加载成功*/
    public void LoadSuccess(){
        Log.d(tag,"loadSuccess()");
        loadingDialog.dismiss();
    }

    /*加载失败*/
    public void LoadFailure(String msg){
        Log.d(tag,"loadFailure()");
        loadingDialog.dismiss();
        ToastShort(msg);
    }
}
