package com.codepath.simpletweets.models;

import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.raizlabs.android.dbflow.annotation.Column;
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
@Parcel(analyze={User.class})
public class User extends BaseModel {
    @Column
    @PrimaryKey
    public long id;

    @Column
    public String name;

    @Column
    public String screenName;

    @Column
    public String profileBackgroundImageUrl;

    @Column
    public String profileImageUrl;

    @Column
    public String location;

    @Column
    public int friendsCount;

    @Column
    public String description;

    @Column
    public int followersCount;

    @Column
    public int followingsCount;

    @Column
    public String url;


    public User() {

    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = null;
        try {
            long id = jsonObject.getLong("id");
            String name = jsonObject.getString("name");
            String screenName = jsonObject.getString("screen_name");
            String profileBackgroundImageUrl =  jsonObject.has("profile_banner_url") ? jsonObject.getString("profile_banner_url"):null;
            String profileImageUrl = jsonObject.getString("profile_image_url");
            String location = jsonObject.getString("location");
            int friendsCount = jsonObject.getInt("friends_count");
            String description = jsonObject.has("description") ? jsonObject.getString("description"):"";
            int followersCount = jsonObject.getInt("followers_count");
            int followingsCount = jsonObject.getInt("followers_count");

            user = new User();
            user.id = id;
            user.name = name;
            user.screenName = screenName;
            user.profileBackgroundImageUrl = profileBackgroundImageUrl;
            user.profileImageUrl = profileImageUrl;
            user.location = location;
            user.followersCount = followersCount;
            user.friendsCount = friendsCount;
            user.description = description;
            user.followingsCount = 0;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static List<User> fromJSONArray(JSONArray jsonArray){
        List<User> users = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                User user = User.fromJSON(jsonArray.getJSONObject(i));
                if(user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return users;
    }
}
