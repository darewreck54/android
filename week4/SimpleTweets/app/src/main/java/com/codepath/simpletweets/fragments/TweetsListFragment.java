package com.codepath.simpletweets.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.activities.ProfileActivity;
import com.codepath.simpletweets.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.simpletweets.adapters.ItemClickSupport;
import com.codepath.simpletweets.adapters.TwitterRecycleAdapter;
import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.NetworkConnectionUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by darewreck_PC on 3/31/2017.
 */

public class TweetsListFragment extends Fragment {
    @BindView(R.id.rvTweets)
    protected RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected TwitterRecycleAdapter adapter;
    protected List<Tweet> tweets;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected LinearLayoutManager layoutManager;
    protected Long minTweetId = Long.MAX_VALUE;
    private static final long MAX_TWEET_COUNT = 10;
    protected ProgressDialog pd;
    protected TwitterClient twitterClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            tweets = new ArrayList<>();
            adapter = new TwitterRecycleAdapter(getActivity(), tweets);
            //layoutManager.setStackFromEnd(true);

            twitterClient = new TwitterClient(getActivity());
            pd = new ProgressDialog(getContext());
            pd.setTitle("Loading...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
        }
    }

    protected void populateList(Long since_id, Long max_id, Long count, final boolean isRefresh) {

    }
    public void init(){
        if(!NetworkConnectionUtil.isInternetAvailable(getActivity())) {
            ArrayList<Tweet> tweetsR = (ArrayList<Tweet>) TweetDbFlowAdapter.queryTweets();
            for(int i=tweetsR.size()-1; i>=0; i--){
                Tweet tweet = tweetsR.get(i);
                TweetDbFlowAdapter.addTweet(tweet);
                tweets.add(0, tweet);
                adapter.notifyItemChanged(0);
                minTweetId = Math.min(minTweetId, tweet.id);
            }
        } else {
            this.populateList(null,null,MAX_TWEET_COUNT,true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, v);


        if(savedInstanceState == null) {

            rvTweets.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            rvTweets.setLayoutManager(layoutManager);
            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    //    populateTimelineAsync(null,minTweetId-1,MAX_TWEET_COUNT,false);
                }
            };
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    //  populateTimelineAsync(null,null,MAX_TWEET_COUNT, true);
                    // populateList(null,null,MAX_TWEET_COUNT,true);
                }
            });

            // Adds the scroll listener to RecyclerView
            rvTweets.addOnScrollListener(scrollListener);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            //getApplicationContext().deleteDatabase(SimpleTweetsDb.NAME);

            //init();

        /*
        boolean isFromShareIntent = getIntent().getBooleanExtra("fromReceiveIntent", false);
        if(isFromShareIntent) {
            onComposeTweetClick();
        }
        */

            ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                    new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            Tweet tweet = tweets.get(position);
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            intent.putExtra("user", Parcels.wrap(tweet.user));
                            startActivity(intent);
                        }
                    }
            );

            init();
        }


        return v;
    }

    public void addAll(List<Tweet> tweetsR, boolean isRefresh) {
        if(isRefresh) {
            tweets.clear();
            adapter.clear();
        }
        for(Tweet tweet:tweetsR){
            TweetDbFlowAdapter.addTweet(tweet);
            tweets.add(tweet);
            adapter.notifyItemChanged(tweets.size()-1);
            minTweetId = Math.min(minTweetId, tweet.id);
        }
        if(isRefresh) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void refreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }


}
