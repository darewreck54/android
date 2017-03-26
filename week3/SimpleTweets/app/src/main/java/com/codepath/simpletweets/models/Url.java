package com.codepath.simpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 {
     "expanded_url": null,
     "url": "http://bit.ly/oauth-dancer",
     "indices": [
         0,
         26
     ],
     "display_url": null
 }
 */
public class Url {
    private final String expandedUrl;
    private final String url;
    private final String displayName;
    public Url(String expandedUrl, String url, String displayName) {
        this.expandedUrl = expandedUrl;
        this.url = url;
        this.displayName = displayName;
    }

    public static Url fromJSON(JSONObject jsonObject) {
        Url url = null;
        try {
            String expandedUrl = jsonObject.getString("expanded_url");
            String urlString = jsonObject.getString("url");
            String displayUrl = jsonObject.getString("display_url");
            url = new Url(expandedUrl, urlString, displayUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static List<Url> fromJSONArray(JSONArray jsonArray){
        return null;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getDisplayName() {
        return displayName;
    }
}
