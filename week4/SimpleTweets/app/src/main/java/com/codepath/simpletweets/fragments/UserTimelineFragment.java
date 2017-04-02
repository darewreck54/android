package com.codepath.simpletweets.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.NetworkConnectionUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UserTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private static final String ARG_PARAM1 = "screenName";
    private String screenName;
    private static final long MAX_TWEET_COUNT = 10;
    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            screenName = getArguments().getString(ARG_PARAM1);
        }
        client = TwitterApplication.getRestClient();
    }

    @Override
    protected void populateList(Long since_id, Long max_id, Long count, final boolean isRefresh) {
        if (NetworkConnectionUtil.isInternetAvailable(getActivity())) {
            client.getUserTimeline(screenName, since_id, max_id, count, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    List<Tweet> tweetsR = Tweet.fromJSONArray(response);
                    addAll(tweetsR, isRefresh);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    refreshComplete();
                    if (statusCode == 429) {
                        Toast.makeText(getContext(), "Rate limit exceeded for the Twitter API.  Please try again in a couple minutes.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Request to retreive Tweets failed.  Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet avaliable.  Please check the connection.", Toast.LENGTH_SHORT).show();
            refreshComplete();
        }
    }
}
