package com.boxnotfound.popularmovies.model.source.genre;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.model.Genre;
import com.boxnotfound.popularmovies.model.GenreJSONResult;
import com.boxnotfound.popularmovies.model.source.MovieApiKeyInjector;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.API_KEY_PARAM;
import static com.boxnotfound.popularmovies.model.utilities.ModelConstants.TMDB_API_GENRE_URL;


/*
The TMDB API has a separate table that stores key/value pairs of genres.  When retrieving
movie JSON data, these genres stored and returned as their respective integer keys.  To
decode these keys into their respective string values, we need to retrieve and cache the table
of genre key/value mappings from the TMDB API.
*/
public class RemoteGenreDataSource implements GenreDataSource {

    private static final String LOG_TAG = RemoteGenreDataSource.class.getSimpleName();

    private static RemoteGenreDataSource INSTANCE = null;

    private static OkHttpClient client;

    private RemoteGenreDataSource() {
        // prevent instantiation to limit usage to require getInstance()
    }

    public static RemoteGenreDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteGenreDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void loadGenres(@NonNull LoadGenresCallback callback) {
        Request request = buildRequest();
        getJsonFromRequest(request, callback);
    }

    private static @NonNull Request buildRequest() {

        /*
            Before we execute this call, we need to ensure that the developer has set their API
            Key, since this constant will be blank by default in the GitHub Repository.
        */
        String apiKey = MovieApiKeyInjector.inject();
        if (apiKey.equals("")) {
            throw new IllegalArgumentException("Error: No API Key Set. Developer needs to set TMDB API Key.");
        }
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(TMDB_API_GENRE_URL)).newBuilder()
                .addQueryParameter(API_KEY_PARAM, apiKey);

        String url = builder.build().toString();

        return new Request.Builder()
                .url(url)
                .build();
    }

    private static void initializeClient() {
        client = new OkHttpClient();
        client.dispatcher().setMaxRequests(1);
        client.dispatcher().setIdleCallback(() -> client = null);
    }

    private static void getJsonFromRequest(@NonNull final Request request, @NonNull final LoadGenresCallback callback) {
        if (client == null) {
            initializeClient();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onGenresNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Gson gson = new Gson();
                assert response.body() != null;
                GenreJSONResult result = gson.fromJson(response.body().charStream(), GenreJSONResult.class);
                List<Genre> genres = result.getGenreList();
                SparseArray<Genre> genreSparseArray = getGenreSparseArrayFromList(genres);
                callback.onGenresLoaded(genreSparseArray);
            }
        });
    }

    private static SparseArray<Genre> getGenreSparseArrayFromList(@NonNull final List<Genre> genres) {
        SparseArray<Genre> genreSparseArray = new SparseArray<>();
        for (Genre genre : genres) {
            genreSparseArray.append(genre.getId(), genre);
        }
        return genreSparseArray;
    }
}
