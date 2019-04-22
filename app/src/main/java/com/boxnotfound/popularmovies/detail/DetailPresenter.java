package com.boxnotfound.popularmovies.detail;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;

import androidx.annotation.NonNull;

public class DetailPresenter implements DetailContract.Presenter {

    private MovieRepository movieRepository;

    private DetailContract.View detailView;

    private long movieId;

    public DetailPresenter(@NonNull final MovieRepository movieRepository, @NonNull final DetailContract.View detailView, final long movieId) {
        this.movieRepository = movieRepository;
        this.detailView = detailView;
        this.movieId = movieId;

        detailView.setPresenter(this);
    }


    @Override
    public void start() {
        loadMovie(movieId);
    }

    @Override
    public void loadMovie(long movieId) {
        Movie movie = movieRepository.getMovieById(movieId);
        detailView.displayLoadingIndicator(true);
        if (movie != null) {
            detailView.displayMovieTitle(movie.getTitle());
            detailView.displayMovieOriginalTitle(movie.getOriginalTitle());
            detailView.displayMovieOverview(movie.getOverview());
            detailView.displayMovieReleaseDate(movie.getReleaseDate());
            detailView.displayMovieUserRating(movie.getUserRating());

            String movieBackdropPosterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getBackdropPosterPath());
            detailView.displayMovieBackdropPoster(movieBackdropPosterUrl);

            //TODO implement genre id to string conversion methods
        } else {
            detailView.displayNoMovieDetails();
        }
    }
}
