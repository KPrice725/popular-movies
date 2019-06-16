package com.boxnotfound.popularmovies.model.source.review;

import androidx.annotation.NonNull;

public class ReviewRepository implements ReviewDataSource {

    private static ReviewRepository INSTANCE = null;

    private final ReviewDataSource remoteReviewDataSource;

    private ReviewRepository(@NonNull final ReviewDataSource remoteReviewDataSource) {
        this.remoteReviewDataSource = remoteReviewDataSource;
    }

    public static ReviewRepository getInstance(@NonNull final ReviewDataSource remoteReviewDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ReviewRepository(remoteReviewDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadReviewsByMovieId(long movieId, @NonNull LoadReviewsCallback callback) {
        remoteReviewDataSource.loadReviewsByMovieId(movieId, callback);
    }
}
