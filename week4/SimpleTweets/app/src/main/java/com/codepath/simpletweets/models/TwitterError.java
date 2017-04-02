package com.codepath.simpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darewreck_PC on 4/2/2017.
 */

public class TwitterError {
    public int code;
    public String message;

    public TwitterError() {

    }

    public static TwitterError fromJSON(JSONObject jsonObject) {
        TwitterError error = null;
        try {
            JSONArray errorsObj= jsonObject.getJSONArray("errors");
            int code = errorsObj.getJSONObject(0).getInt("code");
            String msg = errorsObj.getJSONObject(0).getString("message");

            error = new TwitterError();
            error.code = code;
            error.message = msg;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return error;
    }

}
