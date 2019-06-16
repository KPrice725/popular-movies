package com.boxnotfound.popularmovies.model.source.movie;

import android.app.Application;

import androidx.annotation.NonNull;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.boxnotfound.popularmovies.model.source.AppDatabase;
import com.boxnotfound.popularmovies.model.utilities.AppExecutors;

import java.util.List;

public class LocalFavoriteMovieDataSource implements MovieDataSource {

    private static final String LOG_TAG = LocalFavoriteMovieDataSource.class.getSimpleName();

    private static LocalFavoriteMovieDataSource INSTANCE;
    private FavoriteMovieDao favoriteMovieDao;

    private LocalFavoriteMovieDataSource(@NonNull final Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        favoriteMovieDao = db.favoriteMovieDao();
    }

    public static LocalFavoriteMovieDataSource getInstance(@NonNull final Application application) {
        if (INSTANCE == null) {
            INSTANCE = new LocalFavoriteMovieDataSource(application);
        }
        return INSTANCE;
    }

    @Override
    public void getCachedMovies(@NonNull LoadMoviesCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Movie> favoriteMovies = favoriteMovieDao.getAllFavoriteMovies();
            if (favoriteMovies == null || favoriteMovies.isEmpty()) {
                callback.onMoviesNotAvailable();
            } else {
                callback.onMoviesLoaded(favoriteMovies);
            }
        });
    }

    @Override
    public void getMoreMovies(@NonNull SortParameters sortParameter, boolean newSortSelected, @NonNull LoadMoviesCallback callback) {
        callback.onMoviesNotAvailable();
    }

    @Override
    public void getMovieById(long movieId, @NonNull LoadMovieCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            final Movie movie = favoriteMovieDao.getMovieById(movieId);
            if (movie != null) {
                callback.onMovieLoaded(movie);
            } else {
                callback.onMovieNotAvailable();
            }
        });
    }

    @Override
    public void addMovie(@NonNull Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            favoriteMovieDao.insertFavoriteMovie(movie);
        });
    }

    @Override
    public void deleteMovie(@NonNull Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            favoriteMovieDao.deleteFavoriteMovie(movie);
        });
    }
}
