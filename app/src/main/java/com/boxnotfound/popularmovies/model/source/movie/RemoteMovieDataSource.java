package com.boxnotfound.popularmovies.model.source.movie;


import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.MovieJSONResult;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.boxnotfound.popularmovies.model.source.MovieApiKeyInjector;
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

import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.API_KEY_PARAM;
import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.PAGE_PARAM;
import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.TMDB_API_MOST_POPULAR_URL;
import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.TMDB_API_TOP_RATED_URL;

public class RemoteMovieDataSource implements MovieDataSource {

    private static final String LOG_TAG = RemoteMovieDataSource.class.getSimpleName();

    private static RemoteMovieDataSource INSTANCE = null;

    private static OkHttpClient client;

    private static int PAGE_NUMBER = 1;

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
    public void getCachedMovies(@NonNull LoadMoviesCallback callback) {
        // Remote Source will never have cached movie data
        callback.onMoviesNotAvailable();
    }

    @Override
    public void getMoreMovies(@NonNull final SortParameters sortParameter, final boolean newSortSelected, @NonNull final LoadMoviesCallback callback) {
        if (newSortSelected) {
            PAGE_NUMBER = 1;
        }
        Request request = buildRequest(sortParameter);
        getJsonFromRequest(request, callback);
    }

    private static @NonNull Request buildRequest(@NonNull final SortParameters sort) {

        /*
            Before we execute this call, we need to ensure that the developer has set their API
            Key, since this constant will be blank by default in the GitHub Repository.
        */
        String apiKey = MovieApiKeyInjector.inject();
        if (apiKey.equals("")) {
            throw new IllegalArgumentException("Error: No API Key Set. Developer needs to set TMDB API Key.");
        }

        /*
            Set the base URL to retrieve either the most popular or highest rated movie list,
            according to the User's preference
        */
        String baseUrl = getBaseUrlFromSortParameter(sort);

        HttpUrl.Builder builder = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter(API_KEY_PARAM, apiKey)
                .addQueryParameter(PAGE_PARAM, String.valueOf(PAGE_NUMBER));

        String url = builder.build().toString();

        return new Request.Builder()
                .url(url)
                .build();
    }

    private static void getJsonFromRequest(@NonNull final Request request, @NonNull final LoadMoviesCallback callback) {
        if (client == null) {
            initializeClient();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                client.dispatcher().cancelAll();
                callback.onMoviesNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) {
                PAGE_NUMBER++;
                Gson gson = new Gson();
                MovieJSONResult result = gson.fromJson(response.body().charStream(), MovieJSONResult.class);
                List<Movie> movies = result.getResults();
                callback.onMoviesLoaded(movies);
            }
        });
    }

    private static void initializeClient() {
        client = new OkHttpClient();
        client.dispatcher().setMaxRequests(1);
        client.dispatcher().setIdleCallback(() -> client = null);
    }

    private static @NonNull String getBaseUrlFromSortParameter(@NonNull final SortParameters sort) throws IllegalArgumentException {
        switch (sort) {
            case POPULARITY:
                return TMDB_API_MOST_POPULAR_URL;
            case RATING:
                return TMDB_API_TOP_RATED_URL;
            default:
                throw new IllegalArgumentException("Error: Unknown SortParameter value: " + sort.toString());
        }
    }
}
