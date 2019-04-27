package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

public class GenreJSONResult {

    /*
        The RemoteGenreDataSource calls to retrieve genre data will be initially deserialized
        through this class via Gson, as per the TMDB returned JSON fields.
    */
    @SerializedName("genres") private final List<Genre> genreList;

    public GenreJSONResult(@NonNull final List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
