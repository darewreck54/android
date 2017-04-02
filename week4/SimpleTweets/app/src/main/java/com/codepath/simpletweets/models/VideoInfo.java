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
@Parcel(analyze={VideoInfo.class})
public class VideoInfo extends BaseModel{
    @Column
    @PrimaryKey(autoincrement=true)
    public long key;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    public Variant variant;

    public VideoInfo(){

    }

    public static VideoInfo fromJSON(JSONObject jsonObject) {
        VideoInfo videoInfo = null;
        try {
            JSONArray variantJsonArray = jsonObject.getJSONArray("variants");
            List<Variant> variants = Variant.fromJSONArray(variantJsonArray);
            videoInfo = new VideoInfo();
            if(variants != null && variants.size() > 0) {
                videoInfo.variant = variants.get(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videoInfo;
    }

    public static List<VideoInfo> fromJSONArray(JSONArray jsonArray){
        List<VideoInfo> videoInfos = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                VideoInfo videoInfo = VideoInfo.fromJSON(jsonArray.getJSONObject(i));
                videoInfos.add(videoInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return videoInfos;
    }
}
