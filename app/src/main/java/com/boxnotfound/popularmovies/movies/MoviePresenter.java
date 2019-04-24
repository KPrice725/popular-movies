package com.boxnotfound.popularmovies.movies;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.boxnotfound.popularmovies.model.source.MovieDataSource;
import com.boxnotfound.popularmovies.model.source.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;

public class MoviePresenter implements MovieContract.Presenter {

    private final MovieRepository movieRepository;

    private final MovieContract.View movieView;

    private SortParameters sortParameter;

    private static boolean firstLoad = true;

    private static boolean newSortSelected = true;

    public MoviePresenter(@NonNull MovieRepository movieRepository, @NonNull MovieContract.View movieView) {
        this.movieRepository = movieRepository;
        this.movieView = movieView;

        movieView.setPresenter(this);
    }

    @Override
    public void start() {
        if (sortParameter == null) {
            sortParameter = SortParameters.POPULARITY;
        }
        if (firstLoad) {
            loadMovies();
            firstLoad = false;
            newSortSelected = false;
        } else {
            refreshMovies();
        }
    }

    /*
        Provides a cached list of movies if available.  Otherwise, request to load new Movies
    */
    @Override
    public void refreshMovies() {
        movieRepository.getCachedMovies(new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@NonNull List<Movie> movies) {
                movieView.displayNewMovies(movies);
            }

            @Override
            public void onMoviesNotAvailable() {
                loadMovies();
            }
        });
    }

    @Override
    public void loadMovies() {
        movieView.displayLoadingIndicator(true);
        movieRepository.getMoreMovies(sortParameter, newSortSelected, new MovieDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(@NonNull List<Movie> movies) {
                movieView.displayLoadingIndicator(false);
                movieView.displayNewMovies(movies);
            }

            @Override
            public void onMoviesNotAvailable() {
                movieView.displayLoadingIndicator(false);
                if (movieRepository.getCachedMoviesSize() == 0) {
                    movieView.displayNoMovies();
                }
                movieView.displayLoadMoviesError();
            }
        });
    }

    @Override
    public void setSortParameter(@NonNull final SortParameters sortParameter) {
        if (!this.sortParameter.equals(sortParameter)) {
            this.sortParameter = sortParameter;
            newSortSelected = true;
            if (!firstLoad) {
                movieRepository.clearMovieCache();
                movieView.displayClearMovies();
                loadMovies();
                newSortSelected = false;
            }
        }
    }

    @Override
    public void openMovieDetails(@NonNull final Movie requestedMovie) {
        final long id = requestedMovie.getId();
        movieView.displayMovieDetailActivity(id);
    }
}
