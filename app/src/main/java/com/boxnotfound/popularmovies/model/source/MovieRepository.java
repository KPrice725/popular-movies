package com.boxnotfound.popularmovies.model.source;

import android.util.Log;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class MovieRepository implements MovieDataSource {

    private static final String LOG_TAG = MovieRepository.class.getSimpleName();
    private static MovieRepository INSTANCE = null;

    private MovieDataSource remoteMovieDataSource;

    private Map<Long, Movie> cachedMovies;

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
    public void getMoreMovies(@NonNull final SortParameters sortParameter, final int pageNumber, @NonNull final LoadMoviesCallback callback) {
        remoteMovieDataSource.getMoreMovies(sortParameter, pageNumber, new LoadMoviesCallback() {
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
        if (cachedMovies == null) {
            cachedMovies = new LinkedHashMap<>();
        }

        for (Movie movie : movies) {
            cachedMovies.put(movie.getId(), movie);
        }
    }

    public Movie getMovieById(final long id) throws IllegalArgumentException {
        if (cachedMovies.containsKey(id)) {
            return cachedMovies.get(id);
        } else {
            Log.e(LOG_TAG, "Invalid movie id: " + id);
            throw new IllegalArgumentException("Invalid movie id: " + id);
        }
    }

    public void clearMovieCache() {
        if (cachedMovies != null) {
            cachedMovies.clear();
        }
    }
}
