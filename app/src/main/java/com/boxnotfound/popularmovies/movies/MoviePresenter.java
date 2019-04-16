package com.boxnotfound.popularmovies.movies;

***REMOVED***
***REMOVED***
import com.boxnotfound.popularmovies.model.source.MovieDataSource;
import com.boxnotfound.popularmovies.model.source.MovieRepository;

***REMOVED***

***REMOVED***

public class MoviePresenter implements MovieContract.Presenter {

    private final MovieRepository movieRepository;

    private final MovieContract.View movieView;

    private SortParameters sortParameter;

    private int pageNumber = 1;

    private boolean firstLoad = true;

    public MoviePresenter(@NonNull MovieRepository movieRepository, @NonNull MovieContract.View movieView) {
        this.movieRepository = movieRepository;
        this.movieView = movieView;

        movieView.setPresenter(this);
***REMOVED***

***REMOVED***
    public void start() {
        if (sortParameter == null) {
            sortParameter = SortParameters.POPULARITY;
    ***REMOVED***
        if (firstLoad) {
            loadMovies();
            firstLoad = false;
    ***REMOVED***
***REMOVED***

***REMOVED***
    public void loadMovies() {
        movieView.displayLoadingIndicator(true);
        movieRepository.getMoreMovies(sortParameter, pageNumber, new MovieDataSource.LoadMoviesCallback() {
        ***REMOVED***
            public void onMoviesLoaded(@NonNull List<Movie> movies) {
                movieView.displayLoadingIndicator(false);
                movieView.displayNewMovies(movies);
                pageNumber++;
        ***REMOVED***

        ***REMOVED***
            public void onMoviesNotAvailable() {
                movieView.displayLoadingIndicator(false);
                if (pageNumber == 1) {
                    movieView.displayNoMovies();
            ***REMOVED***
                movieView.displayLoadMoviesError();
        ***REMOVED***
    ***REMOVED***);
***REMOVED***

***REMOVED***
    public void setSortParameter(@NonNull final SortParameters sortParameter) {
        if (!this.sortParameter.equals(sortParameter)) {
            this.sortParameter = sortParameter;
            movieRepository.clearMovieCache();
            pageNumber = 1;
            loadMovies();
    ***REMOVED***
***REMOVED***

***REMOVED***
    public void openMovieDetails(@NonNull final Movie requestedMovie) {
        final long id = requestedMovie.getId();
        movieView.displayMovieDetailActivity(id);
***REMOVED***
***REMOVED***
