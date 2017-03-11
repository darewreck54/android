package com.codepath.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.flicks.R;
import com.codepath.flicks.adapters.MovieAdapter;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.network.MovieDbClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private MovieDbClient client;
    private ListView lvMovies;
    private MovieAdapter adapterMovies;
    private final int DETAIL_MOVIE_REQUEST_CODE = 30;
    private ArrayList<Movie> aMovies;
    private final String TAG = MainActivity.class.getName();
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
        client.getBoxOfficeMovies(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve movies. Please try again!", Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);

                        JSONArray items = json.getJSONArray("results");
                        final ArrayList<Movie> movies = Movie.fromJsonArray(items);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Movie movie : movies) {
                                    adapterMovies.add(movie); // add movie through the adapter
                                }
                                adapterMovies.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        Log.e(TAG, "Error: " + response.message() + " " + response.body().toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }
}
