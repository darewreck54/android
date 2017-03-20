package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.Multimedium;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by darewreck_PC on 3/18/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.ivThumbnail) ImageView ivThumbnail;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSnippet) TextView tvSnippet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    private List<Doc> mArticles;
    private Context mContext;
    public ArticlesAdapter(Context context, List<Doc> articles) {
        mArticles = articles;
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Doc article = mArticles.get(position);
        viewHolder.tvTitle.setText(article.getHeadline().getMain().toString());
        if(!TextUtils.isEmpty(article.getSnippet())) {
            viewHolder.tvSnippet.setText(article.getSnippet().toString());
        }
        else{
            viewHolder.tvSnippet.setText("");
        }


        if(article.getMultimedia().size() > 0) {
            for(Multimedium multimedium:article.getMultimedia()) {
                if(multimedium.getSubtype().equals("thumbnail")){
                    String thumbnail = "http://www.nytimes.com/" + multimedium.getUrl();
                    if(!TextUtils.isEmpty(thumbnail)) {
                        //Picasso.with(getContext()).load(thumbnail).into(viewHolder.ivThumbnail);
                        Glide.with(getContext()).load(thumbnail).centerCrop().error(R.drawable.ic_thumbnial_placeholder).placeholder(R.drawable.ic_thumbnial_placeholder).into(viewHolder.ivThumbnail);
                    }
                }
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
