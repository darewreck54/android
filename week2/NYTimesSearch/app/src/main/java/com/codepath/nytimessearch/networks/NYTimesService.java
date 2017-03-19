package com.codepath.nytimessearch.networks;

/**
 * Created by darewreck_PC on 3/17/2017.
 */

public class NYTimesService {
    private static final String BASE_URL = "https://api.nytimes.com/";
    private static final String SEARCH_URL = BASE_URL + "svc/search/v2/articlesearch.json";

    public static String getBaseUrl(){
        return BASE_URL;
    }

}
