package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

public class TwitterRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tweet> mTweets;
    private Context mContext;
    private LayoutInflater inflater;
    private static final String TAG = TwitterRecycleAdapter.class.getName();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvUserName;
        @BindView(R.id.tvScreenName)
        TextView tvUserScreenName;

        @BindView(R.id.ivProfileImg)
        ImageView ivProfileIcon;

        @BindView(R.id.tvText)
        TextView tvMessage;

        @BindView(R.id.tvRelativeTime)
        TextView tvRelativeTime;

        @BindView(R.id.ivTextImage)
        ImageView ivTextImage;

      //  @BindView(R.id.video_player)
        //VideoPlayerView videoPlayerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        final Tweet tweet = mTweets.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvMessage.setText(tweet.text);
        viewHolder.tvUserName.setText(tweet.user.name);
        viewHolder.tvUserScreenName.setText("@" + tweet.user.screenName);
        viewHolder.tvRelativeTime.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        Glide.with(getContext()).load(tweet.user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(viewHolder.ivProfileIcon);

        if (tweet.extendedEntity != null
                && tweet.extendedEntity.media != null
                && tweet.extendedEntity.media.videoInfo != null
                && tweet.extendedEntity.media.videoInfo.variant != null
                && tweet.extendedEntity.media.videoInfo.variant.url != null) {
            if (tweet.extendedEntity.media.type.equals("video")) {
                /*
                viewHolder.videoPlayerView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
                    @Override
                    public void onVideoPreparedMainThread() {
                        // We hide the cover when video is prepared. Playback is about to start
                        viewHolder.mVideoCover.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onVideoStoppedMainThread() {
                        // We show the cover when video is stopped
                        viewHolder.mVideoCover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVideoCompletionMainThread() {
                        // We show the cover when video is completed
                        viewHolder.mVideoCover.setVisibility(View.VISIBLE);
                    }
                });
                */
              //  mVideoPlayerManager.playNewVideo(null, viewHolder.videoPlayerView, tweet.extendedEntity.media.videoInfo.variant.url);
                /*
                viewHolder.mVideoCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoPlayerManager.playNewVideo(null, viewHolder.videoPlayerView, tweet.extendedEntity.media.videoInfo.variant.url);
                    }
                });
            */
            } else {
                Log.d(TAG, "Not support type: " + tweet.entity.media.type);
            }


        } else {
            viewHolder.ivTextImage.setVisibility(View.GONE);
        }

        if (tweet.entity != null && tweet.entity.media != null) {
            if (tweet.entity.media.type.equals("photo")) {
                Glide.with(getContext()).load(tweet.entity.media.mediaUrl).fitCenter()
                        .error(R.drawable.blue_twitter_icon)
                        .placeholder(R.drawable.blue_twitter_icon)
                        .into(viewHolder.ivTextImage);
                viewHolder.ivTextImage.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "Not support type: " + tweet.entity.media.type);
            }


        } else {
            viewHolder.ivTextImage.setVisibility(View.GONE);
        }


    }

    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

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
