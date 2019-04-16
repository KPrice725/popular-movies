package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

***REMOVED***

public class MovieJSONResult {

***REMOVED***
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through this class via Gson, as per the TMDB returned JSON fields.
***REMOVED***
    @SerializedName("page") private int page;

    @SerializedName("total_pages") private int totalPages;

    @SerializedName("results") private List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
***REMOVED***

    public int getTotalPages() {
        return totalPages;
***REMOVED***

    public int getPage() {
        return page;
***REMOVED***
***REMOVED***
