package com.mils.whisper.search;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/11/29.
 */

public class UserSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<User> mUserList;
    private String TAG = "UserSearchAdapter";
    private User mUser;

    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener{
        void onItemClick(View view, int position);
    }
    public void setOnRecyclerViewListener(OnRecyclerViewListener mOnItemClickListener){
        this.onRecyclerViewListener=mOnItemClickListener;
    }

    public int getContentSize(){
        return mUserList.size();
    }

    class UserSearchHolder extends RecyclerView.ViewHolder{
        CircleImageView userHead;
        TextView userName;
        Button btn_dofocus;
        RelativeLayout rl_user_search;
        UserSearchHolder(View view){
            super(view);
            userHead = (CircleImageView)view.findViewById(R.id.ci_head_mysearch);
            userName = (TextView)view.findViewById(R.id.txt_name_mysearch);
            btn_dofocus = (Button)view.findViewById(R.id.btn_dofocus);
            rl_user_search = (RelativeLayout)view.findViewById(R.id.rl_user_search);
        }
    }

    public UserSearchAdapter(List<User> mUserList, User mUser){
        this.mUserList = mUserList;
        this.mUser = mUser;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_usersearch, parent, false);
            return new UserSearchHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserSearchHolder){
            final User user = mUserList.get(position);
            UserSearchHolder userSearchViewHolder = (UserSearchHolder) holder;
            userSearchViewHolder.userName.setText(user.getUsername());
            userSearchViewHolder.btn_dofocus.setTag(getContext().getResources().getString(R.string.dofocus));
            Glide.with(getContext()).load(user.getHead()).into(userSearchViewHolder.userHead);

            BmobQuery<User> query = new BmobQuery<User>();
            User myUser = new User();
            myUser.setObjectId(mUser.getObjectId());
            query.addWhereRelatedTo("focusUser",new BmobPointer(myUser));
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e==null){
                        Log.d(TAG,"userlist:"+list.get(0).getUsername());
                        for (User user1 : list) {
                             if (user1.getObjectId().equals(user.getObjectId())){
                                 ((UserSearchHolder) holder).btn_dofocus.setBackgroundResource(R.drawable.shape_rounded_rectangle_unselect);
                                 ((UserSearchHolder) holder).btn_dofocus.setTextColor(getContext().getResources().getColor(R.color.darkgray));
                                 ((UserSearchHolder) holder).btn_dofocus.setTag(getContext().getResources().getString(R.string.cancelfocus));
                                 ((UserSearchHolder) holder).btn_dofocus.setText(getContext().getResources().getString(R.string.cancelfocus));
                             }
                        }
                    } else {
                        Log.d(TAG,"error:"+e);
                    }
                }
            });

            if(onRecyclerViewListener!=null){
                ((UserSearchHolder) holder).btn_dofocus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((UserSearchHolder) holder).btn_dofocus,position);
                    }
                });
                ((UserSearchHolder) holder).rl_user_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((UserSearchHolder) holder).rl_user_search,position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}
