package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.models.TwitterError;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.ParseRelativeDate;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

public class TwitterRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tweet> mTweets;
    private Context mContext;
    private LayoutInflater inflater;
    private static final String TAG = TwitterRecycleAdapter.class.getName();
 //   private Tweet tweet;
    private static TwitterClient twitterClient;
    private ViewHolder viewHolder;

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

        @BindView(R.id.videoView)
        VideoView videoView;

        @BindView(R.id.ibFav)
        ToggleButton tbFavorite;

        @BindView(R.id.ibReply)
        ImageButton ibReply;

        @BindView(R.id.ibRetweet)
        ImageButton ibRetweet;

  //      @BindView(R.id.video_player)
//        VideoPlayerView videoPlayerView;

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
        twitterClient = TwitterApplication.getRestClient();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        viewHolder = (ViewHolder) holder;
        if(tweet.favorited) {
            viewHolder.tbFavorite.setChecked(true);
        } else {
            viewHolder.tbFavorite.setChecked(false);
        }
        viewHolder.tvMessage.setText(tweet.text);
        viewHolder.tvUserName.setText(tweet.user.name);
        viewHolder.tvUserScreenName.setText("@" + tweet.user.screenName);
        viewHolder.tvRelativeTime.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        Glide.with(getContext()).load(tweet.user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(viewHolder.ivProfileIcon);

        viewHolder.tbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavClick(tweet);
            }
        });

        viewHolder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReplyClick(tweet);
            }
        });

        viewHolder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetweetClick(tweet);
            }
        });


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

               // viewHolder.videoPlayerView.setVisibility(View.VISIBLE);
                //mVideoPlayerManager.playNewVideo(null, viewHolder.videoPlayerView, tweet.extendedEntity.media.videoInfo.variant.url);
                Uri uri =  Uri.parse( tweet.extendedEntity.media.videoInfo.variant.url );
                viewHolder.videoView.setVideoURI(uri);
                viewHolder.videoView.start();
                viewHolder.videoView.setVisibility(View.VISIBLE);
                /*
                viewHolder.mVideoCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoPlayerManager.playNewVideo(null, viewHolder.videoPlayerView, tweet.extendedEntity.media.videoInfo.variant.url);
                    }
                });
            */
                //viewHolder.ivTextImage.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Not support type: " + tweet.entity.media.type);
            }
        } else {
            viewHolder.ivTextImage.setVisibility(View.GONE);
           // viewHolder.videoPlayerView.setVisibility(View.GONE);
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

    public void onReplyClick(Tweet tweet){
        FragmentManager fm =  ((FragmentActivity)mContext).getSupportFragmentManager();
        ComposeTweetDialogFragment fragment = ComposeTweetDialogFragment.newInstance(true, tweet);
        fragment.show(fm, "fragment_compose");
    }

    public void onRetweetClick(Tweet tweet) {

        twitterClient.retweet(tweet.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getContext(), "Tweet retweeted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                //Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                if(statusCode == 403) {
                    TwitterError error = TwitterError.fromJSON(errorResponse);
                    Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onFavClick(Tweet tweet) {
        if(viewHolder.tbFavorite.isChecked()) {
            twitterClient.unsaveFavorite(tweet.id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getContext(), "Unfavourite", Toast.LENGTH_SHORT).show();
                    viewHolder.tbFavorite.setChecked(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    //Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                    if(statusCode == 403) {
                        TwitterError error = TwitterError.fromJSON(errorResponse);
                        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            twitterClient.saveFavorite(tweet.id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getContext(), "favourite", Toast.LENGTH_SHORT).show();
                    viewHolder.tbFavorite.setChecked(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    //Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                    if(statusCode == 403) {
                        TwitterError error = TwitterError.fromJSON(errorResponse);
                        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
