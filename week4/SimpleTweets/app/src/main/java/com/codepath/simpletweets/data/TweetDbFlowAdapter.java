package com.codepath.simpletweets.data;

import com.codepath.simpletweets.models.Tweet;
import com.codepath.simpletweets.models.Tweet_Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

public class TweetDbFlowAdapter {
    public static List<Tweet> queryTweets() {
        final List<Tweet> taskList = SQLite.select().from(Tweet.class).queryList();
        return taskList;
    }

    public static void clearTable() {
        Delete.table(Tweet.class);
    }

    public static Tweet addTweet(Tweet tweet) {
        tweet.save();
        return tweet;
    }

}
