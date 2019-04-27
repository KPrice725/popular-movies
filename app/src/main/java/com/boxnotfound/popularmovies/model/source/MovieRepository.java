package com.boxnotfound.popularmovies.model.source;

import android.util.Log;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class MovieRepository implements MovieDataSource {

    private static final String LOG_TAG = MovieRepository.class.getSimpleName();
    private static MovieRepository INSTANCE = null;

    private final MovieDataSource remoteMovieDataSource;

    private Map<Long, Movie> cachedMovieMap;

    private ArrayList<Movie> cachedMovieList;

    private MovieRepository(@NonNull MovieDataSource remoteMovieDataSource) {
        this.remoteMovieDataSource = remoteMovieDataSource;
    }

    public static MovieRepository getInstance(@NonNull MovieDataSource remoteMovieDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(remoteMovieDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getCachedMovies(@NonNull LoadMoviesCallback callback) {
        if (cachedMovieList != null && cachedMovieList.size() > 0) {
            callback.onMoviesLoaded(cachedMovieList);
        } else {
            callback.onMoviesNotAvailable();
        }
    }

    public int getCachedMoviesSize() {
        if (cachedMovieList != null) {
            return cachedMovieList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void getMoreMovies(@NonNull final SortParameters sortParameter, final boolean newSortSelected, @NonNull final LoadMoviesCallback callback) {
        remoteMovieDataSource.getMoreMovies(sortParameter, newSortSelected, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@NonNull List<Movie> movies) {
                callback.onMoviesLoaded(movies);
                cacheMovies(movies);
            }

            @Override
            public void onMoviesNotAvailable() {
                callback.onMoviesNotAvailable();
            }
        });
    }

    private void cacheMovies(@NonNull List<Movie> movies) {
        if (cachedMovieMap == null) {
            cachedMovieMap = new LinkedHashMap<>();
        }

        if (cachedMovieList == null) {
            cachedMovieList = new ArrayList<>();
        }

        for (Movie movie : movies) {
            cachedMovieMap.put(movie.getId(), movie);
        }

        cachedMovieList.addAll(movies);
    }

    public Movie getMovieById(final long id) throws IllegalArgumentException {
        if (cachedMovieMap.containsKey(id)) {
            return cachedMovieMap.get(id);
        } else {
            throw new IllegalArgumentException("Invalid movie id: " + id);
        }
    }

    public void clearMovieCache() {
        if (cachedMovieMap != null) {
            cachedMovieMap.clear();
        }

        if (cachedMovieList != null) {
            cachedMovieList.clear();
        }
    }
}
