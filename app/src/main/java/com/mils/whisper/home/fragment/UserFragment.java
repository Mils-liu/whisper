package com.mils.whisper.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.accountsetting.SettingActivity;
import com.mils.whisper.article.article_edit.EditArticleActivity;
import com.mils.whisper.article.articlelist.ArticleListActivity;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.dialogfragment.PictureDialogFragment;
import com.mils.whisper.dialogfragment.listener.OnPicSelectListener;
import com.mils.whisper.fans.FansActivity;
import com.mils.whisper.focus.FocusActivity;
import com.mils.whisper.home.user.presenter.UserInterface;
import com.mils.whisper.home.user.presenter.UserPresenter;
import com.mils.whisper.util.DisplayUtils;
import com.mils.whisper.util.FileUtil;
import com.mils.whisper.util.TimeUtil;
import com.mils.whisper.util.WindowAttr;
import com.yalantis.ucrop.UCrop;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ACTION_CHOOSE;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.ALBUM;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.BACKGROUND;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.CAMERA;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.TAKE_PHOTO;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageFile_bg;
import static com.mils.whisper.dialogfragment.PictureDialogFragment.imageUri;
import static com.mils.whisper.dialogfragment.SexDialogFragment.FEMALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.MALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.UNCERTAIN;

/**
 * Created by Administrator on 2018/6/1.
 */

public class UserFragment extends BaseFragment implements UserInterface.View, OnPicSelectListener {

    @BindView(R.id.img_sex)
    public ImageView img_sex;
    @BindView(R.id.img_userbackground)
    public ImageView img_userbackground;
    @BindView(R.id.cimg_userhead)
    public CircleImageView cimg_userhead;
    @BindView(R.id.txt_brief)
    public TextView txt_brief;
    @BindView(R.id.txt_username)
    public TextView txt_username;
    @BindView(R.id.txt_myfans)
    public TextView txt_myfans;
    @BindView(R.id.txt_myfocus)
    public TextView txt_myfocus;
    @BindView(R.id.txt_myfans_num)
    public TextView txt_fansnum;
    @BindView(R.id.txt_myfocus_num)
    public TextView txt_focusnum;
    @BindView(R.id.txt_article_creation)
    public TextView txt_creatarticle;
    @BindView(R.id.txt_user_inform)
    public TextView txt_userinform;
    @BindView(R.id.cv_myarticle)
    public CardView cv_myarticle;
    @BindView(R.id.cv_mycollection)
    public CardView cv_mycollection;
    @BindView(R.id.img_mycover)
    public ImageView img_mycover;
    @BindView(R.id.img_collectCover)
    public ImageView img_collectCover;

    private PictureDialogFragment picDialog;
    private String TAG = "UserFragment";
    private int background_height;
    private int background_width;

    private User user;
    private UserInterface.Presenter mPresenter;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView() {
        super.initView();

        picDialog = new PictureDialogFragment();
        picDialog.setOnPicSelectListener(this);
        initUserbackground();
        initUserHead();
    }

    @Override
    public void onStart() {
        super.onStart();
        initUserInform();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        mPresenter = new UserPresenter(this);
    }

    @OnClick(R.id.txt_myfans)
    public void toFans() {
        if (TimeUtil.fastClick()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            startActivity(FansActivity.class,bundle);
        }
    }

    @OnClick(R.id.txt_myfocus)
    public void toFocus() {
        if (TimeUtil.fastClick()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            startActivity(FocusActivity.class,bundle);
        }
    }

    @OnClick(R.id.txt_article_creation)
    public void toCreateArticle() {
        if (TimeUtil.fastClick()) {
            startActivity(EditArticleActivity.class);
        }
    }

    @OnClick(R.id.txt_user_inform)
    public void toUserInform() {
        if (TimeUtil.fastClick()) {
            if (user != null) {
                startActivity(SettingActivity.class);
            }
        }
    }

    @OnClick(R.id.cv_myarticle)
    public void toMyArticle() {
        if (TimeUtil.fastClick()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            bundle.putSerializable("from","user");
            bundle.putSerializable("type","article");
            startActivity(ArticleListActivity.class,bundle);
        }
    }

    @OnClick(R.id.cv_mycollection)
    public void toMyCollection() {
        if (TimeUtil.fastClick()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            bundle.putSerializable("from","user");
            bundle.putSerializable("type","collection");
            startActivity(ArticleListActivity.class,bundle);
        }
    }

    @OnClick(R.id.img_userbackground)
    public void backgroundSetting() {
        if (TimeUtil.fastClick()) {
            picDialog.show(mActivity.getFragmentManager(), "pic_dialog");
            background_width = img_userbackground.getWidth();
        }
    }

