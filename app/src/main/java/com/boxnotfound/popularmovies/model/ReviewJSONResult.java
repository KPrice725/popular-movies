package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewJSONResult {

    @SerializedName("results") private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
