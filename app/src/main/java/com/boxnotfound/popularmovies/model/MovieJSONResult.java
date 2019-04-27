package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieJSONResult {

    /*
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through this class via Gson, as per the TMDB returned JSON fields.
    */
    @SerializedName("results") private final List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }

}
