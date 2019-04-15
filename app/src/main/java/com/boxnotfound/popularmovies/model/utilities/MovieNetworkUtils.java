package com.boxnotfound.popularmovies.model.utilities;

public class TmdbConstants {

***REMOVED***  API_KEY deliberately blank to respect TMDB's API Key security
***REMOVED***
***REMOVED***
***REMOVED***
    public static final String API_KEY = "";
    public static final String API_KEY_PARAM = "api_key";

    // Provides the base URL for the movie list query to MovieList
***REMOVED***
    public static final String TMDB_API_DISCOVER_URL = TMDB_API_BASE_URL + "discover/movie";

    // Constants to populate the sort_by portion of the API query
    public static final String SORT_BY_PARAM = "sort_by";
    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
    public static final String SORT_BY_POPULARITY_ASC = "popularity.asc";
    public static final String SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String SORT_BY_VOTE_AVERAGE_ASC = "vote_average.asc";

    // Constants to define the release date portion of the API query
    public static final String RELEASE_DATE_UPPER_LIMIT_PARAM = "primary_release_date.lte";
    public static final String RELEASE_DATE_LOWER_LIMIT_PARAM = "primary_release_date.gte";

    // Provides the base URL for the movie poster thumbnail images in the MovieActivity Grid
    public static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    // Constants to define the image size, which is appended to the Image Base URL
    public static final String IMAGE_SIZE_SMALL = "w185";
    public static final String IMAGE_SIZE_MEDIUM = "w342";
    public static final String IMAGE_SIZE_LARGE = "w500";

***REMOVED***
