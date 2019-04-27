package com.boxnotfound.popularmovies.model.source.genre;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.model.Genre;

import androidx.annotation.NonNull;

public interface GenreDataSource {

    interface LoadGenresCallback {

        void onGenresLoaded(@NonNull final SparseArray<Genre> genreMap);

        void onGenresNotAvailable();
    }

    void loadGenres(@NonNull final LoadGenresCallback callback);
}
