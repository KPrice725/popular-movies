package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {

    /*
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through the MovieJSONResult class via Gson, as per the TMDB returned JSON fields.  From there,
        the MovieJSONResult object's List<Movie> is populated from the returned
        "results" JSON array via Gson.
    */
    @SerializedName("id") private long id;
    @SerializedName("title") private String title;
    @SerializedName("original_title") private String originalTitle;
    @SerializedName("overview") private String overview;
    @SerializedName("release_date") private String releaseDate;
    @SerializedName("vote_average") private double userRating;
    @SerializedName("poster_path") private String posterPath;
    @SerializedName("backdrop_path") private String backdropPosterPath;
    @SerializedName("genre_ids") private List<Integer> genreIds;

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBackdropPosterPath() {
        return backdropPosterPath;
    }

}
