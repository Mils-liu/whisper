package com.mils.whisper.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.article.articlelist.ArticleAdapter;
import com.mils.whisper.bean.Article;
import com.mils.whisper.bean.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/12/13.
 */

public class DynamicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int HEAD_COUNT = 0;
    private final static int FOOT_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;

    private User currentUser;
    private int viewType;
    private List<String> likeList;

    private ArticleAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerViewListener(ArticleAdapter.OnRecyclerViewListener mOnItemClickListener){
        this.onRecyclerViewListener=mOnItemClickListener;
    }

    private List<Article> articleList;

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (articleList.get(position).getType()==TYPE_FOOTER){
            return TYPE_FOOTER;
        }else {
            return TYPE_CONTENT;
        }
    }

    public DynamicsAdapter(List<Article> articleList,User currentUser){
        this.articleList = articleList;
        this.currentUser = currentUser;
        likeList = currentUser.getLikeList();
    }

    public DynamicsAdapter(int viewType){
        this.viewType = viewType;
    }

    class EndViewHolder extends RecyclerView.ViewHolder{
        TextView txt_end;
        public EndViewHolder(View view){
            super(view);
            txt_end = (TextView)view.findViewById(R.id.txt_end_dy);
        }
    }

    class DynamicsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ci_head;
        TextView txt_username;
        TextView txt_creation;
        Button btn_like;
        ImageView img_cover;
        TextView txt_content;
        TextView txt_title;
        RelativeLayout rl_article;
        RelativeLayout rl_inform;
        public DynamicsViewHolder(View view){
            super(view);
            ci_head = (CircleImageView)view.findViewById(R.id.ci_head);
            txt_username = (TextView)view.findViewById(R.id.txt_username_dy);
            txt_creation = (TextView)view.findViewById(R.id.txt_creation_dy);
            btn_like = (Button)view.findViewById(R.id.btn_like_dy);
            img_cover = (ImageView)view.findViewById(R.id.img_cover_dy);
            txt_content = (TextView)view.findViewById(R.id.txt_content_dy);
            txt_title = (TextView)view.findViewById(R.id.txt_title_dy);
            rl_article = (RelativeLayout)view.findViewById(R.id.rl_article_dynamics);
            rl_inform = (RelativeLayout)view.findViewById(R.id.rl_inform);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==TYPE_CONTENT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_dynamics, parent, false);
            DynamicsViewHolder holder = new DynamicsViewHolder(view);
            holder.setIsRecyclable(false);//取消复用
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_end_dy, parent, false);
            return new EndViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DynamicsViewHolder){
            Article article = articleList.get(position);
            User author = article.getAuthor();
            Glide.with(getContext()).load(article.getAuthor().getHead()).into(((DynamicsViewHolder) holder).ci_head);
            ((DynamicsViewHolder) holder).txt_username.setText(author.getUsername());
            ((DynamicsViewHolder) holder).txt_title.setText(article.getTitle());
            ((DynamicsViewHolder) holder).txt_content.setText(article.getContent());
            ((DynamicsViewHolder) holder).txt_creation.setText(article.getCreatedAt());
            Glide.with(getContext()).load(article.getArticleCover().getFileUrl()).into(((DynamicsViewHolder) holder).img_cover);

            if (null!=likeList){
                for (String objectId : likeList) {
                    if (objectId.equals(article.getObjectId())){
                        ((DynamicsViewHolder) holder).btn_like.setBackgroundResource(R.drawable.like);
                        ((DynamicsViewHolder) holder).btn_like.setTag(getContext().getResources().getString(R.string.like));
                    }else {
                        ((DynamicsViewHolder) holder).btn_like.setTag(getContext().getResources().getString(R.string.unlike));
                    }
                }
            }

            if(onRecyclerViewListener!=null){
                ((DynamicsViewHolder) holder).btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((DynamicsViewHolder) holder).btn_like,position);
                    }
                });
                ((DynamicsViewHolder) holder).rl_inform.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((DynamicsViewHolder) holder).rl_inform,position);
                    }
                });
                ((DynamicsViewHolder) holder).rl_article.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((DynamicsViewHolder) holder).rl_article,position);
                    }
                });
            }
        } else if (holder instanceof EndViewHolder){
            if (null==articleList){
                ((EndViewHolder) holder).txt_end.setText(R.string.empty);
            } else {
                ((EndViewHolder) holder).txt_end.setText(R.string.end);
            }
        }
    }
}
