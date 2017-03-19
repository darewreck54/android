package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.Multimedium;
import com.codepath.nytimessearch.models.NYTimesArticleSearchResponse;
import com.codepath.nytimessearch.networks.NYTimesService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by darewreck_PC on 3/18/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Doc> {

    private ViewHolder viewHolder;
    public static class ViewHolder {
        @BindView(R.id.ivThumbnail) ImageView ivThumbnail;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSnippet) TextView tvSnippet;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    public ArticleArrayAdapter(Context context, List<Doc> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Doc article = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(article.getHeadline().getMain().toString());
        viewHolder.tvSnippet.setText(article.getSnippet().toString());


        if(article.getMultimedia().size() > 0) {
            for(Multimedium multimedium:article.getMultimedia()) {
                if(multimedium.getSubtype().equals("thumbnail")){
                    String thumbnail = "http://www.nytimes.com/" + multimedium.getUrl();
                    if(!TextUtils.isEmpty(thumbnail)) {
                        Picasso.with(getContext()).load(thumbnail).into(viewHolder.ivThumbnail);
                    }
                }
            }
        }

        return convertView;
    }
}
