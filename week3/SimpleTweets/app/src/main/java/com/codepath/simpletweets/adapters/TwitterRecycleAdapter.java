package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.utils.ParseRelativeDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

public class TwitterRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Tweet> mTweets;
    private Context mContext;
    private LayoutInflater inflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName) TextView tvUserName;
        @BindView(R.id.tvScreenName) TextView tvUserScreenName;

        @BindView(R.id.ivProfileImg)
        ImageView ivProfileIcon;

        @BindView(R.id.tvText) TextView tvMessage;

        @BindView(R.id.tvRelativeTime)
        TextView tvRelativeTime;

        String relativeTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public TwitterRecycleAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_tweet, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.tvMessage.setText(tweet.text);
        viewHolder.tvUserName.setText( tweet.user.name);
        viewHolder.tvUserScreenName.setText("@"+tweet.user.screenName);
        viewHolder.tvRelativeTime.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        Glide.with(getContext()).load(tweet.user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(viewHolder.ivProfileIcon);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
    private Context getContext() {
        return mContext;
    }

    // Clean all elements of the recycler
    public void clear() {
        TweetDbFlowAdapter.clearTable();
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
