package com.boxnotfound.popularmovies.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.model.source.GenreRepository;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.source.RemoteGenreDataSource;
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;
import com.boxnotfound.popularmovies.movies.MovieActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private DetailContract.Presenter detailPresenter;

    private long movieId;

    private static int moviePosterWidth;
    private static int moviePosterHeight;
    private static int movieBackdropWidth;
    private static int movieBackdropHeight;

    @BindView(R.id.iv_movie_backdrop) ImageView movieBackdropIv;
    @BindView(R.id.iv_detail_poster) ImageView moviePosterIv;
    @BindView(R.id.toolbar_detail) Toolbar toolbar;
    @BindView(R.id.app_bar_layout) AppBarLayout layout;
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_detail_title) TextView titleTv;
    @BindView(R.id.tv_detail_original_title) TextView originalTitleTv;
    @BindView(R.id.tv_release_date) TextView releaseDateTv;
    @BindView(R.id.tv_user_rating) TextView userRatingTv;
    @BindView(R.id.tv_genres) TextView genresTv;
    @BindView(R.id.tv_overview) TextView overviewTv;
    @BindView(R.id.pb_load_details) ProgressBar progressBar;
    @BindView(R.id.details_layout) NestedScrollView detailsLayout;
    @BindView(R.id.details_error) RelativeLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getLong(MovieActivity.EXTRA_MOVIE_ID);
        }

        /*
        This listener allows the action bar's title to fade in and out
        depending on whether the collapsingtoolbarlayout is expanding
        or collapsing.  Ultimately, we want to show the backdrop image
        when expanded, and the title when collapsed.
        */
        layout.addOnOffsetChangedListener((appBarLayout, i) -> {
            float percentage = ((float) Math.abs(i) / layout.getTotalScrollRange());
            titleTv.setAlpha(percentage);
        });

        setPosterImageSize();
        setBackdropImageSize();

        detailPresenter = new DetailPresenter(MovieRepository.getInstance(RemoteMovieDataSource.getInstance()),
                GenreRepository.getInstance(RemoteGenreDataSource.getInstance()), this, movieId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayLoadingIndicator(boolean isLoading) {
        runOnUiThread(() -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void displayMovieTitle(@NonNull String movieTitle) {
        runOnUiThread(() -> {
            titleTv.setText(movieTitle);
        });
    }

    @Override
    public void displayMovieOriginalTitle(@NonNull String movieOriginalTitle) {
        runOnUiThread(() -> originalTitleTv.setText(movieOriginalTitle));
    }

    @Override
    public void displayMovieOverview(@NonNull String movieOverview) {
        runOnUiThread(() -> overviewTv.setText(movieOverview));
    }

    @Override
    public void displayMovieReleaseDate(@NonNull String movieReleaseDate) {
        runOnUiThread(() -> releaseDateTv.setText(movieReleaseDate));
    }

    @Override
    public void displayMovieUserRating(double movieUserRating) {
        runOnUiThread(() -> {
            String formattedRating = getString(R.string.user_rating, movieUserRating);
            userRatingTv.setText(formattedRating);
        });
    }

    @Override
    public void displayMovieGenres(@NonNull List<String> movieGenres) {
        /*
        We only want two genres per line, so separate each pair with a |
        */
        runOnUiThread(() -> {
            StringBuilder genres = new StringBuilder();
            int genreCount = 0;
            for (int i = 0; i < movieGenres.size(); i++) {
                genres.append(movieGenres.get(i));
                genreCount++;
                if (i < movieGenres.size() - 1) {
                    if (genreCount % 2 == 0) {
                        genres.append('\n');
                    } else {
                        genres.append(" | ");
                    }
                }
            }
            genresTv.setText(genres.toString());
        });
    }

    @Override
    public void displayMovieBackdropPoster(@NonNull String backdropPosterPath) {
        runOnUiThread(() -> {

            Picasso.get().load(backdropPosterPath)
                    .error(R.drawable.ic_connection_error_black_24dp)
                    .resize(movieBackdropWidth, movieBackdropHeight)
                    .centerCrop()
                    .into(movieBackdropIv, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(LOG_TAG, "load success! " + backdropPosterPath);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(LOG_TAG, "load error! " + backdropPosterPath);
                        }
                    });
        });
    }

    @Override
    public void displayMoviePoster(@NonNull String posterPath) {
        runOnUiThread(() -> {

            Picasso.get().load(posterPath)
                    .error(R.drawable.ic_connection_error_black_24dp)
                    .resize(moviePosterWidth, moviePosterHeight)
                    .centerCrop()
                    .into(moviePosterIv, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(LOG_TAG, "load success! " + posterPath);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(LOG_TAG, "load error! " + posterPath);
                        }
                    });
        });
    }

    private void setPosterImageSize() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int screenWidthInPx = Resources.getSystem().getDisplayMetrics().widthPixels;
            moviePosterWidth = (int) (screenWidthInPx / 2.25);
            moviePosterHeight = (int) (moviePosterWidth * 1.5);
        } else {
            int screenHeightInPx = Resources.getSystem().getDisplayMetrics().heightPixels;
            moviePosterHeight = (int) (screenHeightInPx / 1.5);
            moviePosterWidth = (int) (moviePosterHeight * (2.0 / 3.0));
        }
        moviePosterIv.getLayoutParams().width = moviePosterWidth;
        moviePosterIv.getLayoutParams().height = moviePosterHeight;
    }

    private void setBackdropImageSize() {
        movieBackdropWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        movieBackdropHeight = (int) (movieBackdropWidth / 1.75);
        movieBackdropIv.getLayoutParams().width = movieBackdropWidth;
        movieBackdropIv.getLayoutParams().height = movieBackdropHeight;
    }

    @Override
    public void displayNoMovieDetails() {
        runOnUiThread(() -> {
            detailsLayout.setVisibility(View.INVISIBLE);
            errorLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        detailPresenter = presenter;
    }
}
