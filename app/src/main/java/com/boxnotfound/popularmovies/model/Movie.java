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
    private List<String> genres;


    public Movie(long id, String title, String originalTitle, String overview, String releaseDate, double userRating, String posterPath, String backdropPosterPath, List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.posterPath = posterPath;
        this.backdropPosterPath = backdropPosterPath;
        this.genreIds = genreIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
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

    public void setBackdropPosterPath(String backdropPosterPath) {
        this.backdropPosterPath = backdropPosterPath;
    }
}
