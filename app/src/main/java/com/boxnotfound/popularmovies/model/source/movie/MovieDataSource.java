package com.boxnotfound.popularmovies.model.source.movie;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;

import java.util.List;

import androidx.annotation.NonNull;

public interface MovieDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(@NonNull List<Movie> movies);

        void onMoviesNotAvailable();
    }

    void getCachedMovies(@NonNull final LoadMoviesCallback callback);

    void getMoreMovies(@NonNull final SortParameters sortParameter, final boolean newSortSelected, @NonNull final LoadMoviesCallback callback);

}