package com.mils.whisper.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.bean.Article;

import java.util.List;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/12/11.
 */

public class RecommendArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> mArticleList;
    private String TAG = "RecommendArticleAdapter";
    private int EMPTY_VIEW=0;
    private int PROGRESS_VIEW=1;
    private int ARTICLE_VIEW=2;

    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener mOnItemClickListener) {
        this.onRecyclerViewListener = mOnItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mArticleList.size()==0){
            return EMPTY_VIEW;
        } else if(mArticleList.get(position).getType()==PROGRESS_VIEW){
            return PROGRESS_VIEW;
        }else {
            return ARTICLE_VIEW;
        }
    }

    public int getContentSize() {
        return mArticleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_content;
        ImageView img_cover;
        RelativeLayout rl_article;

        ArticleViewHolder(View view) {
            super(view);
            txt_content = (TextView) view.findViewById(R.id.txt_content_recommend);
            txt_title = (TextView) view.findViewById(R.id.txt_title_recommend);
            img_cover = (ImageView) view.findViewById(R.id.img_cover_recommend);
            rl_article = (RelativeLayout) view.findViewById(R.id.rl_article_recommend);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public ProgressViewHolder(View view){
            super(view);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        }
    }

    public RecommendArticleAdapter(List<Article> mArticleList) {
        this.mArticleList = mArticleList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==ARTICLE_VIEW){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_recommend, parent, false);
            return new ArticleViewHolder(view);
        }else if (viewType==PROGRESS_VIEW){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress,parent,false);
            return new ProgressViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            Article article = mArticleList.get(position);
            Log.d(TAG, "articleTitle:" + article.getTitle());
            ((ArticleViewHolder) holder).txt_title.setText(article.getTitle());
            ((ArticleViewHolder) holder).txt_content.setText(article.getContent());
            Glide.with(getContext()).load(article.getArticleCover().getFileUrl()).into(((ArticleViewHolder) holder).img_cover);

            if (onRecyclerViewListener != null) {
                ((ArticleViewHolder) holder).rl_article.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(view, position);
                    }
                });
            }
        }else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder = (ProgressViewHolder)holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }
}
