package com.codepath.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.adapters.ArticlesAdapter;
import com.codepath.nytimessearch.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.adapters.ItemClickSupport;
import com.codepath.nytimessearch.fragments.FilterDialogueFragment;
import com.codepath.nytimessearch.interfaces.EndlessScrollListener;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.NYTimesArticleSearchResponse;
import com.codepath.nytimessearch.networks.NYTimesSearchInterface;
import com.codepath.nytimessearch.networks.NYTimesService;
import com.codepath.nytimessearch.networks.NYTimesRequestInterceptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.etQuery) EditText etQuery;
//    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rvResults) RecyclerView rvResults;

    private OkHttpClient client;
    private NYTimesSearchInterface service;
 //   private ArticleArrayAdapter adapter;
    private ArticlesAdapter adapter;
    private List<Doc> articles;
    private EndlessRecyclerViewScrollListener scrollListener;
    private class QuerySearchParams {
        public String filter;
        public String sortBy;
        public String convertedDate;
        public String query;
        public QuerySearchParams(String query, String filter, String convertedDate, String sortBy) {
            this.query = query;
            this.filter = filter;
            this.convertedDate = convertedDate;
            this.sortBy = sortBy;

        }
    }

    private QuerySearchParams queryParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new NYTimesRequestInterceptor());

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.build();

        this.client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NYTimesService.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NYTimesSearchInterface.class);
        articles = new ArrayList<>();
        //adapter = new ArticleArrayAdapter(this,articles);

        //gvResults.setAdapter(adapter);

        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        articles = new ArrayList<>();
        adapter = new ArticlesAdapter(this,articles);
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayout = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayout);

        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                        Doc article = articles.get(position);
                        intent.putExtra("web_url", article.getWebUrl());
                        startActivity(intent);
                    }
                }
        );
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                searchArticles(queryParams,page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvResults.addOnScrollListener(scrollListener);
/*
        gridLayout.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                searchArticles(queryParams,page);
                return false;
            }
        });
*/
    }

    private void searchArticles(QuerySearchParams queryParms, int page){
        Call<NYTimesArticleSearchResponse> call = service.getArticles(queryParms.query,page, queryParms.filter,queryParms.convertedDate,queryParms.sortBy);
        call.enqueue(new Callback<NYTimesArticleSearchResponse>() {
            @Override
            public void onResponse(Call<NYTimesArticleSearchResponse> call, Response<NYTimesArticleSearchResponse> response) {
                if(response.isSuccessful()) {
                   // articles.clear();
                    // 2. Notify the adapter of the update
                 //   adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                // 3. Reset endless scroll listener when performing a new search
                  //  scrollListener.resetState();

                    NYTimesArticleSearchResponse nytimesResponse = response.body();
                    //adapter.clear();
                    for(Doc article: nytimesResponse.getResponse().getDocs()) {
                        articles.add(0,article);
                        adapter.notifyItemChanged(0);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Request to retrieve articles failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NYTimesArticleSearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Run the above code block on the main thread after 2 seconds
        handler.postDelayed(runnableCode, 2000);
    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter: {
                showFilterDialog();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogueFragment fragment = FilterDialogueFragment.newInstance();
        fragment.show(fm, "fragment_filter");
    }


    @OnClick(R.id.btSearch)
    public void onArticleSearch(View view) {
        String query = this.etQuery.getText().toString();
        query = TextUtils.isEmpty(query) ? null:query;

        SharedPreferences settings = this.getSharedPreferences("filterSetting", Context.MODE_PRIVATE);
        ArrayList<String> desks = new ArrayList<>();

        String filter = null;

        if(settings.getBoolean("Arts", false)) {
            desks.add("Arts");
        }

        if(settings.getBoolean("FashionStyle", false)) {
            desks.add("Fashion & Style");
        }

        if(settings.getBoolean("Sports", false)) {
            desks.add("Sports");
        }

        for(int i = 0; i < desks.size(); i++) {
            if(i == 0) {
                filter ="";
            }
            if(i == desks.size()-1) {
                filter += desks.get(i);
            }
            else
            {
                filter += desks.get(i) + ",";
            }
        }

        filter = "news_desk:(" + filter + ")";
        int sortByPosition = (int)settings.getLong("SortBy", 0);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(c.getTime());

        String beginDate = settings.getString("BeginDate",date);
        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();

        String sortBy = getResources().getStringArray(R.array.sort_array)[sortByPosition];
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String convertedDate = df.format(new Date(beginDate));

        queryParams = new QuerySearchParams(query, filter, convertedDate, sortBy);

        articles.clear();
        // 2. Notify the adapter of the update
        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();
        searchArticles(queryParams,0);
    }
    // Create the Handler object (on the main thread by default)
    private Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
       //     Log.d("Handlers", "Called on main thread");
        }
    };

}
