package com.codepath.nytimessearch.networks;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by darewreck_PC on 3/17/2017.
 */

public class NYTimesRequestInterceptor implements Interceptor {
    private static final String API_KEY = "d98ec721e1f9427b9f1230aad3c5053d";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder().addHeader("api-key", API_KEY).build();
        return chain.proceed(newRequest);
    }
}
