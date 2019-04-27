package com.boxnotfound.popularmovies.model.utilities;

public class ModelConstants {

    // Provides the base URL for the TMDB API genre list query to populate the GenreMap
    private static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";

    // Provides the base URL for the genre list query to populate the GenreMap
    public static final String TMDB_API_GENRE_URL = TMDB_API_BASE_URL + "genre/movie/list";

    // Provides the base URLs for the movie list query to populate the MovieList
    public static final String TMDB_API_GET_MOVIE_URL = TMDB_API_BASE_URL + "movie/";
    public static final String TMDB_API_MOST_POPULAR_URL = TMDB_API_GET_MOVIE_URL + "popular";
    public static final String TMDB_API_TOP_RATED_URL = TMDB_API_GET_MOVIE_URL + "top_rated";

    // API Key Parameter required for all TMDB API GET requests
    public static final String API_KEY_PARAM = "api_key";

    // Page number parameter needed when retrieving Movie data
    public static final String PAGE_PARAM = "page";
}
