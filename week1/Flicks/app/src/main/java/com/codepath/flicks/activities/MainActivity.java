package com.codepath.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flicks.R;
import com.codepath.flicks.adapters.MovieAdapter;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.network.MovieDbClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private MovieDbClient client;
    private ListView lvMovies;
    private MovieAdapter adapterMovies;
    private final int DETAIL_MOVIE_REQUEST_CODE = 30;
    private ArrayList<Movie> aMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        aMovies = new ArrayList<Movie>();
        adapterMovies = new MovieAdapter(this, aMovies);
        lvMovies.setAdapter(adapterMovies);

        fetchBoxOfficeMovies();
        this.lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movie = (Movie) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                intent.putExtra("movieIndex", position);
                startActivityForResult (intent, DETAIL_MOVIE_REQUEST_CODE);
            }
        });

    }

    private void fetchBoxOfficeMovies() {
        client = new MovieDbClient();
        client.getBoxOfficeMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("results");
                    final ArrayList<Movie> movies = Movie.fromJsonArray(items);


                    for (Movie movie : movies) {


                        adapterMovies.add(movie); // add movie through the adapter
                    }
                    adapterMovies.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
