package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author") private String author;
    @SerializedName("content") private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
