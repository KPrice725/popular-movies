package com.boxnotfound.popularmovies.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.detail.DetailActivity;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.boxnotfound.popularmovies.model.source.movie.MovieRepository;
import com.boxnotfound.popularmovies.model.source.movie.RemoteMovieDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    private static final String SORT_MENU_ITEM_SELECTED = "sort_menu_item_selected";

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private int selected = -1;

    private MovieContract.Presenter moviePresenter;

    private MovieGridLayoutManager layoutManager;

    private MovieAdapter adapter;

    private boolean readyToLoad = true;

    private Snackbar errorSnackbar;

    @BindView(R.id.rv_movies) RecyclerView recyclerView;
    @BindView(R.id.display_error) RelativeLayout errorView;
    @BindView(R.id.pb_load_movies) ProgressBar progressBar;
    @BindView(R.id.toolbar_movie) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        moviePresenter = new MoviePresenter(
                MovieRepository.getInstance(RemoteMovieDataSource.getInstance()),
                this);
        recyclerView = findViewById(R.id.rv_movies);
        int screenWidthInPx = Resources.getSystem().getDisplayMetrics().widthPixels;
        layoutManager = new MovieGridLayoutManager(this, screenWidthInPx);
        adapter = new MovieAdapter(moviePresenter, layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new MovieScrollListener());

        if (savedInstanceState != null) {
            selected = savedInstanceState.getInt(SORT_MENU_ITEM_SELECTED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_movies, menu);
        int id = -1;
        if (selected != -1) {
            id = selected;
        } else {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (item.isChecked()) {
                    id = item.getItemId();
                }
            }
        }

        switch (id) {
            case R.id.sort_popularity:
                menu.findItem(id).setChecked(true);
                selected = id;
                moviePresenter.setSortParameter(SortParameters.POPULARITY);
                break;
            case R.id.sort_rating:
                menu.findItem(id).setChecked(true);
                selected = id;
                moviePresenter.setSortParameter(SortParameters.RATING);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.isChecked()) {
            item.setChecked(true);
            int id = item.getItemId();
            switch (id) {
                case R.id.sort_popularity:
                    selected = id;
                    moviePresenter.setSortParameter(SortParameters.POPULARITY);
                    return true;
                case R.id.sort_rating:
                    selected = id;
                    moviePresenter.setSortParameter(SortParameters.RATING);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readyToLoad = false;
        moviePresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SORT_MENU_ITEM_SELECTED, selected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        moviePresenter = presenter;
    }

    @Override
    public void displayLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void displayNewMovies(@NonNull List<Movie> movies) {
        if (errorSnackbar != null && errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }
        runOnUiThread(() -> {
            adapter.addMovies(movies);
            displayMovieView();
        });
    }

    @Override
    public void displayCachedMovies(@NonNull List<Movie> movies) {
        if (adapter.getItemCount() > 0) {
            adapter.clearMovies();
        }
        adapter.addMovies(movies);
        runOnUiThread(this::displayMovieView);
        readyToLoad = true;
    }

    @Override
    public void displayNoMovies() {
        runOnUiThread(this::displayErrorView);
        readyToLoad = true;
    }

    @Override
    public void displayClearMovies() {
        readyToLoad = true;
        adapter.clearMovies();
    }

    @Override
    public void displayLoadMoviesError() {
        runOnUiThread(() -> {
            if (errorSnackbar != null && errorSnackbar.isShown()) {
                errorSnackbar.dismiss();
            }
            errorSnackbar = Snackbar.make(errorView, R.string.message_connection_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, v -> moviePresenter.loadMovies());
            errorSnackbar.show();
        });
        readyToLoad = true;
    }

    @Override
    public void displayMovieDetailActivity(@NonNull final Movie requestedMovie) {
        Intent intent = new Intent(MovieActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, requestedMovie);
        intent.putExtra(EXTRA_MOVIE_ID, requestedMovie.getId());
        startActivity(intent);
    }

    private void displayMovieView() {
        errorView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayErrorView() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    /*
        Detects when user has scrolled to the bottom portion of the recyclerview,
        after which a call to load more movies is executed to keep continuous scrolling.
    */
    private class MovieScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                int currentItemCount = recyclerView.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int previousItemCount = layoutManager.findFirstVisibleItemPosition();
                int spanCount = layoutManager.getSpanCount();

                if (readyToLoad) {
                    if ((currentItemCount + previousItemCount + spanCount) >= totalItemCount) {
                        readyToLoad = false;
                        moviePresenter.loadMovies();
                    }
                }

            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
