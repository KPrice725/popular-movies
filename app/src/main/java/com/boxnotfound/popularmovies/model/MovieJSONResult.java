package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieJSONResult {

    /*
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through this class via Gson, as per the TMDB returned JSON fields.
    */
    @SerializedName("page") private int page;

    @SerializedName("total_pages") private int totalPages;

    @SerializedName("results") private List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return page;
    }
}
