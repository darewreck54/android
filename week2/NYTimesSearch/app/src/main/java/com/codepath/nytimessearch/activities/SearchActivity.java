package com.codepath.nytimessearch.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticlesAdapter;
import com.codepath.nytimessearch.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.adapters.ItemClickSupport;
import com.codepath.nytimessearch.databinding.ActivitySearchBinding;
import com.codepath.nytimessearch.fragments.FilterDialogueFragment;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.NYTimesArticleSearchResponse;
import com.codepath.nytimessearch.networks.NYTimesRequestInterceptor;
import com.codepath.nytimessearch.networks.NYTimesSearchInterface;
import com.codepath.nytimessearch.networks.NYTimesService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private static final String TAG = SearchActivity.class.getName();
    private OkHttpClient client;
    private NYTimesSearchInterface service;
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
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        setSupportActionBar(this.binding.search.toolbar);
        getSupportActionBar().setTitle("The New York Times");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

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


        articles = new ArrayList<>();
        adapter = new ArticlesAdapter(this, articles);

        this.binding.search.rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        this.binding.search.rvResults.setLayoutManager(gridLayout);

        final Activity _activity = this;
        ItemClickSupport.addTo(this.binding.search.rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Doc article = articles.get(position);

                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        // set toolbar color and/or setting custom actions before invoking build()

                        builder.setToolbarColor(ContextCompat.getColor(_activity, R.color.colorPrimary));
                        //builder.addDefaultShareMenuItem();

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_share);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

                        int requestCode = 100;

                        PendingIntent pendingIntent = PendingIntent.getActivity(_activity,
                                requestCode,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                        CustomTabsIntent customTabsIntent = builder.build();
                        // and launch the desired Url with CustomTabsIntent.launchUrl()
                        customTabsIntent.launchUrl(_activity, Uri.parse(article.getWebUrl()));

                    }
                }
        );
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                searchArticles(queryParams, page);
            }
        };
        // Adds the scroll listener to RecyclerView
        this.binding.search.rvResults.addOnScrollListener(scrollListener);
    }

    private void searchArticles(QuerySearchParams queryParms, int page) {
        if(isInternetAvailable(this.getApplicationContext())) {
        Call<NYTimesArticleSearchResponse> call = service.getArticles(queryParms.query, page, queryParms.filter, queryParms.convertedDate, queryParms.sortBy);
        call.enqueue(new Callback<NYTimesArticleSearchResponse>() {
            @Override
            public void onResponse(Call<NYTimesArticleSearchResponse> call, Response<NYTimesArticleSearchResponse> response) {
                if (response.isSuccessful()) {
                    // articles.clear();
                    // 2. Notify the adapter of the update
                    //   adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                    // 3. Reset endless scroll listener when performing a new search
                    //  scrollListener.resetState();

                    NYTimesArticleSearchResponse nytimesResponse = response.body();
                    //adapter.clear();
                    for (Doc article : nytimesResponse.getResponse().getDocs()) {
                        articles.add(0, article);
                        adapter.notifyItemChanged(0);
                    }
                } else {
                    if(response.code() == 429) {
                    //    handler.postDelayed(runnableCode, 500);
                        Toast.makeText(getApplicationContext(),"Please try again in a bit.  Hit NYTime API Limit.",Toast.LENGTH_SHORT).show();
                        // Run the above code block on the main thread after 2 seconds
                        handler.postDelayed(runnableCode, 300);

                    }
                    else {
                        Log.d(TAG,"Request to retrieve articles failed: " + response.message());
                    }
                    //Toast.makeText(getApplicationContext(), "Request to retrieve articles failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Request to retrieve articles failed: " + response.message());
                    //handler.postDelayed(runnableCode, 500);
                }
            }

            @Override
            public void onFailure(Call<NYTimesArticleSearchResponse> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Search Request Failed: " + t.getMessage());
              //  handler.postDelayed(runnableCode, 500);
                // Run the above code block on the main thread after 2 seconds
                handler.postDelayed(runnableCode, 300);

            }
        });
         }
         else {
            Toast.makeText(getApplicationContext(), "Not internet detected.  Please try again", Toast.LENGTH_SHORT).show();
        }


    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private boolean filterChange = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter: {
                showFilterDialog();
                filterChange = true;
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

    private String previousQuery = null;
    public void onArticleSearch(String query) {
        if(previousQuery!=null && previousQuery.equals(query) && !filterChange){

            return;
        }
        previousQuery = query;
        filterChange = false;

        query = TextUtils.isEmpty(query) ? null : query;

        SharedPreferences settings = this.getSharedPreferences("filterSetting", Context.MODE_PRIVATE);
        ArrayList<String> desks = new ArrayList<>();

        String filter = null;

        if (settings.getBoolean("Arts", false)) {
            desks.add("Arts");
        }

        if (settings.getBoolean("FashionStyle", false)) {
            desks.add("Fashion & Style");
        }

        if (settings.getBoolean("Sports", false)) {
            desks.add("Sports");
        }

        for (int i = 0; i < desks.size(); i++) {
            if (i == 0) {
                filter = "";
            }
            if (i == desks.size() - 1) {
                filter += desks.get(i);
            } else {
                filter += desks.get(i) + ",";
            }
        }

        if(filter != null) {
            filter = "news_desk:(" + filter + ")";
        }
        int sortByPosition = (int) settings.getLong("SortBy", 0);

        String beginDate = settings.getString("BeginDate", null);
        String convertedDate = null;
        if(!TextUtils.isEmpty(beginDate) ) {
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            convertedDate = df.format(new Date(beginDate));
        }

        //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Searching " + query + ".");
        String sortBy = getResources().getStringArray(R.array.sort_array)[sortByPosition];

        queryParams = new QuerySearchParams(query, filter, convertedDate, sortBy);

        articles.clear();
        // 2. Notify the adapter of the update
        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();
        searchArticles(queryParams, 0);
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


    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;

        switch (activeNetwork.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetwork.getState() == NetworkInfo.State.CONNECTING) &&
                        isInternet())
                    return true;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetwork.getState() == NetworkInfo.State.CONNECTING) &&
                        isInternet())
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    private static boolean isInternet() {
        Runtime runtime = Runtime.getRuntime();

        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }
}
