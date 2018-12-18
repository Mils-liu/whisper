package com.mils.whisper.fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.bean.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/12/12.
 */

public class FansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int HEAD_COUNT = 0;
    private final static int FOOT_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;

    private List<User> fansList;

    public int getContentSize(){
        if (null==fansList){
            return 0;
        }
        return fansList.size();
    }

    @Override
    public int getItemCount() {
        return getContentSize()+FOOT_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int contentsize = getContentSize();
        if(HEAD_COUNT!=0&&position==0){
            return TYPE_HEAD;
        }else if(FOOT_COUNT!=0&&position==HEAD_COUNT+contentsize){
            return TYPE_FOOTER;
        }else {
            return TYPE_CONTENT;
        }
    }

    public FansAdapter(List<User> fansList){
        this.fansList = fansList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==TYPE_CONTENT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_myfans, parent, false);
            return new FansViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_end, parent, false);
            return new EndViewHolder(view);
        }
    }

    class FansViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ci_head;
        TextView txt_focus;
        TextView txt_username;
        public FansViewHolder(View view){
            super(view);
            ci_head = (CircleImageView)view.findViewById(R.id.ci_head_myfans);
            txt_focus = (TextView)view.findViewById(R.id.txt_focus_myfans);
            txt_username = (TextView)view.findViewById(R.id.txt_username_myfans);
        }
    }

    class EndViewHolder extends RecyclerView.ViewHolder{
        TextView txt_end;
        public EndViewHolder(View view){
            super(view);
            txt_end = (TextView)view.findViewById(R.id.txt_end);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FansViewHolder){
            User fan = fansList.get(position);
            Glide.with(getContext()).load(fan.getHead()).into(((FansViewHolder) holder).ci_head);
            ((FansViewHolder) holder).txt_username.setText(fan.getUsername());
        } else if (holder instanceof EndViewHolder){
            if (getContentSize()==0){
                ((EndViewHolder) holder).txt_end.setText(R.string.empty);
            } else {
                ((EndViewHolder) holder).txt_end.setText(R.string.end);
            }
        }
    }
}
