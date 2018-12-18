package com.mils.whisper.dialogfragment;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnSexSelectListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/5.
 */

public class SexDialogFragment extends BaseDialogFragment {

    @BindView(R.id.img_blue)
    public ImageView img_blue;
    @BindView(R.id.img_male)
    public ImageView img_male;
    @BindView(R.id.img_red)
    public ImageView img_red;
    @BindView(R.id.img_female)
    public ImageView img_female;
    @BindView(R.id.img_purple)
    public ImageView img_purple;
    @BindView(R.id.img_uncertain)
    public ImageView img_uncertain;
    @BindView(R.id.txt_ensure_sex)
    public TextView txt_ensure;
    @BindView(R.id.fl_male)
    public FrameLayout fl_male;
    @BindView(R.id.fl_female)
    public FrameLayout fl_female;
    @BindView(R.id.fl_uncertain)
    public FrameLayout fl_uncertain;

    private OnSexSelectListener listener;
    private int sex_select;

    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int UNCERTAIN = 2;

    //创建 SingleObject 的一个对象
    private static SexDialogFragment instance = new SexDialogFragment();

    //让构造函数为 private，这样该类就不会被实例化
    public SexDialogFragment(){}

    //获取唯一可用的对象
    public static SexDialogFragment getInstance(){
        return instance;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialog_gravity(Gravity.CENTER);
        setWidthPercent(0.8f);
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.dialog_sex_choose;
    }

    @Override
    protected int setLayoutStyleId() {
        return R.style.dialog_custom;
    }

    public void setOnSexSelectListener(OnSexSelectListener listener){
        this.listener = listener;
    }

    @Override
    protected Boolean setOntouchCancel() {
        return true;
    }

    @Override
    protected void initData(Bundle bundle) {
        super.initData(bundle);
        if (bundle != null) {
            int sex = bundle.getInt("sex");
            sex_select = sex;
            SexSelect(sex);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @OnClick(R.id.fl_female)
    public void female_choose(){
        SexSelect(FEMALE);
        sex_select=FEMALE;
    }

    @OnClick(R.id.fl_male)
    public void male_choose(){
        SexSelect(MALE);
        sex_select=MALE;
    }

    @OnClick(R.id.fl_uncertain)
    public void uncertain_choose(){
        SexSelect(UNCERTAIN);
        sex_select=UNCERTAIN;
    }

    @OnClick(R.id.txt_ensure_sex)
    public void sex_ensure(){
        listener.OnSetSex(sex_select);
        dismiss();
    }

    private void SexSelect(int sex){
        img_uncertain.setSelected(false);
        img_purple.setSelected(false);
        img_female.setSelected(false);
        img_red.setSelected(false);
        img_male.setSelected(false);
        img_blue.setSelected(false);

        switch (sex){
            case MALE:
                img_male.setSelected(true);
                img_blue.setSelected(true);
                break;
            case FEMALE:
                img_female.setSelected(true);
                img_red.setSelected(true);
                break;
            case UNCERTAIN:
                img_uncertain.setSelected(true);
                img_purple.setSelected(true);
                break;
            default:
                break;
        }
    }
}
