package com.mils.whisper.fans;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.Fans;
import com.mils.whisper.bean.User;
import com.mils.whisper.config.LayoutConfig;
import com.mils.whisper.user.UserVisitActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/6/3.
 */

public class FansActivity extends BaseActivity {

    @BindView(R.id.btn_back_fans)
    Button btn_back;
    @BindView(R.id.txt_fans_num)
    TextView txt_fansNum;
    @BindView(R.id.rv_fans)
    RecyclerView rv_fans;
    @BindView(R.id.view_statusBar_fans)
    View statusBar;
    @BindView(R.id.txt_fans)
    TextView txt_fans;
    @BindView(R.id.rl_loading_fans)
    RelativeLayout rl_loading;

    private FansAdapter fansAdapter;
    private List<User> fansList = new ArrayList<>();
    private User user;
    private String from;

    private Boolean currentFlag=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        from = (String)bundle.getString("from");
        initFans();
    }

    @Override
    public void initView() {
        setSteepStatusBar(true);
        initStatusBar(statusBar, LayoutConfig.RELATIVE_LAYOUT);
        if (null!=from){
            txt_fans.setText("他的粉丝");
            currentFlag=false;
        }
    }

    /*通过一对一关联查找用户粉丝表*/
    private void initFans(){
        Log.d(TAG,"doInitFans");
        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.include("fans");
        userQuery.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    Log.d(TAG,"username:"+user.getUsername());
                    initFansList(user.getFans());
                }else {
                    initFansList(user.getFans());
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    /*通过多对多关联查找粉丝表里的所有粉丝*/
    private void initFansList(final Fans fans){
        Log.d(TAG,"doInitFansList");
        BmobQuery<User> fansQuery = new BmobQuery<User>();
        Fans myfans = new Fans();
        myfans.setObjectId(fans.getObjectId());
        fansQuery.addWhereRelatedTo("fansList",new BmobPointer(myfans));
        fansQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> fansList, BmobException e) {
                if (e==null){
                    Log.d(TAG,"fansUsername:"+fansList.get(0).getUsername());
                    txt_fansNum.setText("("+fansList.size()+")");
                    initAdapter(fansList);
                }else {
                    initAdapter(fansList);
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    private void initAdapter(final List<User> fansList){
        Log.d(TAG,"doInitAdapter");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_fans.setLayoutManager(layoutManager);
        fansAdapter = new FansAdapter(fansList,currentFlag);
        rv_fans.setAdapter(fansAdapter);
        rl_loading.setVisibility(View.GONE);

        fansAdapter.setOnRecyclerViewListener(new FansAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.txt_focus_myfans:
                        break;
                    case R.id.rl_myfans:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user",fansList.get(position));
                        startActivity(UserVisitActivity.class,bundle);
                }
            }
        });
    }

    @OnClick(R.id.btn_back_fans)
    public void back(){
        finish();
    }

}
