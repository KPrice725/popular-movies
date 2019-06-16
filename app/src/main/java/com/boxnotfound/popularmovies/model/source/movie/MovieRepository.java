package com.boxnotfound.popularmovies.model.source.movie;

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
    private final MovieDataSource favoriteMovieDataSource;

    private Map<Long, Movie> cachedMovieMap;

    private ArrayList<Movie> cachedMovieList;

    private MovieRepository(@NonNull MovieDataSource remoteMovieDataSource,
                            @NonNull MovieDataSource favoriteMovieDataSource) {
        this.remoteMovieDataSource = remoteMovieDataSource;
        this.favoriteMovieDataSource = favoriteMovieDataSource;
        cachedMovieMap = new LinkedHashMap<>();
        cachedMovieList = new ArrayList<>();
    }

    public static MovieRepository getInstance(@NonNull MovieDataSource remoteMovieDataSource,
                                              @NonNull MovieDataSource favoriteMovieDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(remoteMovieDataSource, favoriteMovieDataSource);
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
        if (sortParameter == SortParameters.FAVORITE) {
            if (cachedMovieList != null && cachedMovieList.size() == 0) {
                favoriteMovieDataSource.getCachedMovies(new LoadMoviesCallback() {
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
        } else {
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
    }

    @Override
    public void getMovieById(long movieId, @NonNull LoadMovieCallback callback) {
        /*
        First check the cache.  If the movie is not found in the cache, request
        it from the remote data source.
        */
        Movie movie = cachedMovieMap.get(movieId);
        if (movie == null || movie.getTitle() == null) {
            remoteMovieDataSource.getMovieById(movieId, callback);
        } else {
            callback.onMovieLoaded(movie);
        }
    }

    @Override
    public void addMovie(@NonNull Movie movie) {
        favoriteMovieDataSource.addMovie(movie);
    }

    @Override
    public void deleteMovie(@NonNull Movie movie) {
        favoriteMovieDataSource.deleteMovie(movie);
    }

    private void cacheMovies(@NonNull List<Movie> movies) {
        for (Movie movie : movies) {
            cachedMovieMap.put(movie.getId(), movie);
        }

        cachedMovieList.addAll(movies);
    }

    public void clearMovieCache() {
        if (cachedMovieMap != null) {
            cachedMovieMap.clear();
        }

        if (cachedMovieList != null) {
            cachedMovieList.clear();
        }
    }

    public void isFavoriteMovie(@NonNull final Movie loadedMovie, @NonNull final LoadMovieCallback callback) {
        favoriteMovieDataSource.getMovieById(loadedMovie.getId(), callback);
    }

    public void setFavoriteMovie(Movie movie, boolean favorite) {
        if (favorite) {
            favoriteMovieDataSource.addMovie(movie);
        } else {
            favoriteMovieDataSource.deleteMovie(movie);
        }
    }
}
