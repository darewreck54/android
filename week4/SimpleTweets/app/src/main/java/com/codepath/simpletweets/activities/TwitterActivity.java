package com.codepath.simpletweets.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.simpletweets.R;
import com.codepath.simpletweets.adapters.TwitterFragmentPagerAdapter;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.simpletweets.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwitterActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogFragmentListener {
    private static final String TAG = TwitterActivity.class.getName();

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    @BindView(R.id.viewpager)
    ViewPager vpPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.twitter_white);
        vpPager.setAdapter(new TwitterFragmentPagerAdapter(getSupportFragmentManager(),getApplicationContext()));
        tabsStrip.setViewPager(vpPager);

    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return true;
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

    public void onComposeTweetClick(){
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment fragment = ComposeTweetDialogFragment.newInstance(false, null);
        fragment.show(fm, "fragment_compose");
    }

    @Override
    public void onFinishCompose(Tweet tweet) {
     //   int index = tweets.size();
      //  index = (index == 0) ? 0: index-1;
        /*
        tweets.add(0, tweet);
        adapter.notifyItemChanged(0);
        layoutManager.scrollToPosition(0);
        */
    }
}
