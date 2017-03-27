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
@Parcel(analyze={Media.class})
public class Media extends BaseModel {
    @Column
    @PrimaryKey
    public long id;

    @Column
    public String mediaUrl;
    @Column
    public String url;
    @Column
    public String type;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    public VideoInfo videoInfo;

    //@Column
   // @ForeignKey(saveForeignKeyModel = true)
    //Entity entity;

    public Media(){

    }

    public static Media fromJSON(JSONObject jsonObject) {
        Media media = null;
        try {
            long id = jsonObject.getLong("id");
            String mediaUrl = jsonObject.getString("media_url");
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            media = new Media();

            media.mediaUrl = mediaUrl;
            media.url = url;
            media.type = type;
            media.id = id;

            if(media.type.equals("video")) {
                JSONObject videoInfoJson = jsonObject.getJSONObject("video_info");
                if(videoInfoJson != null)
                {
                    VideoInfo videoInfo = VideoInfo.fromJSON(videoInfoJson);
                    media.videoInfo = videoInfo;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return media;
    }

    public static List<Media> fromJSONArray(JSONArray jsonArray){
        List<Media> medias = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                Media media = Media.fromJSON(jsonArray.getJSONObject(i));
                medias.add(media);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return medias;
    }
}
