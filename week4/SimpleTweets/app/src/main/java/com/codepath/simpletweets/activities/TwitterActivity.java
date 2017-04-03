package com.codepath.simpletweets.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.TwitterApplication;
import com.codepath.simpletweets.adapters.TwitterFragmentPagerAdapter;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.simpletweets.fragments.TweetsListFragment;
import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TwitterActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogFragmentListener {
    private static final String TAG = TwitterActivity.class.getName();

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    @BindView(R.id.viewpager)
    ViewPager vpPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TwitterFragmentPagerAdapter twitterFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.twitter_white);
        twitterFragmentPagerAdapter = new TwitterFragmentPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        vpPager.setAdapter(twitterFragmentPagerAdapter);
        tabsStrip.setViewPager(vpPager);

        boolean isFromShareIntent = getIntent().getBooleanExtra("fromReceiveIntent", false);
        if(isFromShareIntent) {
            onComposeTweetClick();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_compose: {
                onComposeTweetClick();
                return true;
            }
            case R.id.action_user: {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);

        /*
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        final Activity _activity = this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        */
        return true;
    }


    public void onComposeTweetClick(){
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment fragment = ComposeTweetDialogFragment.newInstance(false, null);
        fragment.show(fm, "fragment_compose");
    }
    @Override
    public void onFinishCompose(Tweet tweet) {
        TweetsListFragment frag = ((TweetsListFragment) (twitterFragmentPagerAdapter.instantiateItem(vpPager, vpPager.getCurrentItem())));
        frag.onFinishCompose(tweet);
        //   int index = tweets.size();
        //  index = (index == 0) ? 0: index-1;
        //tweets.add(0, tweet);
        //adapter.notifyItemChanged(0);
        //layoutManager.scrollToPosition(0);
    }





}
