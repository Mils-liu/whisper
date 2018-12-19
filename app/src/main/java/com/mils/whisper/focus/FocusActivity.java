package com.mils.whisper.focus;

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
import com.mils.whisper.user.UserVisitActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.config.LayoutConfig.RELATIVE_LAYOUT;
import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/3.
 */

public class FocusActivity extends BaseActivity {
    @BindView(R.id.rv_focus)
    RecyclerView rv_focus;
    @BindView(R.id.txt_focus_num)
    TextView txt_focusNum;
    @BindView(R.id.txt_focus)
    TextView txt_focus;
    @BindView(R.id.view_statusBar_focus)
    View statusBar;
    @BindView(R.id.btn_back_focus)
    Button btn_back;
    @BindView(R.id.rl_loading_focus)
    RelativeLayout rl_loading;
    RelativeLayout rl_myfocus;
    Button btn_unfocus;

    User user;
    String from;

    Boolean currentFlag=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        setSteepStatusBar(true);
        initStatusBar(statusBar,RELATIVE_LAYOUT);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable("user");
        from = (String)bundle.getString("from");

    }

    @Override
    public void initView() {
        if (null!=from){
            txt_focus.setText("他的关注");
            currentFlag=false;
        }
        initFocus();
    }

    private void initFocus(){
        BmobQuery<User> query = new BmobQuery<User>();
        User myUser = new User();
        myUser.setObjectId(user.getObjectId());
        query.addWhereRelatedTo("focusUser", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> focusList, BmobException e) {
                if(e==null){
                    initAdapter(focusList);
                    txt_focusNum.setText("("+focusList.size()+")");
                }else{
                    initAdapter(focusList);
                    Log.d(TAG,"error:"+e);
                }
            }
        });
    }

    private void initAdapter(final List<User> focusList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_focus.setLayoutManager(layoutManager);
        final FocusAdapter focusAdapter = new FocusAdapter(focusList,currentFlag);
        rv_focus.setAdapter(focusAdapter);
        rl_loading.setVisibility(View.GONE);
        rl_myfocus = (RelativeLayout)findViewById(R.id.rl_myfocus);
        focusAdapter.setOnRecyclerViewListener(new FocusAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.btn_unfocus:
                        btn_unfocus = (Button) view.findViewById(R.id.btn_unfocus);
                        if (btn_unfocus.getText().equals(getResources().getString(R.string.cancelfocus))){
                            Focus.cancelFocus(user,focusList.get(position),btn_unfocus);
                        }else {
                            Focus.doFocus(user,focusList.get(position),btn_unfocus);
                        }
                        break;
                    case R.id.rl_myfocus:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user",focusList.get(position));
                        startActivity(UserVisitActivity.class,bundle);
                        break;
                }
            }
        });
    }
    /*关注*/
    private void doFocus(final User myUser,final User focusUser){
        Log.d(TAG,"userObjectId:"+focusUser.getObjectId());
        /*添加关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(focusUser);
        user.setFocusUser(relation);
        /*关注数加1*/
        user.increment("focusNum",1);
        user.update(myUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null||e.getErrorCode()==9015){
                    addFan(myUser,focusUser);
                }else {
                    Log.d(TAG,"error1:"+e.getMessage());
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*取关*/
    private void cancelFocus(final User myUser,final User focusUser){
        /*删除关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(focusUser);
        user.setFocusUser(relation);
        /*关注数减1*/
        user.increment("focusNum",-1);
        user.update(myUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    deleteFan(myUser,focusUser);
                }else {
                    Log.d(TAG,"error1:"+e.getMessage());
                    ToastShort("取关失败！");
                }
            }
        });
    }

    /*被关注用户添加粉丝*/
    private void addFan(User myUser,User focusUser){
        Fans fans = new Fans();
        String fansObject = focusUser.getFans().getObjectId();
        fans.setObjectId(fansObject);
        Log.d(TAG,"fans_objectId:"+fansObject);
                User user = new User();
                user.setObjectId(myUser.getObjectId());
        Log.d(TAG,"myUser_objectId:"+myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum",1);
        fans.update(fansObject,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    focus();
                    ToastShort("关注成功");
                }else {
                    Log.d(TAG,"error2:"+e);
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*被关注用户删除粉丝*/
    private void deleteFan(final User myUser,final User focusUser){
        Fans fans = new Fans();
        fans.setObjectId(focusUser.getFans().getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum",-1);
        fans.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    unFocus();
                    ToastShort("取关成功");
                }else {
                    Log.d(TAG,"error2:"+e);
                    ToastShort("取关失败！");
                }
            }
        });
    }

    private void focus() {
        btn_unfocus.setBackgroundResource(R.drawable.shape_rounded_rectangle_unselect);
        btn_unfocus.setTextColor(getResources().getColor(R.color.darkgray));
        btn_unfocus.setText(getResources().getString(R.string.cancelfocus));
    }

    private void unFocus() {
        btn_unfocus.setBackgroundResource(R.drawable.shape_rounded_rectangle_select);
        btn_unfocus.setTextColor(getResources().getColor(R.color.white));
        btn_unfocus.setText(getResources().getString(R.string.dofocus));
    }

    @OnClick(R.id.btn_back_focus)
    public void back(){
        finish();
    }
}
