package com.mils.whisper.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseFragment;
import com.mils.whisper.bean.Fans;
import com.mils.whisper.bean.User;
import com.mils.whisper.search.SearchActivity;
import com.mils.whisper.search.UserSearchAdapter;
import com.mils.whisper.user.UserVisitActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.mils.whisper.util.ToastUtil.ToastShort;

/**
 * Created by Administrator on 2018/6/3.
 */

public class UserSearchFragment extends BaseFragment implements SearchActivity.OnUserSearchListener {

    @BindView(R.id.rv_search_user)
    RecyclerView rv_usersearch;
    @BindView(R.id.txt_end_user)
    TextView end;
    Button btn_dofocus;

    private String TAG = "UserSearchFragment";
    private List<User> userList = new ArrayList<User>();
    private UserSearchAdapter userSearchAdapter;
    private User myUser;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SearchActivity) context).setOnUserSearchListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myUser = BmobUser.getCurrentUser(User.class);
    }

    @Override
    public void doUserSearch(final String searchText, final UserSearchCallback callback) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.order("-createdAt");
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> objects, BmobException e) {
                if (e == null) {
                    Log.d("doSearch", "startUserSearch");
                    if (objects.size() > 0) {
                        Log.d("doSearch", "doUserSearch");
                        if (userList.size() > 0) {
                            userList.clear();
                        }
                        for (User user : objects) {
                            if (user.getUsername().indexOf(searchText) != -1) {
                                userList.add(user);
                            }
                        }
                        Log.d(TAG, "username:" + userList.get(0).getUsername());
                        if (userList.size() > 0) {
                            end.setVisibility(View.INVISIBLE);
                            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            rv_usersearch.setLayoutManager(layoutManager);
                            userSearchAdapter = new UserSearchAdapter(userList, myUser);
                            rv_usersearch.setAdapter(userSearchAdapter);

                            callback.onSuccess();
                            Log.d("doSearch", "searchUserSuccess");

                            userSearchAdapter.setOnRecyclerViewListener(new UserSearchAdapter.OnRecyclerViewListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.btn_dofocus:
                                            Log.d(TAG, "objectId:" + myUser.getObjectId());
                                            btn_dofocus = (Button) view.findViewById(R.id.btn_dofocus);
                                            if (myUser.getObjectId().equals(userList.get(position).getObjectId())) {
                                                ToastShort("不能关注自己！");
                                            } else {
                                                if (btn_dofocus.getTag().equals(getResources().getString(R.string.dofocus))) {
                                                    doFocus(myUser, userList.get(position));
                                                } else {
                                                    cancelFocus(myUser, userList.get(position));
                                                }
                                            }
                                            Log.d(TAG, "listobjectId:" + userList.get(position).getObjectId());
                                            break;
                                        case R.id.rl_user_search:
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("user", userList.get(position));
                                            startActivity(UserVisitActivity.class, bundle);
                                            break;
                                    }
                                }
                            });
                        } else {
                            userSearchAdapter.notifyDataSetChanged();
                            callback.onSuccess();
                            end.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (userList.size() > 0) {
                            userList.clear();
                        }
                        userSearchAdapter.notifyDataSetChanged();
                        end.setVisibility(View.VISIBLE);
                    }
                } else {
                    end.setVisibility(View.VISIBLE);
                    callback.onFailure(e.getErrorCode());
                    Log.d("doSearch", "searchError2:" + e);
                }
            }
        });
    }

    /*关注*/
    private void doFocus(final User myUser, final User focusUser) {
        Log.d(TAG, "userObjectId:" + focusUser.getObjectId());
        /*添加关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(focusUser);
        user.setFocusUser(relation);
        /*关注数加1*/
        user.increment("focusNum", 1);
        user.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null || e.getErrorCode() == 9015) {
                    addFan(myUser, focusUser);
                } else {
                    Log.d(TAG, "error1:" + e.getMessage());
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*取关*/
    private void cancelFocus(final User myUser, final User focusUser) {
        /*删除关注的关联*/
        User user = new User();
        user.setObjectId(myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(focusUser);
        user.setFocusUser(relation);
        /*关注数减1*/
        user.increment("focusNum", -1);
        user.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    deleteFan(myUser, focusUser);
                } else {
                    Log.d(TAG, "error1:" + e.getMessage());
                    ToastShort("取关失败！");
                }
            }
        });
    }

    /*被关注用户添加粉丝*/
    private void addFan(User myUser, User focusUser) {
        Fans fans = new Fans();
        String fansObject = focusUser.getFans().getObjectId();
        fans.setObjectId(fansObject);
        Log.d(TAG, "fans_objectId:" + fansObject);
        Log.d(TAG, "myUser_objectId:" + myUser.getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.add(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum", 1);
        fans.update(fansObject, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    focus();
                    ToastShort("关注成功");
                } else {
                    Log.d(TAG, "error2:" + e);
                    ToastShort("关注失败！");
                }
            }
        });
    }

    /*被关注用户删除粉丝*/
    private void deleteFan(final User myUser, final User focusUser) {
        Fans fans = new Fans();
        fans.setObjectId(focusUser.getFans().getObjectId());
        BmobRelation relation = new BmobRelation();
        relation.remove(myUser);
        fans.setFansList(relation);
        fans.increment("fansNum", -1);
        fans.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    unFocus();
                    ToastShort("取关成功");
                } else {
                    Log.d(TAG, "error2:" + e);
                    ToastShort("取关失败！");
                }
            }
        });
    }

    private void focus() {
        btn_dofocus.setBackgroundResource(R.drawable.shape_rounded_rectangle_unselect);
        btn_dofocus.setTextColor(getResources().getColor(R.color.darkgray));
        btn_dofocus.setText(getResources().getString(R.string.cancelfocus));
        btn_dofocus.setTag(getResources().getString(R.string.cancelfocus));
    }

    private void unFocus() {
        btn_dofocus.setBackgroundResource(R.drawable.shape_rounded_rectangle_select);
        btn_dofocus.setTextColor(getResources().getColor(R.color.white));
        btn_dofocus.setText(getResources().getString(R.string.dofocus));
        btn_dofocus.setTag(getResources().getString(R.string.dofocus));
    }
}
