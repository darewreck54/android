package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

/**
 * Created by darewreck_PC on 3/18/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_DETAIL_LAYOUT1 = 0;
    private final int ITEM_DETAIL_LAYOUT2 = 1;

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

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSnippet) TextView tvSnippet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder2(View itemView) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType) {
            case ITEM_DETAIL_LAYOUT1: {
                View v1 = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolder(v1);
                break;
            }
            case ITEM_DETAIL_LAYOUT2: {
                View v2 = inflater.inflate(R.layout.item_article_result2, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            }
            default:
            {
               Log.d(TAG,"This should never happen");
            }
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Doc article = mArticles.get(position);

        switch(viewHolder.getItemViewType()) {
            case ITEM_DETAIL_LAYOUT1: {
                ViewHolder vh1 = (ViewHolder)viewHolder;
                // Get the data model based on position
                vh1.tvTitle.setText(article.getHeadline().getMain().toString());
                if(!TextUtils.isEmpty(article.getSnippet())) {
                    vh1.tvSnippet.setText(article.getSnippet().toString());
                }
                else{
                    vh1.tvSnippet.setText("");
                }

                for(Multimedium multimedium:article.getMultimedia()) {
                    if(multimedium.getSubtype() != null && multimedium.getSubtype().equals("thumbnail")){
                        String thumbnail = "http://www.nytimes.com/" + multimedium.getUrl();
                        if(!TextUtils.isEmpty(thumbnail)) {
                            //Picasso.with(getContext()).load(thumbnail).into(viewHolder.ivThumbnail);
                            Glide.with(getContext()).load(thumbnail).override(200,200).error(R.drawable.ic_thumbnial_placeholder).placeholder(R.drawable.ic_thumbnial_placeholder).into(vh1.ivThumbnail);
                        }
                    }
                }
                break;
            }
            case ITEM_DETAIL_LAYOUT2: {
                ViewHolder2 vh2 = (ViewHolder2)viewHolder;

                // Get the data model based on position
                vh2.tvTitle.setText(article.getHeadline().getMain().toString());
                if(!TextUtils.isEmpty(article.getSnippet())) {
                    vh2.tvSnippet.setText(article.getSnippet().toString());
                }
                else{
                    vh2.tvSnippet.setText("");
                }
                break;
            }
            default:{
                break;
            }
        }

    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        View selectedView = null;

        if (ITEM_DETAIL_LAYOUT1 == type) {
            selectedView =  LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
        } else if (ITEM_DETAIL_LAYOUT2 == type) {
            selectedView =  LayoutInflater.from(getContext()).inflate(R.layout.item_article_result2, parent, false);
        }
        else {
            Log.e(TAG, "Layout Type is Invalid");
        }

        return selectedView;
    }

    @Override
    public int getItemViewType(int position) {
        Doc article = this.mArticles.get(position);

        if(article.getMultimedia().size() > 0) {
            for(Multimedium multimedium:article.getMultimedia()) {
                if(multimedium.getSubtype() != null && multimedium.getSubtype().equals("thumbnail")){
                    String thumbnail = "http://www.nytimes.com/" + multimedium.getUrl();
                    if(!TextUtils.isEmpty(thumbnail)) {
                        return ITEM_DETAIL_LAYOUT1;
                    }
                }
            }
        }

        return ITEM_DETAIL_LAYOUT2;
    }
}
