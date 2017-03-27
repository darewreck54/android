package com.codepath.simpletweets.models;

import com.codepath.simpletweets.data.SimpleTweetsDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
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
/*
    "entities": {
        "url": {
            "urls": [{
                "expanded_url": null,
                "url": "http://bit.ly/oauth-dancer",
                "indices": [
                    0,
                    26
                ],
                "display_url": null
            }]
        },
        "description": null
    }
*/
@Table(database = SimpleTweetsDb.class)
@Parcel(analyze={Entity.class})
public class Entity extends BaseModel {

    @Column
    @PrimaryKey(autoincrement=true)
    public long id;

    @ColumnIgnore
    @ForeignKey(saveForeignKeyModel = true)
    public Media media;

    // public List<Media> medias;

    /*
    @Column
    @ForeignKey(saveForeignKeyModel = true)
    Tweet tweet;


    @OneToMany(methods = OneToMany.Method.ALL)
    public List<Media>[] getMedias() {
        if (medias == null) {
            medias = new Select()
                    .from(Media.class)
                    .where(Media_Table.id.eq(id))
                    .queryList();
        }
        return medias;
    }
*/
    public Entity() {
    }

    public static Entity fromJSON(JSONObject jsonObject) {
        Entity entity = null;
        try {
            entity = new Entity();
            if(jsonObject.has("media")) {
                JSONArray mediaArrays = jsonObject.getJSONArray("media");
                // entity.medias = Media.fromJSONArray(mediaArrays);
                if(mediaArrays.length() > 0) {
                    entity.media = Media.fromJSON(mediaArrays.getJSONObject(0));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public static List<Entity> fromJSONArray(JSONArray jsonArray){
        List<Entity> entities = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                Entity entity = Entity.fromJSON(jsonArray.getJSONObject(i));
                entities.add(entity);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return entities;
    }

}
