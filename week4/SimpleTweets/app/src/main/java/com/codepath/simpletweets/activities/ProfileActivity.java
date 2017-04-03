package com.codepath.simpletweets.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.adapters.ProfileFragmentPagerAdapter;
import com.codepath.simpletweets.adapters.TwitterFragmentPagerAdapter;
import com.codepath.simpletweets.fragments.UserTimelineFragment;
import com.codepath.simpletweets.models.User;
import com.codepath.simpletweets.networks.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient twitterClient;
    private User user;

    @BindView(R.id.ivProfileImg)
    ImageView ivProfileImg;

    @BindView(R.id.tvTagline)
    TextView tvTagline;

    @BindView(R.id.tvScreenName)
    TextView tvScreenName;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvFollowsCount)
    TextView tvFollowsCount;

    @BindView(R.id.tvFollowingsCount)
    TextView tvFollowingsCount;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivBackgroundImg)
    ImageView ivBackgroundImg;

    @BindView(R.id.pstProfile)
    PagerSlidingTabStrip tabsStrip;

    @BindView(R.id.vpProfile)
    ViewPager vpPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.twitter_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        twitterClient = TwitterApplication.getRestClient();

        if(getIntent().getParcelableExtra("user") == null) {
            twitterClient.getUserInfo( new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
            populateProfileHeader(user);
        }

        String screenName = (user == null) ? getIntent().getStringExtra("screen_name"):user.screenName;
        if(savedInstanceState == null) {
            /*
            UserTimelineFragment fragmentTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentTimeline);
            ft.commit();
            */
            ProfileFragmentPagerAdapter pageAdapter = new ProfileFragmentPagerAdapter(screenName, getSupportFragmentManager(),getApplicationContext());
            vpPager.setAdapter(pageAdapter);
            tabsStrip.setViewPager(vpPager);
        }

    }

    @OnClick(R.id.tvFollowsCount)
    public void getFollowesCount() {
        final Activity _activity = this;
        twitterClient.getFollowers( user.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray idJsonArray = response.getJSONArray("ids");
                    ArrayList<String> ids = new ArrayList<>();
                    for(int i = 0; i < idJsonArray.length(); i++) {
                        ids.add(String.valueOf(idJsonArray.getLong(i)));
                    }

                    Intent intent = new Intent(_activity, UserListActivity.class);
                    intent.putStringArrayListExtra("userIds", ids);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }



    @OnClick(R.id.tvFollowingsCount)
    public void getFollowingsCount() {
        final Activity _activity = this;
        twitterClient.getFriends( user.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray idJsonArray = response.getJSONArray("ids");
                    ArrayList<String> ids = new ArrayList<>();
                    for(int i = 0; i < idJsonArray.length(); i++) {
                        ids.add(String.valueOf(idJsonArray.getLong(i)));
                    }

                    Intent intent = new Intent(_activity, UserListActivity.class);
                    intent.putStringArrayListExtra("userIds", ids);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void populateProfileHeader(User user) {
        this.tvName.setText(user.name.toString());
        this.tvScreenName.setText("@" + user.screenName.toString());
        this.tvFollowingsCount.setText(user.friendsCount + " FOLLOWING");
        this.tvFollowsCount.setText(user.followersCount + " FOLLOWERS");
        this.tvTagline.setText(user.description.toString());
        Glide.with(getApplicationContext()).load(user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(ivProfileImg);
        Glide.with(getApplicationContext()).load(user.profileBackgroundImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(ivBackgroundImg);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
