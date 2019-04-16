package com.boxnotfound.popularmovies.model;

import com.google.gson.annotations.SerializedName;

***REMOVED***

public class Movie {

***REMOVED***
        The RemoteMovieDataSource calls to retrieve popular movie data will be initially deserialized
        through the MovieJSONResult class via Gson, as per the TMDB returned JSON fields.  From there,
        the MovieJSONResult object's List<Movie> is populated from the returned
        "results" JSON array via Gson.
***REMOVED***
    @SerializedName("id") private long id;
    @SerializedName("title") private String title;
    @SerializedName("original_title") private String originalTitle;
    @SerializedName("overview") private String overview;
    @SerializedName("release_date") private String releaseDate;
    @SerializedName("vote_average") private double userRating;
    @SerializedName("poster_path") private String posterPath;
    @SerializedName("genre_ids") private List<Integer> genreIds;
    private List<String> genres;


    public Movie(long id, String title, String originalTitle, String overview, String releaseDate, double userRating, String posterPath, List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.posterPath = posterPath;
        this.genreIds = genreIds;
***REMOVED***

    public String getTitle() {
        return title;
***REMOVED***

    public void setTitle(String title) {
        this.title = title;
***REMOVED***

    public String getOriginalTitle() {
        return originalTitle;
***REMOVED***

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
***REMOVED***

    public String getOverview() {
        return overview;
***REMOVED***

    public void setOverview(String overview) {
        this.overview = overview;
***REMOVED***

    public String getReleaseDate() {
        return releaseDate;
***REMOVED***

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
***REMOVED***

    public double getUserRating() {
        return userRating;
***REMOVED***

    public void setUserRating(double userRating) {
        this.userRating = userRating;
***REMOVED***

    public String getPosterPath() {
        return posterPath;
***REMOVED***

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
***REMOVED***

    public List<Integer> getGenreIds() {
        return genreIds;
***REMOVED***

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
***REMOVED***

    public long getId() {
        return id;
***REMOVED***

    public void setId(long id) {
        this.id = id;
***REMOVED***
***REMOVED***
