package com.codepath.flicks.network;

import android.util.Log;

import com.codepath.flicks.interfaces.IMovieClient;
import com.codepath.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public class MovieDbClient implements IMovieClient {
    private final String TAG = MovieDbClient.class.getName();
    private final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String API_BASE_URL ="https://api.themoviedb.org/3/movie/";
    private AsyncHttpClient client;
    public MovieDbClient() {
        client = new AsyncHttpClient();
    }

    @Override
    public void getBoxOfficeMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl("now_playing");
        RequestParams params = new RequestParams("api_key", this.API_KEY);
        client.get(url, params, handler);
    }

    /* Setters/Getters */

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
}
