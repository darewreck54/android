package com.codepath.flicks.interfaces;

import com.codepath.flicks.models.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public interface IMovieClient {
    void getBoxOfficeMovies(JsonHttpResponseHandler handler);
}
