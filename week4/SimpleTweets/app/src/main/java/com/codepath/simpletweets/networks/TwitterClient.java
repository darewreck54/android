package com.codepath.simpletweets.networks;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.databinding.tool.util.StringUtils;
import android.text.TextUtils;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

/*
 *
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 *
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL

 //   public static final String REST_CONSUMER_KEY = "XMPm004Fh3Ldv3PWHqCBXDw1O";       // Change this
   // public static final String REST_CONSUMER_SECRET = "9y3e6CFk8nTB3EyqpCuRhpyxD4jfMMdqvE5PR5MnG2v7dsDKlJ"; // Change this

    public static final String REST_CONSUMER_KEY = "o6BMCvHERsHmFFdXc3LkRhfj8";       // Change this
    public static final String REST_CONSUMER_SECRET = "ZFCQnkZnGtZw5TaxCevy2H578tJaOaHGO0T70cfcFBO9R8RT8R"; // Change this


    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    /*
    public void createFavorite() {
        String apiUrl = getApiUrl("favorites/create.json");
    }
    public void retweet(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/retweet/{id}.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("status", message);
        params.put("in_reply_to_status_id", message);
        client.post(apiUrl, params, handler);
    }
    */


    public void updateStatus(String message, Long replyStatusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("status", message);
        if(replyStatusId != null) {
            params.put("in_reply_to_status_id", replyStatusId);
        }
        client.post(apiUrl, params, handler);
    }
     public void getHomeTimeline(Long sinceId, Long maxid, long count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", count);
        if(sinceId != null) {
            params.put("since_id", sinceId);
        }
        if(maxid != null) {
            params.put("max_id", maxid);
        }
        client.get(apiUrl, params, handler);
    }
    public void getMentionTimeline(Long sinceId, Long maxid, long count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", count);

        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, Long sinceId, Long maxid, long count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", count);
        params.put("screen_name", screenName);
        if(sinceId != null) {
            params.put("since_id", sinceId);
        }
        if(maxid != null) {
            params.put("max_id", maxid);
        }
        client.get(apiUrl, params, handler);
    }
    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        // Can specify query string params directly or through RequestParams.
        client.get(apiUrl, null, handler);
    }

    public void retweet(Long retweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/"+ retweetId+".json");
        RequestParams params = new RequestParams();
        params.put("format", "json");

        client.post(apiUrl, params, handler);
    }

    public void reply(int replyToStatusId, String status, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("in_reply_to_status_id", replyToStatusId);
        params.put("status", status);
        client.post(apiUrl, params, handler);
    }

    public void saveFavorite( long userId,  AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("id", userId);
        client.post(apiUrl, params, handler);
    }
    public void unsaveFavorite( long userId,  AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("id", userId);
        client.post(apiUrl, params, handler);
    }

    public void getFollowers(long userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/ids.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("user_id", userId);
        client.get(apiUrl, params, handler);
    }

    public void lookupUser(List<String> userIds, AsyncHttpResponseHandler handler ) {
        String apiUrl = getApiUrl("users/lookup.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        String stringUserIds =  TextUtils.join(",", userIds);
        params.put("user_id",stringUserIds);
        client.get(apiUrl, params, handler);
    }

    public void getFavorites(long userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/ids.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("user_id", userId);
        client.get(apiUrl, params, handler);
    }
}
