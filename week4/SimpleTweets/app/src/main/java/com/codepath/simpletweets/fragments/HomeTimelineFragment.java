package com.codepath.simpletweets.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.data.TweetDbFlowAdapter;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.models.TwitterError;
import com.codepath.simpletweets.networks.TwitterClient;
import com.codepath.simpletweets.utils.NetworkConnectionUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void populateList(Long since_id, Long max_id, Long count, final boolean isRefresh) {
        if(NetworkConnectionUtil.isInternetAvailable(getActivity())) {
            pd.show();
            twitterClient.getHomeTimeline(since_id, max_id, count, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    pd.dismiss();
                    List<Tweet> tweetsR = Tweet.fromJSONArray(response);
                    addAll(tweetsR,isRefresh);

                    pd.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    refreshComplete();
                    if(statusCode == 429) {
                        //Toast.makeText(getContext(),"Rate limit exceeded for the Twitter API.  Please try again in a couple minutes.", Toast.LENGTH_SHORT).show();
                        TwitterError error = TwitterError.fromJSON(errorResponse);
                        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
                    } else   if(statusCode == 403) {
                        TwitterError error = TwitterError.fromJSON(errorResponse);
                        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
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
