package com.mils.whisper.article.articlelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.bean.Article;

import java.util.List;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/7/27.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Article> mArticleList;
    private String TAG = "ArticleAdapter";

    private final static int HEAD_COUNT = 0;
    private final static int FOOT_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;

    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener mOnItemClickListener){
        this.onRecyclerViewListener=mOnItemClickListener;
    }

    public int getContentSize(){
        return mArticleList.size();
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

    class ArticleViewHolder extends RecyclerView.ViewHolder{
        TextView articleTitle;
        TextView articleContent;
        TextView articleCreation;
        ImageView articleCover;
        RelativeLayout rl_article;

        public ArticleViewHolder(View view){
            super(view);
            articleTitle = (TextView)view.findViewById(R.id.txt_articles_title);
            articleContent = (TextView)view.findViewById(R.id.txt_articles_content);
            articleCover = (ImageView)view.findViewById(R.id.img_articles_cover);
            articleCreation = (TextView)view.findViewById(R.id.txt_articles_creation);
            rl_article = (RelativeLayout)view.findViewById(R.id.rl_article);
        }
    }

    class EndViewHolder extends RecyclerView.ViewHolder{
        TextView txt_end;
        public EndViewHolder(View view){
            super(view);
            txt_end = (TextView)view.findViewById(R.id.txt_end);
        }
    }

    public ArticleAdapter(List<Article> articleList){
        mArticleList = articleList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
        if (viewType==TYPE_CONTENT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_article, parent, false);
            return new ArticleViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_end, parent, false);
            return new EndViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder){
            Article article = mArticleList.get(position);
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            articleViewHolder.articleTitle.setText(article.getTitle());
            articleViewHolder.articleContent.setText(article.getContent());
            articleViewHolder.articleCreation.setText("创建于"+article.getCreatedAt());
            Glide.with(getContext()).load(article.getArticleCover().getFileUrl()).into(articleViewHolder.articleCover);

            if(onRecyclerViewListener!=null){
                ((ArticleViewHolder) holder).rl_article.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((ArticleViewHolder) holder).rl_article,position);
                    }
                });
            }
        }else if (holder instanceof EndViewHolder){
            if (null==mArticleList||mArticleList.size()==0){
                ((EndViewHolder) holder).txt_end.setText(getContext().getResources().getString(R.string.empty));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size()+FOOT_COUNT;
    }
}
