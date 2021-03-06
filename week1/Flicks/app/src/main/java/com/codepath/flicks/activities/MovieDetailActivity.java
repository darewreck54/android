package com.codepath.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.adapters.MovieAdapterLayoutType;
import com.codepath.flicks.interfaces.IMovieClient;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.models.Trailer;
import com.codepath.flicks.network.MovieDbClient;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_popularityScore) TextView tv_popularityScore;
    @BindView(R.id.rb_starRating) RatingBar rb_starRate;
   // @BindView(R.id.player) YouTubePlayerView youTubePlayerView;
    private static IMovieClient client = new MovieDbClient();
    private final String YOUTUBE_API_KEY = "AIzaSyC_lcsmYGbxitZgmc9vpm32dwHVu4_lxOQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Movie movie =  (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        int layoutType = getIntent().getIntExtra("layoutType", -1);

        if(MovieAdapterLayoutType.POPULAR.ordinal() == layoutType) {
            setContentView(R.layout.activity_movie_detail_popular);

            YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                    getFragmentManager().findFragmentById(R.id.youtubeFragment);

            youtubeFragment.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        final YouTubePlayer this_youTubPalyer = youTubePlayer;
                        client.getTrailerForBoxOfficeMovies(String.valueOf(movie.getId()), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "Error in receiving trailer: " + e.getStackTrace());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if(response.isSuccessful()) {
                                    String responseData = response.body().string();
                                    try {
                                        JSONObject json = new JSONObject(responseData);
                                        JSONArray jsonArray = json.getJSONArray("results");
                                        ArrayList<Trailer> trailers = Trailer.fromJsonArray(jsonArray);
                                        if(trailers.size() > 0) {
                                            this_youTubPalyer.loadVideo(trailers.get(0).key);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    Log.d(TAG, "Error in receiving trailer: " + response.message());
                                }
                            }
                        });
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
        }
        else if (MovieAdapterLayoutType.NORMAL.ordinal() == layoutType) {
            setContentView(R.layout.activity_movie_detail);
            ButterKnife.bind(this);

            rb_starRate.setIsIndicator(true);
            rb_starRate.setRating( movie.getVoteAverage()/2);
            tv_title.setText(movie.getTitle());
            tv_overview.setText(movie.getOverview());
            tv_popularityScore.setText(String.valueOf(movie.getPopularity()));

            YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                    getFragmentManager().findFragmentById(R.id.youtubeFragment);

            youtubeFragment.initialize(YOUTUBE_API_KEY,
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {

                            final YouTubePlayer this_youTubPalyer = youTubePlayer;
                            client.getTrailerForBoxOfficeMovies(String.valueOf(movie.getId()), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d(TAG, "Error in receiving trailer: " + e.getStackTrace());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    if(response.isSuccessful()) {
                                        String responseData = response.body().string();
                                        try {
                                            JSONObject json = new JSONObject(responseData);
                                            JSONArray jsonArray = json.getJSONArray("results");
                                            ArrayList<Trailer> trailers = Trailer.fromJsonArray(jsonArray);
                                            if(trailers.size() > 0) {
                                                this_youTubPalyer.cueVideo(trailers.get(0).key);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        Log.d(TAG, "Error in receiving trailer: " + response.message());
                                    }
                                }
                            });
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });

        }

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.back_arrow);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
