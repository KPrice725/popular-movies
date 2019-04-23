package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

public class GenreJSONResult {

    @SerializedName("genres") private List<Genre> genreList;

    public GenreJSONResult(@NonNull final List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
