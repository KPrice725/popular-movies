package com.boxnotfound.popularmovies.model.utilities;

public class MoviePosterUtils {

    //  Provides the base URL for the TMDB movie poster thumbnail images
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    // Constants to define the image size, which is appended to the Image Base URL
    private static final String IMAGE_SIZE_SMALL = "w185";
    private static final String IMAGE_SIZE_MEDIUM = "w342";
    private static final String IMAGE_SIZE_LARGE = "w500";

    private MoviePosterUtils() {
        // prevent instantiation to limit usage to static method calls
    }

    public static String getSmallMoviePosterUrlPath(String posterPath) {
        return TMDB_IMAGE_BASE_URL + IMAGE_SIZE_SMALL + posterPath;
    }

    public static String getMediumMoviePosterUrlPath(String posterPath) {
        return TMDB_IMAGE_BASE_URL + IMAGE_SIZE_MEDIUM + posterPath;
    }

    public static String getLargeMoviePosterUrlPath(String posterPath) {
        return TMDB_IMAGE_BASE_URL + IMAGE_SIZE_LARGE + posterPath;
    }
}
