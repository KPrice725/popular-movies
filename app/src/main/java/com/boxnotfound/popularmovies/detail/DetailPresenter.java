package com.boxnotfound.popularmovies.detail;

import android.util.SparseArray;

import com.boxnotfound.popularmovies.model.Genre;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.genre.GenreDataSource;
import com.boxnotfound.popularmovies.model.source.genre.GenreRepository;
import com.boxnotfound.popularmovies.model.source.movie.MovieDataSource;
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

    private Movie movie;

    public DetailPresenter(@NonNull final MovieRepository movieRepository,
                           @NonNull final GenreRepository genreRepository,
                           @NonNull final DetailContract.View detailView,
                           final Movie movie, final long movieId) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.detailView = detailView;
        this.movie = movie;
        this.movieId = movieId;

        detailView.setPresenter(this);
    }


    @Override
    public void start() {
        detailView.displayLoadingIndicator(true);
        genreRepository.loadGenres(new GenreDataSource.LoadGenresCallback() {
            @Override
            public void onGenresLoaded(@NonNull SparseArray<Genre> genreMap) {
                loadMovie(genreMap);
            }

            @Override
            public void onGenresNotAvailable() {
                loadMovie(new SparseArray<>());
            }
        });
    }

    @Override
    public void loadMovie(@NonNull SparseArray<Genre> genreMap) {
        /*
        We receive both the movie and the movie ID from the intent Bundle.  If for some reason
        the Parcelable Movie object passed to us is null, retrieve the movie from the Repository
        by passing it the movie ID.
        */
        final boolean[] loading = {false};
        if (movie == null) {
            loading[0] = true;
            movieRepository.getMovieById(movieId, new MovieDataSource.LoadMovieCallback() {
                @Override
                public void onMovieLoaded(@NonNull Movie loadedMovie) {
                    movie = loadedMovie;
                    loading[0] = false;
                }

                @Override
                public void onMovieNotAvailable() {
                    loading[0] = false;
                }
            });
        }
        /*
            In the event that the movieRepository calls on an asynchronous request to load the movie,
            we want to wait until the movie is loaded before populating the UI.
        */
        while (loading[0]) ;
        if (movie != null) {
            detailView.displayMovieTitle(movie.getTitle());
            detailView.displayMovieOriginalTitle(movie.getOriginalTitle());
            detailView.displayMovieOverview(movie.getOverview());
            detailView.displayMovieReleaseDate(movie.getReleaseDate());
            detailView.displayMovieUserRating(movie.getUserRating());

            String movieBackdropPosterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getBackdropPosterPath());
            detailView.displayMovieBackdropPoster(movieBackdropPosterUrl);

            String moviePosterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getPosterPath());
            detailView.displayMoviePoster(moviePosterUrl);

            /*
                For populating the genreNames list, there are two potential options.  In the case of
                a get movies request, the JSON result returns a list of genre int IDs.  In the case of a
                get movie request, the JSON result returns a list of deserialized Genre objects.  Check
                both of these to attempt to fill the genreNames List.
            */
            List<String> genreNames = new ArrayList<>();
            if (genreMap.size() > 0) {
                List<Integer> genreIds = movie.getGenreIds();
                List<Genre> genreList = movie.getGenreList();
                if (genreIds != null) {
                    for (int i = 0; i < genreIds.size(); i++) {
                        int genreId = genreIds.get(i);
                        Genre genre = genreMap.get(genreId);
                        genreNames.add(genre.getName());
                    }
                } else if (genreList != null) {
                    for (Genre genre : genreList) {
                        genreNames.add(genre.getName());
                    }
                }
            }
            detailView.displayMovieGenres(genreNames);
            detailView.displayLoadingIndicator(false);
        } else {
            detailView.displayLoadingIndicator(false);
            detailView.displayNoMovieDetails();
        }
    }
}
