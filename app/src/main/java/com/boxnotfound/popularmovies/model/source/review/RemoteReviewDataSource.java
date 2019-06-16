package com.boxnotfound.popularmovies.model.source.review;

import androidx.annotation.NonNull;

import com.boxnotfound.popularmovies.model.Review;
import com.boxnotfound.popularmovies.model.ReviewJSONResult;
import com.boxnotfound.popularmovies.model.source.MovieApiKeyInjector;
import com.boxnotfound.popularmovies.model.utilities.ModelConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteReviewDataSource implements ReviewDataSource {

    private static RemoteReviewDataSource INSTANCE = null;

    private static OkHttpClient client;

    private RemoteReviewDataSource() {

    }

    public static RemoteReviewDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteReviewDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void loadReviewsByMovieId(long movieId, @NonNull LoadReviewsCallback callback) {
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
                        .addQueryParameter(ModelConstants.API_KEY_PARAM, apiKey))
                        .addQueryParameter(ModelConstants.PAGE_PARAM, "1");

        String url = builder.build().toString();

        return new Request.Builder()
                .url(url)
                .build();

    }

    private static void getJsonFromRequest(@NonNull final Request request,
                                           @NonNull final LoadReviewsCallback callback) {
        if (client == null) {
            client = new OkHttpClient();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onReviewsNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ReviewJSONResult result = gson.fromJson(response.body().charStream(), ReviewJSONResult.class);
                List<Review> results = result.getResults();
                callback.onReviewsLoaded(results);
            }
        });
    }

    private static String getBaseUrlFromMovieId(final long movieId) {
        return ModelConstants.TMDB_API_GET_REVIEWS_URL + movieId + "/reviews";
    }
}
