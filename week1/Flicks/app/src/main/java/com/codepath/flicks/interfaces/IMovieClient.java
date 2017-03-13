package com.codepath.flicks.interfaces;

import okhttp3.Callback;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public interface IMovieClient {
    void getBoxOfficeMovies(Callback handler);
    void getTrailerForBoxOfficeMovies(String movidId, Callback handler);
}
