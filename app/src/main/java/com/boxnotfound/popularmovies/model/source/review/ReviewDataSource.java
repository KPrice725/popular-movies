package com.boxnotfound.popularmovies.model.source.review;

import androidx.annotation.NonNull;

import com.boxnotfound.popularmovies.model.Review;

import java.util.List;

public interface ReviewDataSource {

    interface LoadReviewsCallback {

        void onReviewsLoaded(@NonNull final List<Review> reviews);

        void onReviewsNotAvailable();
    }

    void loadReviewsByMovieId(final long movieId, @NonNull final LoadReviewsCallback callback);
}
