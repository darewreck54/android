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
@Parcel(analyze={Variant.class})
public class Variant extends BaseModel{

    @Column
    @PrimaryKey(autoincrement=true)
    public long key;
    @Column
    public String contentType;
    @Column
    public String url;

    public Variant(){

    }

    public static Variant fromJSON(JSONObject jsonObject) {
        Variant variant = null;
        try {
            String contentType = jsonObject.getString("content_type");
            String url = jsonObject.getString("url");
            variant = new Variant();
            variant.url = url;
            variant.contentType = contentType;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return variant;
    }

    public static List<Variant> fromJSONArray(JSONArray jsonArray){
        List<Variant> variants = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                Variant variant = Variant.fromJSON(jsonArray.getJSONObject(i));
                variants.add(variant);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return variants;
    }
}
