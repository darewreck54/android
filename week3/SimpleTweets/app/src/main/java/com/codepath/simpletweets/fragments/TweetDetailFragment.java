package com.codepath.simpletweets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.activities.LoginActivity;
import com.codepath.simpletweets.activities.TwitterActivity;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.ParseRelativeDate;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TweetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TweetDetailFragment extends DialogFragment {
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    @BindView(R.id.tvScreenName)
    TextView tvScreenName;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvText)
    TextView tvText;

    @BindView(R.id.tvCreationTime)
    TextView tvCreationTime;

    @BindView(R.id.tvRetweetCount)
    TextView tvRetweetCount;

    @BindView(R.id.tvLikes)
    TextView tvLikes;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivTextImage)
    ImageView ivTextImage;

    private TwitterClient twitterClient;

    private Tweet tweet;

    public TweetDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TweetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TweetDetailFragment newInstance(Tweet tweet) {
        TweetDetailFragment fragment = new TweetDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", Parcels.wrap(tweet));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = new TwitterClient(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tweet = (Tweet) Parcels.unwrap( getArguments().getParcelable("tweet"));
        View view =  inflater.inflate(R.layout.fragment_tweet_detail_dialog, container, false);
        ButterKnife.bind(this, view);

        ((TwitterActivity) getActivity()).setSupportActionBar(toolbar);
        ((TwitterActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TwitterActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TwitterActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_back);
        setHasOptionsMenu(true);

        tvScreenName.setText("@"+tweet.user.screenName);
        tvName.setText(tweet.user.name);
        tvText.setText(tweet.text);
        tvRetweetCount.setText(String.valueOf(tweet.retweetCount) + " RETWEETS");
        tvLikes.setText(String.valueOf(tweet.favoriteCount) + " LIKES");
        tvCreationTime.setText(ParseRelativeDate.getDisplayTime(tweet.createdAt));
        Glide.with(getContext()).load(tweet.user.profileImageUrl).fitCenter()
                .error(R.drawable.blue_twitter_icon)
                .placeholder(R.drawable.blue_twitter_icon)
                .into(ivProfilePic);

        if(tweet.entity != null && tweet.entity.media != null && tweet.entity.media != null) {

            Glide.with(getContext()).load( tweet.entity.media.mediaUrl).fitCenter()
                    .error(R.drawable.blue_twitter_icon)
                    .placeholder(R.drawable.blue_twitter_icon)
                    .into(ivTextImage);

            ivTextImage.setVisibility(View.VISIBLE);
        } else {
            ivTextImage.setVisibility(View.GONE);
        }

        return view;
    }

    @OnClick(R.id.tvReply)
    public void onReplyClick(){

    }

    @OnClick(R.id.tvRetweet)
    public void onRetweetClick() {
        twitterClient.retweet(tweet.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getContext(), "Tweet retweeted!", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(getContext(), "Twitter UpdateStatus Failed!", Toast.LENGTH_SHORT).show();
                if(statusCode == 403) {
                    Toast.makeText(getContext(), "Cannot tweet the same status.  Please type something new.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @OnClick(R.id.tvFav)
    public void onFavClick() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                dismiss();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
