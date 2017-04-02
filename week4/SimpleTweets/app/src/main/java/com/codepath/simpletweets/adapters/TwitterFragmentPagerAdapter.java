package com.codepath.simpletweets.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.simpletweets.fragments.MentionTimelineFragment;

/**
 * Created by darewreck_PC on 4/1/2017.
 */

public class TwitterFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String tabTitles[] = new String[] { "Home", "Mention" };

    public TwitterFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new HomeTimelineFragment();
        } else if(position == 1) {
            return new MentionTimelineFragment();
        } else {
            return null;
        }
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
