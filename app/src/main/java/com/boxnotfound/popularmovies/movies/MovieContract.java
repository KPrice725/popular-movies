package com.boxnotfound.popularmovies.movies;

import com.boxnotfound.popularmovies.BasePresenter;
import com.boxnotfound.popularmovies.BaseView;
***REMOVED***
***REMOVED***

***REMOVED***

***REMOVED***

public interface MovieContract {

    interface Presenter extends BasePresenter {
        void loadMovies();

        void setSortParameter(@NonNull SortParameters sortParameter);

        void openMovieDetails(@NonNull Movie requestedMovie);

***REMOVED***

    interface View extends BaseView<Presenter> {

        void displayLoadingIndicator(boolean isLoading);

        void displayNewMovies(@NonNull List<Movie> movies);

        void displayNoMovies();

        void displayLoadMoviesError();

        void displayMovieDetailActivity(final long movieId);
***REMOVED***
***REMOVED***