    private void initUserInform() {
        user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            txt_username.setText(user.getUsername());
            Log.d(TAG,"username:"+user.getUsername());
            txt_focusnum.setText(user.getFocusNum().toString());
            initSex(user.getSex());
            txt_brief.setText(user.getBrief().toString());
            Log.d(TAG, user.getHead());
            if (user.getHead() != null) {
                Log.d(TAG,"head:"+user.getHead());
                Glide.with(this).load(user.getHead()).into(cimg_userhead);
            }
            if (user.getUserBackground() != null) {
                Glide.with(this).load(user.getUserBackground())
                        .into(img_userbackground);
            }
            initFansNum();
            initUserArticleCover(user);
            initCollectArticleCover(user);
        }
    }

    /*加载用户粉丝数*/
    private void initFansNum(){
        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.include("fans");
        userQuery.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    txt_fansnum.setText(Integer.toString(user.getFans().getFansNum()));
                }else {
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    /*加载“我的文字”封面*/
    private void initUserArticleCover(User user){
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> object, BmobException e) {
                if(e==null){
                    Log.d(TAG,"cover:"+object.get(0).getArticleCover().getUrl());
                    Glide.with(getContext()).load(object.get(0).getArticleCover().getUrl()).into(img_mycover);
                    Log.d(TAG,"title:"+object.get(0).getTitle());
                }else {
                    Glide.with(getContext()).load(R.drawable.image).into(img_collectCover);
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    /*加载“我的最爱”封面*/
    private void initCollectArticleCover(User user){
        List<String> collectList = user.getCollectList();
        if (null!=collectList&&collectList.size()>0){
            String objectId = collectList.get(collectList.size()-1);
            BmobQuery<Article> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId",objectId);
            query.findObjects(new FindListener<Article>() {
                @Override
                public void done(List<Article> list, BmobException e) {
                    if (e==null){
                        Glide.with(getContext()).load(list.get(0).getArticleCover().getUrl()).into(img_collectCover);
                    }else {
                        Glide.with(getContext()).load(R.drawable.image).into(img_collectCover);
                        Log.d(TAG,"error:"+e);
                    }
                }
            });
        }
    }

    /*初始化性别*/
    private void initSex(Integer sex) {
        if (sex == UNCERTAIN) {
            img_sex.setImageResource(R.drawable.sex_uncertain);
        } else if (sex == MALE) {
            img_sex.setImageResource(R.drawable.sex_male);
        } else if (sex == FEMALE) {
            img_sex.setImageResource(R.drawable.sex_female);
        }
    }

    @Override
    public void OnSetPic(int pic) {
        switch (pic) {
            case CAMERA:
                mPresenter.openCamera(mActivity, imageFile_bg);
                break;
            case ALBUM:
                mPresenter.openAlbum(mActivity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult:"+"UserResult");
        if (resultCode != RESULT_OK) {
            Log.d(TAG,"NULL");
            return;
        }else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d(TAG, "error:" + cropError);
        }
        Log.d(TAG,"background_width:"+background_width);
        Log.d(TAG,"backgroud_height:"+background_height);
        switch (requestCode) {
            case TAKE_PHOTO:  //拍照的回调
                Log.d(TAG,"imgUri:"+imageUri);
                Log.d(TAG,"imgFile_bg"+imageFile_bg);
                mPresenter.cropImage(mActivity,imageUri, imageFile_bg,
                        background_width, background_height, background_width, background_height);
                break;
            case ACTION_CHOOSE:  //选择照片的回调
                imageUri = data.getData();
                Log.d(TAG,"imgUri:"+imageUri);
                mPresenter.cropImage(mActivity,imageUri, imageFile_bg,
                        background_width, background_height, background_width, background_height);
                break;
            case UCrop.REQUEST_CROP:
                Log.d(TAG,"cropreturn");
                final Uri resultUri = UCrop.getOutput(data);
                mPresenter.onUpload(FileUtil.getFileByUri(resultUri), BACKGROUND);
            default:
                break;
        }
    }

    @Override
    public void reloadUserInform() {
        initUserInform();
    }

    @Override
    public void loading(String loadMsg) {
        Loading(loadMsg);
    }

    @Override
    public void loadFailure(String loadfailMsg) {
        LoadFailure(loadfailMsg);
    }

    @Override
    public void loadSuccess() {
        LoadSuccess();
    }

    /*动态设置用户封面高度*/
    private void initUserbackground() {
        background_height = DisplayUtils.dp2px(mActivity, 150) + WindowAttr.getStateBarHeight(mActivity);
        img_userbackground.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                background_height));
    }

    /*动态设置用户头像的位置*/
    private void initUserHead() {
        int head_margin = DisplayUtils.dp2px(mActivity, 100) + WindowAttr.getStateBarHeight(mActivity);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
                cimg_userhead.getLayoutParams());
        margin.setMargins(0, head_margin, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        cimg_userhead.setLayoutParams(layoutParams);
    }
}
