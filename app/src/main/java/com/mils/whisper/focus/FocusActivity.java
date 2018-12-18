package com.mils.whisper.focus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.bean.User;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.mils.whisper.app.MyApplicant.getContext;
import static com.mils.whisper.config.LayoutConfig.RELATIVE_LAYOUT;

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

    User currentUser;
    String from;

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
        currentUser = (User)bundle.getSerializable("user");
        from = (String)bundle.getString("from");
    }

    @Override
    public void initView() {
        if (null!=from){
            txt_focus.setText("他的关注");
        }
        initFocus();
    }

    private void initFocus(){
        BmobQuery<User> query = new BmobQuery<User>();
        User user = new User();
        user.setObjectId(currentUser.getObjectId());
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

    private void initAdapter(List<User> focusList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_focus.setLayoutManager(layoutManager);
        FocusAdapter focusAdapter = new FocusAdapter(focusList);
        rv_focus.setAdapter(focusAdapter);
    }

    @OnClick(R.id.btn_back_focus)
    public void back(){
        finish();
    }
}
