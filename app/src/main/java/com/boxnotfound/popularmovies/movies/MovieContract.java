package com.boxnotfound.popularmovies.movies;

import com.boxnotfound.popularmovies.BasePresenter;
import com.boxnotfound.popularmovies.BaseView;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;

import java.util.List;

import androidx.annotation.NonNull;

public interface MovieContract {

    interface Presenter extends BasePresenter {

        void refreshMovies();

        void loadMovies();

        void setSortParameter(@NonNull SortParameters sortParameter);

        void openMovieDetails(@NonNull Movie requestedMovie);

    }

    interface View extends BaseView<Presenter> {

        void displayLoadingIndicator(boolean isLoading);

        void displayNewMovies(@NonNull List<Movie> movies);

        void displayCachedMovies(@NonNull List<Movie> movies);

        void displayNoMovies();

        void displayClearMovies();

        void displayLoadMoviesError();

        void displayMovieDetailActivity(@NonNull final Movie requestedMovie);
    }
}
