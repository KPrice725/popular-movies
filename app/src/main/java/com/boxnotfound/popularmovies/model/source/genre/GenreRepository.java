package com.boxnotfound.popularmovies.model.source.genre;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.model.Genre;

import androidx.annotation.NonNull;

public class GenreRepository implements GenreDataSource {

    private static final String LOG_TAG = GenreRepository.class.getSimpleName();
    private static GenreRepository INSTANCE = null;

    private final GenreDataSource remoteGenreDataSource;

    private SparseArray<Genre> cachedGenreMap;

    private GenreRepository(@NonNull final GenreDataSource remoteGenreDataSource) {
        this.remoteGenreDataSource = remoteGenreDataSource;
    }

    public static GenreRepository getInstance(@NonNull final GenreDataSource remoteGenreDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GenreRepository(remoteGenreDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void loadGenres(@NonNull LoadGenresCallback callback) {
        if (cachedGenreMap != null && cachedGenreMap.size() > 0) {
            callback.onGenresLoaded(cachedGenreMap);
        } else {
            remoteGenreDataSource.loadGenres(new LoadGenresCallback() {
                @Override
                public void onGenresLoaded(@NonNull SparseArray<Genre> genreMap) {
                    cachedGenreMap = genreMap;
                    callback.onGenresLoaded(cachedGenreMap);
                }

                @Override
                public void onGenresNotAvailable() {
                    callback.onGenresNotAvailable();
                }
            });
        }
    }
}
