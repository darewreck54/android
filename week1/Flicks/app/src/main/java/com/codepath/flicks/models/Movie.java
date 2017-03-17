package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by darewreck_PC on 3/8/2017.
 */

@Parcel(analyze={Movie.class})
public class Movie {
    public String posterPath;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public long popularity;
    public long voteCount;
    public boolean isVideo;
    public long voteAverage;
    public boolean isForAdults;
    public Date release_date;
    public String overview;
    public List<Long> genreIds;
    public long id;

    public String BASE_URL = "https://image.tmdb.org/t/p";
    public String FILE_SIZE = "w500";

    public Movie () {

    }
    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = BASE_URL + "/" + FILE_SIZE + jsonObject.getString("poster_path");
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
        this.backdropPath =  BASE_URL + "/" + FILE_SIZE + jsonObject.getString("backdrop_path");
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
