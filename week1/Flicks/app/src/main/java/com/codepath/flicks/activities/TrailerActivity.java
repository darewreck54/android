package com.codepath.flicks.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.codepath.flicks.R;
import com.codepath.flicks.interfaces.IMovieClient;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.models.Trailer;
import com.codepath.flicks.network.MovieDbClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by darewreck on 3/12/17.
 */

public class TrailerActivity extends YouTubeBaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_popularityScore) TextView tv_popularityScore;
    @BindView(R.id.rb_starRating)
    RatingBar rb_starRate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static IMovieClient client = new MovieDbClient();
    private final String YOUTUBE_API_KEY = "AIzaSyC_lcsmYGbxitZgmc9vpm32dwHVu4_lxOQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        setContentView(R.layout.activity_movie_detail_popular);
        YouTubePlayerFragment youTubePlayerView = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);

        youTubePlayerView.initialize(YOUTUBE_API_KEY,
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

                                if (response.isSuccessful()) {
                                    String responseData = response.body().string();
                                    try {
                                        JSONObject json = new JSONObject(responseData);
                                        JSONArray jsonArray = json.getJSONArray("results");
                                        ArrayList<Trailer> trailers = Trailer.fromJsonArray(jsonArray);
                                        if (trailers.size() > 0) {
                                            this_youTubPalyer.loadVideo(trailers.get(0).key);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
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


        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */
    }
}
