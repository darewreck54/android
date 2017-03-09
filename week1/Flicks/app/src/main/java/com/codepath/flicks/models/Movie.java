package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

public class Movie {
    private String posterPath;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private long popularity;
    private long voteCount;
    private boolean isVideo;
    private long voteAverage;
    private boolean isForAdults;
    private Date release_date;
    private String overview;
    private List<Long> genreIds;
    private long id;

    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = jsonObject.getString("poster_path");
        this.isForAdults = jsonObject.getBoolean("adult");
        this.overview = jsonObject.getString("overview");

        String date = jsonObject.getString("release_date");
        String[] dateInfo = date.split("-");
        int year = Integer.valueOf(dateInfo[0]);
        int month = Integer.valueOf(dateInfo[1]);
        int day = Integer.valueOf(dateInfo[2]);

        this.release_date = new Date(year, month, day);

        this.genreIds = new ArrayList<>();
        JSONArray genreArrayObj = jsonObject.getJSONArray("genre_ids");
        for(int i = 0; i < genreArrayObj.length(); i++) {
            long id  = genreArrayObj.getLong(i);
            genreIds.add(id);
        }
        this.id = jsonObject.getLong("id");
        this.originalTitle = jsonObject.getString("original_title");
        this.originalLanguage = jsonObject.getString("original_language");
        this.title = jsonObject.getString("title");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.popularity = jsonObject.getLong("popularity");
        this.isVideo = jsonObject.getBoolean("video");
        this.voteCount = jsonObject.getLong("vote_count");
        this.voteAverage = jsonObject.getLong("vote_average");
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();
        for(int i=0; i < array.length(); i++) {
            try {

                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    /* setters/getters */

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(long voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isForAdults() {
        return isForAdults;
    }

    public void setForAdults(boolean forAdults) {
        isForAdults = forAdults;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
