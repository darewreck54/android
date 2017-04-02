package com.codepath.simpletweets.models;

import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darewreck_PC on 3/26/2017.
 */

@Table(database = SimpleTweetsDb.class)
@Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel {
    @Column
    @PrimaryKey
    public long id;

    @Column
    public String createdAt;

    @Column
    public boolean favorited;

    @Column
    public String text;

    @Column
    public int retweetCount;

    @Column
    public int favoriteCount;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    public User user;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    public Entity entity;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    public Entity extendedEntity;

    public Tweet(){

    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = null;
        try {
            Long id = jsonObject.getLong("id");
            String createdAt = jsonObject.getString("created_at");
            Boolean favorited = jsonObject.getBoolean("favorited");
            String text = jsonObject.getString("text");
            int retweetCount = jsonObject.getInt("retweet_count");
            int favoriteCount = jsonObject.getInt("favorite_count");
            JSONObject userObject = jsonObject.getJSONObject("user");
            User user = User.fromJSON(userObject);
            tweet = new Tweet();
            tweet.id = id;
            tweet.createdAt = createdAt;
            tweet.favorited = favorited;
            tweet.text = text;
            tweet.retweetCount = retweetCount;
            tweet.favoriteCount = favoriteCount;
            tweet.user = user;
            tweet.entity = Entity.fromJSON(jsonObject.getJSONObject("entities"));
            if(jsonObject.has("extended_entities")) {
                tweet.extendedEntity = Entity.fromJSON(jsonObject.getJSONObject("extended_entities"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray){
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                Tweet tweet = Tweet.fromJSON(jsonArray.getJSONObject(i));
                tweets.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
