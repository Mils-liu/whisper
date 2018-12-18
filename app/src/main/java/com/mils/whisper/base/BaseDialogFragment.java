package com.mils.whisper.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/6/6.
 */

public abstract class BaseDialogFragment extends DialogFragment{

    private String TAG = "BaseDialogFragment";

    private double widthPercent = 4/5;

    private int dialog_gravity = Gravity.CENTER;

    private float dialog_width = WIDTH_NORMAL;

    public static final int WIDTH_NORMAL = 4/5;

    private Boolean ONTOUCHCANCEL=true;

    private Unbinder unbinder;
    /**
     * 贴附的activity
     */
    private Activity mActivity;
    /**
     * 根view
     */
    protected View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach(context)");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach(activity)");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG,"onCreateDialog()");

        mActivity=getActivity();

        Dialog dialog = new Dialog(mActivity, setLayoutStyleId());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(setLayoutResouceId());
        dialog.setCanceledOnTouchOutside(setOntouchCancel());

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        // 宽度全屏
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = dialog_gravity;
        lp.width = (int)(display.getWidth() * widthPercent); // 设置dialog宽度为屏幕的4/5
        window.setAttributes(lp);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(setLayoutResouceId(),null);

        unbinder = ButterKnife.bind(this,view);

        initView();

        initData(getArguments());

        onLoad();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 初始化数据
     * @date 2016-5-26 下午3:57:48
     * @param bundle 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle bundle)
    {

    }

    /**
     * 初始化View
     * @date 2016-5-26 下午3:58:49
     */
    protected void initView()
    {

    }

    /**
     * 设置监听事件
     * @date 2016-5-26 下午3:59:36
     */
    protected void setListener()
    {

    }

    public void setWidthPercent(float widthPercent){
        this.widthPercent = widthPercent;
    }

    public void setDialog_gravity(int dialog_gravity){
        this.dialog_gravity = dialog_gravity;
    }

    protected abstract Boolean setOntouchCancel();

    protected abstract int setLayoutResouceId();

    protected abstract int setLayoutStyleId();

    protected void onLoad(){};

    protected void initDialogWidth(int width){
        dialog_width = width;
    }
}
