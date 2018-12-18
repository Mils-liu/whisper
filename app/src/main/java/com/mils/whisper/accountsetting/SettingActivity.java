package com.mils.whisper.accountsetting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.accountsetting.presenter.SettingInterface;
import com.mils.whisper.accountsetting.presenter.SettingPresenter;
import com.mils.whisper.activitymanager.ActivityCollector;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;
import com.mils.whisper.config.LayoutConfig;
import com.mils.whisper.dialogfragment.BriefSettingDialogFragment;
import com.mils.whisper.dialogfragment.NameSettingDialogFragment;
import com.mils.whisper.dialogfragment.PictureDialogFragment;
import com.mils.whisper.dialogfragment.SexDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnBriefSettingListener;
import com.mils.whisper.dialogfragment.listener.OnNameSettingListener;
import com.mils.whisper.dialogfragment.listener.OnPicSelectListener;
import com.mils.whisper.dialogfragment.listener.OnSexSelectListener;
import com.mils.whisper.login.LoginActivity;
import com.mils.whisper.util.FileUtil;
import com.mils.whisper.util.TimeUtil;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.config.UserInformConfig.BRIEF;
import static com.mils.whisper.config.UserInformConfig.SEX;
import static com.mils.whisper.config.UserInformConfig.USERNAME;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ACTION_CHOOSE;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ALBUM;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.CAMERA;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.HEAD;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.TAKE_PHOTO;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageFile_head;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;
import static com.mils.whisper.dialogfragment.SexDialogFragment.FEMALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.MALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.UNCERTAIN;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/2.
 */

