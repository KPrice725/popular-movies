package com.boxnotfound.popularmovies.detail;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.model.Genre;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.genre.GenreDataSource;
import com.boxnotfound.popularmovies.model.source.genre.GenreRepository;
import com.boxnotfound.popularmovies.model.source.movie.MovieRepository;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DetailPresenter implements DetailContract.Presenter {

    private final MovieRepository movieRepository;

    private final GenreRepository genreRepository;

    private final DetailContract.View detailView;

    private final long movieId;

    public DetailPresenter(@NonNull final MovieRepository movieRepository,
                           @NonNull final GenreRepository genreRepository,
                           @NonNull final DetailContract.View detailView,
                           final long movieId) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.detailView = detailView;
        this.movieId = movieId;

        detailView.setPresenter(this);
    }


    @Override
    public void start() {
        detailView.displayLoadingIndicator(true);
        genreRepository.loadGenres(new GenreDataSource.LoadGenresCallback() {
            @Override
            public void onGenresLoaded(@NonNull SparseArray<Genre> genreMap) {
                loadMovie(movieId, genreMap);
            }

            @Override
            public void onGenresNotAvailable() {
                loadMovie(movieId, new SparseArray<>());
            }
        });
    }

    @Override
    public void loadMovie(long movieId, @NonNull SparseArray<Genre> genreMap) {
        Movie movie = movieRepository.getMovieById(movieId);
        if (movie != null) {
            detailView.displayLoadingIndicator(false);
            detailView.displayMovieTitle(movie.getTitle());
            detailView.displayMovieOriginalTitle(movie.getOriginalTitle());
            detailView.displayMovieOverview(movie.getOverview());
            detailView.displayMovieReleaseDate(movie.getReleaseDate());
            detailView.displayMovieUserRating(movie.getUserRating());

            String movieBackdropPosterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getBackdropPosterPath());
            detailView.displayMovieBackdropPoster(movieBackdropPosterUrl);

            String moviePosterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getPosterPath());
            detailView.displayMoviePoster(moviePosterUrl);

            List<String> genresNames = new ArrayList<>();
            if (genreMap.size() > 0) {
                List<Integer> genreIds = movie.getGenreIds();
                for (int i = 0; i < genreIds.size(); i++) {
                    int genreId = genreIds.get(i);
                    Genre genre = genreMap.get(genreId);
                    genresNames.add(genre.getName());
                }
            }
            detailView.displayMovieGenres(genresNames);
        } else {
            detailView.displayLoadingIndicator(false);
            detailView.displayNoMovieDetails();
        }
    }
}
