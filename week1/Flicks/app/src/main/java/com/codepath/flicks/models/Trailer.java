package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by darewreck on 3/12/17.
 */

public class Trailer {
    public String id;
    public String iso_639_1;
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    public Trailer(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.iso_639_1 = jsonObject.getString("iso_639_1");
        this.iso_3166_1 = jsonObject.getString("iso_3166_1");
        this.key = jsonObject.getString("key");
        this.name = jsonObject.getString("name");
        this.site = jsonObject.getString("site");
        this.size = jsonObject.getInt("size");
        this.type = jsonObject.getString("type");
    }

    public static ArrayList<Trailer> fromJsonArray(JSONArray array) {
        ArrayList<Trailer> results = new ArrayList<>();
        for(int i=0; i < array.length(); i++) {
            try {
                results.add(new Trailer(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
