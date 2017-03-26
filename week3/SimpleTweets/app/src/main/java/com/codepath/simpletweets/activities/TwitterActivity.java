package com.codepath.simpletweets.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.simpletweets.adapters.TwitterRecycleAdapter;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.NetworkConnectionUtil;
import com.google.common.collect.Lists;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TwitterActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogFragmentListener {
    private static final String TAG = TwitterActivity.class.getName();
    private static final long MAX_TWEET_COUNT = 25;
    private TwitterClient twitterClient;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    private TwitterRecycleAdapter adapter;
    private List<Tweet> tweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager layoutManager;
    private Long minTweetId = Long.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.twitter_white);

        tweets = new ArrayList<>();
        adapter = new TwitterRecycleAdapter(this, tweets);

        rvTweets.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvTweets.setLayoutManager(layoutManager);

        twitterClient = new TwitterClient(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimelineAsync(null,null,MAX_TWEET_COUNT, true);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                //populateTimelineAsync(null,minTweetId-1,MAX_TWEET_COUNT,false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        init();

        boolean isFromShareIntent = getIntent().getBooleanExtra("fromReceiveIntent", false);
        if(isFromShareIntent) {
            onComposeTweetClick();
        }

    }

    public void init(){
        if(!NetworkConnectionUtil.isInternetAvailable(this)) {
            ArrayList<Tweet> tweetsR = (ArrayList<Tweet>) TweetDbFlowAdapter.queryTweets();
            for(int i=tweetsR.size()-1; i>=0; i--){
                Tweet tweet = tweetsR.get(i);
                TweetDbFlowAdapter.addTweet(tweet);
                tweets.add(0, tweet);
                adapter.notifyItemChanged(0);
                minTweetId = Math.min(minTweetId, tweet.id);
            }
        } else {
            populateTimelineAsync(null,null,MAX_TWEET_COUNT,true);
        }
    }
    private void populateTimelineAsync(Long since_id, Long max_id, Long count, final boolean isRefresh) {
        if(NetworkConnectionUtil.isInternetAvailable(this)) {
            twitterClient.getTimeline(since_id, max_id, count, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if(isRefresh) {
                        tweets.clear();
                        adapter.clear();
                    }

                    List<Tweet> tweetsR = Tweet.fromJSONArray(response);
                    for(Tweet tweet:tweetsR){
                        TweetDbFlowAdapter.addTweet(tweet);
                        tweets.add(0, tweet);
                        adapter.notifyItemChanged(0);
                        minTweetId = Math.min(minTweetId, tweet.id);
                    }

                    if(isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"Request to retreive Tweets failed.  Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Internet avaliable.  Please check the connection.", Toast.LENGTH_SHORT ).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick(R.id.floatingActionBar)
    public void onComposeTweetClick(){
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment fragment = ComposeTweetDialogFragment.newInstance(null);
        fragment.show(fm, "fragment_compose");
    }

    @Override
    public void onFinishCompose(Tweet tweet) {
        tweets.add(tweets.size()-1, tweet);
        adapter.notifyItemChanged(tweets.size()-1);
        layoutManager.scrollToPosition(tweets.size()-1);
    }
}
