package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Genre {

    @SerializedName("id") private int id;
    @SerializedName("name") private String name;

    public Genre(final int id, @NonNull final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
