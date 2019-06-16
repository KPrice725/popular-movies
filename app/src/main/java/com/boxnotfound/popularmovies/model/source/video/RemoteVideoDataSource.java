package com.boxnotfound.popularmovies.model.source.video;

import androidx.annotation.NonNull;

import com.boxnotfound.popularmovies.model.Video;
import com.boxnotfound.popularmovies.model.VideoJSONResult;
import com.boxnotfound.popularmovies.model.source.MovieApiKeyInjector;
import com.boxnotfound.popularmovies.model.utilities.ModelConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteVideoDataSource implements VideoDataSource {

    private static RemoteVideoDataSource INSTANCE = null;

    private static OkHttpClient client;

    private RemoteVideoDataSource() {

    }

    public static RemoteVideoDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteVideoDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void loadVideosByMovieId(long movieId, @NonNull LoadVideosCallback callback) {
        Request request = buildRequest(movieId);
        getJsonFromRequest(request, callback);
    }

    private static Request buildRequest(final long movieId) {
        String apiKey = MovieApiKeyInjector.inject();
        if (apiKey.equals("")) {
            throw new IllegalArgumentException("Error: No API Key Set. Developer needs to set TMDB API Key.");
        }
        String baseUrl = getBaseUrlFromMovieId(movieId);
        HttpUrl.Builder builder = Objects.requireNonNull(
                HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter(ModelConstants.API_KEY_PARAM, apiKey));

        String url = builder.build().toString();

        return new Request.Builder()
                .url(url)
                .build();

    }

    private static void getJsonFromRequest(@NonNull final Request request,
                                           @NonNull final LoadVideosCallback callback) {
        if (client == null) {
            client = new OkHttpClient();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onVideosNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                VideoJSONResult result = gson.fromJson(response.body().charStream(), VideoJSONResult.class);
                List<Video> results = result.getResults();
                callback.onVideosLoaded(results);
            }
        });
    }

    private static String getBaseUrlFromMovieId(final long movieId) {
        return ModelConstants.TMDB_API_GET_VIDEOS_URL + movieId + "/videos";
    }
}
