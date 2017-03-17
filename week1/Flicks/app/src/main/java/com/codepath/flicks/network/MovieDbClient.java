package com.codepath.flicks.network;

import com.codepath.flicks.interfaces.IMovieClient;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by darewreck_PC on 3/8/2017.
 */

public class MovieDbClient implements IMovieClient {
    private final String TAG = MovieDbClient.class.getName();
    private final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String API_BASE_URL ="https://api.themoviedb.org/3/movie/";
    private OkHttpClient client;

    public MovieDbClient() {

        client = new OkHttpClient();
    }

    @Override
    public void getBoxOfficeMovies(Callback handler) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(getApiUrl("now_playing")).newBuilder();
        urlBuilder.addQueryParameter("api_key", this.API_KEY);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(handler);
    }

    @Override
    public void getTrailerForBoxOfficeMovies(String id, Callback handler) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getApiUrl(id + "/videos")).newBuilder();
        urlBuilder.addQueryParameter("api_key", this.API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(handler);

    }


    /* Setters/Getters */

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
}
