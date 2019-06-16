package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Genre implements Serializable {

    @SerializedName("id") private int id;
    @SerializedName("name") private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
