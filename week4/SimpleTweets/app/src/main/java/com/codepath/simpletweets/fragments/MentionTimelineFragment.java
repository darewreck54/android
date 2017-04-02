package com.codepath.simpletweets.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.NetworkConnectionUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class MentionTimelineFragment extends TweetsListFragment {
    private TwitterClient twitterClient;
    private static final long MAX_TWEET_COUNT = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = new TwitterClient(getActivity());
    }

    @Override
    protected void populateList(Long since_id, Long max_id, Long count, final boolean isRefresh) {
        if(NetworkConnectionUtil.isInternetAvailable(getActivity())) {
            twitterClient.getMentionTimeline(since_id, max_id, count, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    List<Tweet> tweetsR = Tweet.fromJSONArray(response);
                    addAll(tweetsR,isRefresh);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    refreshComplete();
                    if(statusCode == 429) {
                        Toast.makeText(getContext(),"Rate limit exceeded for the Twitter API.  Please try again in a couple minutes.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(),"Request to retreive Tweets failed.  Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet avaliable.  Please check the connection.", Toast.LENGTH_SHORT ).show();
            refreshComplete();
        }
    }
}
