package com.boxnotfound.popularmovies.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.model.source.GenreRepository;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.source.RemoteGenreDataSource;
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;
import com.boxnotfound.popularmovies.movies.MovieActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private DetailContract.Presenter detailPresenter;

    private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getLong(MovieActivity.EXTRA_MOVIE_ID);
        }

        detailPresenter = new DetailPresenter(MovieRepository.getInstance(RemoteMovieDataSource.getInstance()),
                GenreRepository.getInstance(RemoteGenreDataSource.getInstance()), this, movieId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPresenter.start();
    }

    @Override
    public void displayLoadingIndicator(boolean isLoading) {

    }

    @Override
    public void displayMovieTitle(@NonNull String movieTitle) {

    }

    @Override
    public void displayMovieOriginalTitle(@NonNull String movieOriginalTitle) {

    }

    @Override
    public void displayMovieOverview(@NonNull String movieOverview) {

    }

    @Override
    public void displayMovieReleaseDate(@NonNull String movieReleaseDate) {

    }

    @Override
    public void displayMovieUserRating(double movieUserRating) {

    }

    @Override
    public void displayMovieGenres(@NonNull List<String> movieGenres) {

    }

    @Override
    public void displayMovieBackdropPoster(@NonNull String backdropPosterPath) {

    }

    @Override
    public void displayNoMovieDetails() {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        detailPresenter = presenter;
    }
}
