package com.mils.whisper.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mils.whisper.dialogfragment.LoadingDialogFragment;
import com.mils.whisper.util.WindowAttr;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mils.whisper.config.LayoutConfig.FRAME_LAYOUT;
import static com.mils.whisper.config.LayoutConfig.LINEAR_LAYOUT;
import static com.mils.whisper.config.LayoutConfig.RELATIVE_LAYOUT;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/1.
 */

public abstract class BaseFragment extends Fragment{

    private Unbinder unbinder;

    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View view;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    //等待加载的DialogFragment
    private LoadingDialogFragment loadingDialog;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(setLayoutResouceId(), container, false);

        unbinder = ButterKnife.bind(this,view);

        loadingDialog = new LoadingDialogFragment();

        initData(getArguments());

        initView();

        mIsPrepare = true;

        onLazyLoad();

        setListener();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 初始化数据
     * @author 漆可
     * @date 2016-5-26 下午3:57:48
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments)
    {

    }

    /**
     * 初始化View
     * @author 漆可
     * @date 2016-5-26 下午3:58:49
     */
    protected void initView()
    {

    }

    /**
     * 设置监听事件
     * @author 漆可
     * @date 2016-5-26 下午3:59:36
     */
    protected void setListener()
    {

    }

    /*用View充当statusBar*/
    public void initStatusBar(View view,int layout_config){
        if(layout_config==RELATIVE_LAYOUT){
            view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(mActivity)));
        }else if(layout_config==LINEAR_LAYOUT){
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(mActivity)));
        }else if(layout_config==FRAME_LAYOUT){
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowAttr.getStateBarHeight(mActivity)));
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
        intent.setClass(mActivity, clz);
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
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser)
        {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     * @author 漆可
     * @date 2016-5-26 下午4:09:39
     */
    protected void onVisibleToUser()
    {
        if (mIsPrepare && mIsVisible)
        {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见view初始化结束后才会执行
     * @author 漆可
     * @date 2016-5-26 下午4:10:20
     */
    protected void onLazyLoad() {}

    /**
     * 设置根布局资源id
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     * @return
     */
    protected abstract int setLayoutResouceId();

    /*数据加载界面*/
    public void Loading(String msg){
        loadingDialog.show(mActivity.getFragmentManager(),"dialog_fragment");
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        loadingDialog.setArguments(bundle);
    }

    /*加载成功*/
    public void LoadSuccess(){
        loadingDialog.dismiss();
    }

    /*加载失败*/
    public void LoadFailure(String loadfailMsg){
        loadingDialog.dismiss();
        ToastShort(loadfailMsg);
    }

    /**
     * 隐藏软件盘
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        if (mActivity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    *//**
     * 点击软键盘之外的空白处，隐藏软件盘
     * @param ev
     * @return
     *//*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = mActivity.getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return true;
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (mActivity.getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return mActivity.onTouchEvent(ev);
    }

    *//**
     * 显示软键盘
     *//*
    public void showInputMethod(){
        if (mActivity.getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(mActivity.getCurrentFocus().getWindowToken(),0);
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
    }*/
}
