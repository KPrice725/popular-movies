package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoJSONResult {

    @SerializedName("results") private List<Video> results;

    public List<Video> getResults() {
        return results;
    }
}
