package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Genre {

    @SerializedName("id") private int id;
    @SerializedName("name") private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
