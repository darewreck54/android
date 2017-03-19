package com.codepath.nytimessearch.networks;

import com.codepath.nytimessearch.models.NYTimesArticleSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by darewreck_PC on 3/17/2017.
 */

public interface NYTimesSearchInterface {
    @GET("svc/search/v2/articlesearch.json")
    Call<NYTimesArticleSearchResponse> getArticles(@Query("q") String searchString,
                                                   @Query("page") int page,
                                                   @Query("fq") String filter,
                                                   @Query("begin_date") String beginDate,
                                                   @Query("sort") String sort);
}