public class SettingActivity extends BaseActivity implements SettingInterface.View,
        OnSexSelectListener, OnPicSelectListener, OnNameSettingListener, OnBriefSettingListener {

    @BindView(R.id.view_statusBar_as)
    public View view_statusBar;
    @BindView(R.id.rl_id_setting)
    public RelativeLayout rl_id;
    @BindView(R.id.txt_id_setting)
    public TextView txt_id;
    @BindView(R.id.rl_head_setting)
    public RelativeLayout rl_head;
    @BindView(R.id.cimg_head_setting)
    public CircleImageView cimg_head;
    @BindView(R.id.rl_name_setting)
    public RelativeLayout rl_name;
    @BindView(R.id.txt_name_setting)
    public TextView txt_username;
    @BindView(R.id.rl_sex_setting)
    public RelativeLayout rl_sex;
    @BindView(R.id.txt_sex_setting)
    public TextView txt_sex;
    @BindView(R.id.rl_brief_setting)
    public RelativeLayout rl_brief;
    @BindView(R.id.txt_brief_setting)
    public TextView txt_brief;
    @BindView(R.id.rl_phone_setting)
    public RelativeLayout rl_phone;
    @BindView(R.id.txt_phone_setting)
    public TextView txt_phone;
    @BindView(R.id.rl_account_change)
    public RelativeLayout rl_account_change;
    @BindView(R.id.btn_back_aas)
    public Button btn_back;

    private SexDialogFragment sexDialog;
    private PictureDialogFragment picDialog;
    private NameSettingDialogFragment nameSettingDialog;
    private BriefSettingDialogFragment briefSettingDialog;

    private User user;

    private SettingInterface.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        setSteepStatusBar(true);

        mPresenter = new SettingPresenter(this);
    }

    @Override
    public void initView() {
        sexDialog = SexDialogFragment.getInstance();
        picDialog = new PictureDialogFragment();
        initStatusBar(view_statusBar, LayoutConfig.RELATIVE_LAYOUT);
    }

    @Override
    public void initData() {
        initUserInform();
    }

    @OnClick(R.id.rl_sex_setting)
    public void sexSetting() {
        if (TimeUtil.fastClick()) {
            sexDialog.show(getFragmentManager(), "sex_dialog");
            Bundle bundle = new Bundle();
            bundle.putInt("sex", user.getSex());
            sexDialog.setArguments(bundle);
            sexDialog.setOnSexSelectListener(this);
        }
    }

    @OnClick(R.id.rl_head_setting)
    public void headSetting() {
        if (TimeUtil.fastClick()){
            picDialog.show(getFragmentManager(), "pic_dialog");
            picDialog.setOnPicSelectListener(this);
        }
    }

    @OnClick(R.id.rl_name_setting)
    public void nameSetting(){
        if (TimeUtil.fastClick()){
            nameSettingDialog = new NameSettingDialogFragment();
            nameSettingDialog.show(getFragmentManager(),"name_dialog");
            nameSettingDialog.setOnNameSettingListener(this);
            if (user != null){
                String username = user.getUsername();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Log.d(TAG,"username:"+username);
                nameSettingDialog.setArguments(bundle);
            }
        }
    }

    @OnClick(R.id.rl_brief_setting)
    public void briefSetting(){
        if(TimeUtil.fastClick()){
            briefSettingDialog = new BriefSettingDialogFragment();
            briefSettingDialog.show(getFragmentManager(),"brief_dialog");
            briefSettingDialog.setOnBriefSettingListener(this);
            if(user != null){
                String brief = user.getBrief();
                Bundle bundle = new Bundle();
                bundle.putString("brief",brief);
                briefSettingDialog.setArguments(bundle);
            }
        }
    }

    @OnClick(R.id.rl_account_change)
    public void logOut(){
        BmobUser.logOut();
        startActivity(LoginActivity.class);
        ActivityCollector.finishAll();
    }

    @OnClick(R.id.btn_back_aas)
    public void back(){
        finish();
    }

    private void initUserInform() {
        user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            txt_id.setText(user.getObjectId());
            txt_phone.setText(user.getMobilePhoneNumber());
            txt_username.setText(user.getUsername());
            initSex(user.getSex());
            txt_brief.setText(user.getBrief());
            if (user.getHead() != null) {
                Glide.with(this).load(user.getHead()).into(cimg_head);
            }
        }
    }

    private void initSex(int sex) {
        if (sex == UNCERTAIN) {
            txt_sex.setText(R.string.uncertain);
        } else if (sex == MALE) {
            txt_sex.setText(R.string.male);
        } else if (sex == FEMALE) {
            txt_sex.setText(R.string.female);
        }
    }

    public void OnSetSex(int sex) {
        mPresenter.onUserUpdateInteger(sex ,SEX);
    }

    public void OnSetBrief(String brief){
        briefSettingDialog.dismiss();
        mPresenter.onUserUpdateString(brief, BRIEF);
    }

    @Override
    public void OnSetPic(int pic) {
        switch (pic) {
            case ALBUM:
                mPresenter.openAlbum(this);
                break;
            case CAMERA:
                mPresenter.openCamera(this, imageFile_head);
                break;
        }
    }

    @Override
    public void onSetName(String username) {
        if (username != null && user != null){
            if (username != user.getUsername()){
                nameSettingDialog.dismiss();
                mPresenter.onUserUpdateString(username, USERNAME);
            }
        }else {
            ToastShort(getContext().getResources().getString(R.string.username_warn));
        }
    }

    @Override
    public void reloadUserInform() {
        initUserInform();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"activityResult");
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:  //拍照的回调
                mPresenter.cropImage(this, imageUri, imageFile_head,1,1,200,200);
                break;
            case ACTION_CHOOSE:  //选择照片的回调
                imageUri = data.getData();
                mPresenter.cropImage(this, imageUri, imageFile_head,1,1,200,200);
                break;
            default:
                break;
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            mPresenter.onUpload(FileUtil.getFileByUri(resultUri), HEAD);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d("TAG", "error:" + cropError);
        }
    }

    /*加载数据*/
    @Override
    public void loading(String loadMsg) {
        Loading(loadMsg);
    }

    /*加载失败*/
    @Override
    public void loadFailure(String loadfailMsg) {
        LoadFailure(loadfailMsg);
    }

    /*加载成功*/
    @Override
    public void loadSuccess() {
        LoadSuccess();
    }
}
