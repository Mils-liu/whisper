package com.mils.whisper.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.article.articlelist.ArticleListActivity;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;
import com.mils.whisper.fans.FansActivity;
import com.mils.whisper.focus.FocusActivity;
import com.mils.whisper.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;
import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.dialogfragment.SexDialogFragment.FEMALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.MALE;
import static com.mils.whisper.dialogfragment.SexDialogFragment.UNCERTAIN;

/**
 * Created by Administrator on 2018/12/1.
 */

public class UserVisitActivity extends BaseActivity {

    @BindView(R.id.img_userbackground_visit)
    ImageView img_userBackground;
    @BindView(R.id.ci_head_visit)
    CircleImageView ci_head;
    @BindView(R.id.txt_name_visit)
    TextView txt_username;
    @BindView(R.id.txt_brief_visit)
    TextView txt_brief;
    @BindView(R.id.txt_fans_num_visit)
    TextView txt_fansNum;
    @BindView(R.id.txt_focus_num_visit)
    TextView txt_focusNum;
    @BindView(R.id.img_sex_visit)
    ImageView img_sex;
    @BindView(R.id.img_mycover_visit)
    ImageView img_myCover;
    @BindView(R.id.img_loveCover_visit)
    ImageView img_loveCover;
    @BindView(R.id.cv_userarticle_visit)
    CardView cv_userarticle;
    @BindView(R.id.cv_collectarticle_visit)
    CardView cv_collectarticle;
    @BindView(R.id.btn_back_auv)
    Button btn_back;
    @BindView(R.id.txt_fans_visit)
    TextView txt_fans;
    @BindView(R.id.txt_focus_visit)
    TextView txt_focus;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_visit);
        setSteepStatusBar(true);
    }

    @Override
    public void initView() {
        Glide.with(getApplicationContext()).load(user.getUserBackground()).into(img_userBackground);
        Glide.with(getApplicationContext()).load(user.getHead()).into(ci_head);
        txt_username.setText(user.getUsername());
        if (isEmpty(user.getBrief().toString())){
            txt_brief.setText(getResources().getString(R.string.brief_introduce));
        }else {
            txt_brief.setText(user.getBrief());
        }
        initUserArticleCover(user);
        initUserSex(user);
        initUserfansNum(user);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
    }

    private void initUserArticleCover(User user){
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> object, BmobException e) {
                if(e==null){
                    Log.d(TAG,"cover:"+object.get(0).getArticleCover().getUrl());
                    Glide.with(getApplicationContext()).load(object.get(0).getArticleCover().getUrl()).into(img_myCover);
                    Log.d(TAG,"title:"+object.get(0).getTitle());
                }else {
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    private void initCollectArticleCover(User user){
        List<String> collectList = user.getCollectList();
        if (null!=collectList){
            String objectId = collectList.get(collectList.size()-1);
            BmobQuery<Article> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId",objectId);
            query.findObjects(new FindListener<Article>() {
                @Override
                public void done(List<Article> list, BmobException e) {
                    if (e==null){
                        Glide.with(getContext()).load(list.get(0).getArticleCover().getUrl()).into(img_loveCover);
                    }else {
                        Log.d(TAG,"error:"+e);
                    }
                }
            });
        }
    }

    private void initUserSex(User user){
        int sex = user.getSex();
        if (sex == MALE) {
            img_sex.setImageResource(R.drawable.sex_male);
        } else if(sex == FEMALE){
            img_sex.setImageResource(R.drawable.sex_female);
        } else if(sex == UNCERTAIN){
            img_sex.setImageResource(R.drawable.sex_uncertain);
        }
    }

    private void initUserfansNum(User user){
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.include("fans");
        userBmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    txt_fansNum.setText(Integer.toString(user.getFans().getFansNum()));
                }else {
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    @OnClick(R.id.cv_userarticle_visit)
    public void userArticle(){
        if (TimeUtil.fastClick()){
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            bundle.putString("from","search");
            bundle.putString("type","article");
            startActivity(ArticleListActivity.class,bundle);
        }
    }

    @OnClick(R.id.cv_collectarticle_visit)
    public void collectArticle(){
        if (TimeUtil.fastClick()){
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            bundle.putString("from","search");
            bundle.putString("type","collection");
            startActivity(ArticleListActivity.class,bundle);
        }
    }

    @OnClick(R.id.btn_back_auv)
    public void back(){
        if (TimeUtil.fastClick()){
            finish();
        }
    }

    @OnClick(R.id.txt_fans_visit)
    public void toFans(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        bundle.putString("from","search");
        startActivity(FansActivity.class,bundle);
    }

    @OnClick(R.id.txt_focus_visit)
    public void toFocus(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        bundle.putString("from","search");
        startActivity(FocusActivity.class,bundle);
    }
}
