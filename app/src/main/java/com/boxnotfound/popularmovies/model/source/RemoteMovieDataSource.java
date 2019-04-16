package com.boxnotfound.popularmovies.model.source;


import android.util.Log;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.MovieJSONResult;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteMovieDataSource implements MovieDataSource {

    private static final String LOG_TAG = RemoteMovieDataSource.class.getSimpleName();

    private static RemoteMovieDataSource INSTANCE = null;

    /*
        API_KEY deliberately blank to respect TMDB's API Key security
        and privacy requirements.  The end user should paste their
        own personal API key to this constant to utilize the TMDB functionality.
    */
    private static final String API_KEY = "aba27a2ef6ffc72d6c249ef5e89b66d1";
    private static final String API_KEY_PARAM = "api_key";

    //TODO: IMPLEMENT PAGE NUMBER HANDLING TO GET FURTHER INTO THE LIST
    private static int PAGE_NUMBER = 1;
    private static final String PAGE_PARAM = "page";

    // Provides the base URL for the movie list query to populate the MovieList
    private static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TMDB_API_MOST_POPULAR_URL = TMDB_API_BASE_URL + "movie/popular";
    private static final String TMDB_API_TOP_RATED_URL = TMDB_API_BASE_URL + "movie/top_rated";

    private RemoteMovieDataSource() {
        // prevent instantiation to limit usage to require getInstance()
    }

    public static RemoteMovieDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteMovieDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getMoreMovies(@NonNull final SortParameters sortParameter, final int pageNumber, @NonNull final LoadMoviesCallback callback) {
        Request request = buildRequest(sortParameter, pageNumber);
        getJsonFromRequest(request, callback);
    }

    private static @NonNull Request buildRequest(@NonNull final SortParameters sort, final int pageNumber) {

        /*
            Before we execute this call, we need to ensure that the developer has set their API
            Key, since this constant will be blank by default in the GitHub Repository.
        */
        if (API_KEY.equals("")) {
            Log.e(LOG_TAG, "Error: No API Key Set. Developer needs to set TMDB API Key.");
            return null;
        }

        /*
            Set the base URL to retrieve either the most popular or highest rated movie list,
            according to the User's preference
        */
        String baseUrl = getBaseUrlFromSortParameter(sort);

        HttpUrl.Builder builder = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter(API_KEY_PARAM, API_KEY)
                .addQueryParameter(PAGE_PARAM, String.valueOf(pageNumber));

        String url = builder.build().toString();

        Log.v(LOG_TAG, "URL Built: " + url);

        return new Request.Builder()
                .url(url)
                .build();
    }

    private static void getJsonFromRequest(@NonNull final Request request, @NonNull final LoadMoviesCallback callback) {
        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "getResponse onFailure: " + e.toString());
                callback.onMoviesNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                MovieJSONResult result = gson.fromJson(response.body().charStream(), MovieJSONResult.class);
                List<Movie> movies = result.getResults();
                callback.onMoviesLoaded(movies);
            }
        });
    }

    private static @NonNull String getBaseUrlFromSortParameter(@NonNull final SortParameters sort) throws IllegalArgumentException {
        switch (sort) {
            case POPULARITY:
                return TMDB_API_MOST_POPULAR_URL;
            case RATING:
                return TMDB_API_TOP_RATED_URL;
            default:
                Log.e(LOG_TAG, "Error: Unknown SortParameter value: " + sort.toString());
                throw new IllegalArgumentException("Error: Unknown SortParameter value: " + sort.toString());
        }
    }
}
