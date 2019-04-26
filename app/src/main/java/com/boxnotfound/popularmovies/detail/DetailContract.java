package com.boxnotfound.popularmovies.detail;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.BasePresenter;
import com.boxnotfound.popularmovies.BaseView;
import com.boxnotfound.popularmovies.model.Genre;

import java.util.List;

import androidx.annotation.NonNull;

public interface DetailContract {

    interface Presenter extends BasePresenter {

        void loadMovie(final long movieId, @NonNull final SparseArray<Genre> genreMap);

    }

    interface View extends BaseView<Presenter> {

        void displayLoadingIndicator(final boolean isLoading);

        void displayMovieTitle(@NonNull final String movieTitle);

        void displayMovieOriginalTitle(@NonNull final String movieOriginalTitle);

        void displayMovieOverview(@NonNull final String movieOverview);

        void displayMovieReleaseDate(@NonNull final String movieReleaseDate);

        void displayMovieUserRating(final double movieUserRating);

        void displayMovieGenres(@NonNull final List<String> movieGenres);

        void displayMovieBackdropPoster(@NonNull final String backdropPosterPath);

        void displayMoviePoster(@NonNull final String posterPath);

        void displayNoMovieDetails();
    }
}
