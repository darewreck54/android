package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.simpletweets.fragments.FavoritesFragment;
import com.codepath.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.simpletweets.fragments.MentionTimelineFragment;
import com.codepath.simpletweets.fragments.PhotosFragment;
import com.codepath.simpletweets.fragments.UserTimelineFragment;

/**
 * Created by darewreck_PC on 4/2/2017.
 */

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String tabTitles[] = new String[] { "Tweets", "Photos", "Favorites" };
    private String screenName;

    public ProfileFragmentPagerAdapter(String screenName, FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.screenName = screenName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0) {
            fragment =  UserTimelineFragment.newInstance(screenName);
        } else if(position ==1) {
            fragment = new PhotosFragment();
        } else if(position == 2) {
            fragment = new FavoritesFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
