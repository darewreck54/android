package com.codepath.simpletweets.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.simpletweets.adapters.ItemClickSupport;
import com.codepath.simpletweets.adapters.UserRecycleAdapter;
import com.codepath.simpletweets.models.User;
import com.codepath.simpletweets.networks.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class UserListActivity extends AppCompatActivity {
    @BindView(R.id.rvUsers)
    protected RecyclerView rvUsers;

    @BindView(R.id.swipeContainer)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    protected UserRecycleAdapter adapter;
    protected List<User> users;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected LinearLayoutManager layoutManager;
    protected Long minTweetId = Long.MAX_VALUE;
    private static final long MAX_TWEET_COUNT = 10;
    protected ProgressDialog pd;
    protected TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.twitter_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            users = new ArrayList<>();
            adapter = new UserRecycleAdapter(this, users);
            final List<String> userIds = getIntent().getStringArrayListExtra("userIds");

            twitterClient = new TwitterClient(this);
            pd = new ProgressDialog(this.getApplicationContext());
            pd.setTitle("Loading...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            rvUsers.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(this);
            rvUsers.setLayoutManager(layoutManager);
            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    //populateList(userIds);
                }
            };
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    //  populateTimelineAsync(null,null,MAX_TWEET_COUNT, true);
                    //populateList(userIds);
                }
            });

            // Adds the scroll listener to RecyclerView
            rvUsers.addOnScrollListener(scrollListener);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            final Activity _actiivty = this;
            ItemClickSupport.addTo(rvUsers).setOnItemClickListener(
                    new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            User user = users.get(position);
                            Intent intent = new Intent(_actiivty, ProfileActivity.class);
                            intent.putExtra("user", Parcels.wrap(user));
                            startActivity(intent);
                        }
                    }
            );
             populateList(userIds);
        }
    }

    protected void populateList(List<String> userIds) {
        twitterClient.lookupUser(userIds, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<User> usersR = User.fromJSONArray(response);
                addAll(usersR);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void addAll(List<User> usersR) {
        for (User user : usersR) {
            users.add(user);
            adapter.notifyItemChanged(users.size() - 1);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
