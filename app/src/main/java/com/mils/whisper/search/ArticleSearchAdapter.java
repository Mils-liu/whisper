package com.mils.whisper.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mils.whisper.R;
import com.mils.whisper.article.articlelist.ArticleAdapter;
import com.mils.whisper.bean.Article;

import java.util.List;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/11/29.
 */

public class ArticleSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Article> mArticleList;
    private String TAG = "ArticleSearchAdapter";

    private ArticleAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener{
        void onItemClick(View view, int position);
    }
    public void setOnRecyclerViewListener(ArticleAdapter.OnRecyclerViewListener mOnItemClickListener){
        this.onRecyclerViewListener=mOnItemClickListener;
    }

    public int getContentSize(){
        return mArticleList.size();
    }

    class ArticleSearchHolder extends RecyclerView.ViewHolder{
        TextView articleCreateAt;
        ImageView articleCover;
        TextView articleTitle;
        TextView articleContent;
        RelativeLayout rl_articleSearch;
        ArticleSearchHolder(View view){
            super(view);
            articleCreateAt = (TextView)view.findViewById(R.id.txt_articleCreation_search);
            articleCover = (ImageView)view.findViewById(R.id.img_articleCover_search);
            articleTitle = (TextView)view.findViewById(R.id.txt_articleTitle_search);
            articleContent = (TextView)view.findViewById(R.id.txt_articleContent_search);
            rl_articleSearch = (RelativeLayout)view.findViewById(R.id.rl_article_search);
        }
    }

    public ArticleSearchAdapter(List<Article> mArticleList){
        this.mArticleList = mArticleList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_articlesearch, parent, false);
            return new ArticleSearchHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleSearchHolder){
            Article article = mArticleList.get(position);
            ArticleSearchHolder articleSearchViewHolder = (ArticleSearchHolder) holder;
            articleSearchViewHolder.articleContent.setText(article.getContent());
            articleSearchViewHolder.articleTitle.setText(article.getTitle());
            articleSearchViewHolder.articleCreateAt.setText("创建于"+article.getCreatedAt());
            Glide.with(getContext()).load(article.getArticleCover().getFileUrl()).into(articleSearchViewHolder.articleCover);

            if(onRecyclerViewListener!=null){
                ((ArticleSearchHolder) holder).rl_articleSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        onRecyclerViewListener.onItemClick(((ArticleSearchHolder) holder).rl_articleSearch,position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

}
