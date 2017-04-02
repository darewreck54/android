package com.codepath.simpletweets.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darewreck_PC on 4/1/2017.
 */

public class UserInfo extends BaseModel {
    @Column
    @PrimaryKey
    public long id;

    @Column
    public String name;

    @Column
    public String screenName;

    @Column
    public String creationTime;

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

    public static UserInfo fromJSON(JSONObject jsonObject) {
        UserInfo user = null;
        try {
            long id = jsonObject.getLong("id");
            String name = jsonObject.getString("name");
            String screenName = jsonObject.getString("screen_name");
            String profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            String profileImageUrl = jsonObject.getString("profile_image_url");
            String location = jsonObject.getString("location");
            int friendsCount = jsonObject.getInt("friends_count");
            String description = jsonObject.getString("description");
            int followersCount = jsonObject.getInt("followers_count");
            int followingsCount = jsonObject.getInt("followers_count");

            user = new UserInfo();
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

    public static List<UserInfo> fromJSONArray(JSONArray jsonArray){
        List<UserInfo> users = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                UserInfo user = UserInfo.fromJSON(jsonArray.getJSONObject(i));
                users.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return users;
    }
}
