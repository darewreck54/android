package com.codepath.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;

import org.parceler.Parcels;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie =  (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));


        TextView tv_title = (TextView)  findViewById(R.id.tv_title);
        TextView tv_overview = (TextView)  findViewById(R.id.tv_overview);
        TextView tv_popularityScore = (TextView)  findViewById(R.id.tv_popularityScore);
        RatingBar rb_starRate   = (RatingBar) findViewById(R.id.rb_starRating);
        rb_starRate.setIsIndicator(true);
        rb_starRate.setRating( movie.getVoteAverage()/2);
        tv_title.setText(movie.getTitle());
        tv_overview.setText(movie.getOverview());
        tv_popularityScore.setText(String.valueOf(movie.getPopularity()));

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
