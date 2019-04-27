package com.boxnotfound.popularmovies.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.detail.DetailActivity;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.SortParameters;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    private static final String SORT_MENU_ITEM_SELECTED = "sort_menu_item_selected";

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
        adapter = new MovieAdapter(this);
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

    //TODO is this necessary since I'm instantiating the moviePresenter
    //in onCreate?
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
        adapter.addMovies(movies);
        runOnUiThread(this::displayMovieView);
        readyToLoad = true;
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
    public void displayMovieDetailActivity(final long movieId) {
        Intent intent = new Intent(MovieActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
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

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

        private Context context;
        private ArrayList<Movie> movieList;

        public MovieAdapter(@NonNull final Context context) {
            this.context = context;
            movieList = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context parentContext = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(parentContext);

            View movieView = inflater.inflate(R.layout.movie_rv_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(movieView);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
            Log.d(LOG_TAG, "onBindViewHolder called!");
            final Movie movie = movieList.get(position);
            String posterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getPosterPath());
            Picasso.get().load(posterUrl)
                    .resize(layoutManager.getTargetItemWidth(), layoutManager.getTargetItemHeight())
                    .centerCrop()
                    .into(viewHolder.posterImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(LOG_TAG, "Poster for " + movie.getTitle() + " successfully loaded!");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(LOG_TAG, "Poster for " + movie.getTitle() + " not loaded!");
                        }
                    });
        }

        @Override
        public int getItemCount() {
            if (movieList != null) {
                return movieList.size();
            } else {
                return 0;
            }
        }

        public void addMovies(@NonNull final List<Movie> movies) {
            int currentIndex = getItemCount();
            movieList.addAll(movies);
            Log.d(LOG_TAG, "Current adapter movieList size: " + movieList.size());
            runOnUiThread(() -> notifyItemRangeInserted(currentIndex, movies.size()));
        }

        public void clearMovies() {
            if (movieList != null) {
                movieList.clear();
                notifyDataSetChanged();
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView posterImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                posterImage = itemView.findViewById(R.id.iv_movie_poster);
                posterImage.getLayoutParams().height = layoutManager.getTargetItemHeight();
                posterImage.getLayoutParams().width = layoutManager.getTargetItemWidth();
                posterImage.setPadding(layoutManager.getTargetItemHorizontalPadding(),
                        0, layoutManager.getTargetItemHorizontalPadding(), 0);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int index = getAdapterPosition();
                moviePresenter.openMovieDetails(movieList.get(index));
            }
        }
    }

    /*
        Create custom GridLayoutManager that dynamically provides target width and height
        values to poster ImageViews in order to properly fit in the layout space,
        given a set spanCount.
    */
    private class MovieGridLayoutManager extends GridLayoutManager {

        private int targetItemWidth;
        private int targetItemHeight;
        private int targetItemHorizontalPadding;
        private static final double ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION = 1.5;
        private static final int SPAN_COUNT_PORTRAIT = 2;
        private static final int SPAN_COUNT_LANDSCAPE = 4;

        public MovieGridLayoutManager(final Context context, final int screenWidthInPx) {
            super(context, 2); // spanCount must be set here, but will be adjusted shortly...
            setTargetSpanCount();
            setTargetItemWidth(screenWidthInPx);
            setTargetItemHeight();
            setTargetItemHorizontalPadding(screenWidthInPx);
        }

        /*
            Set the grid item's width and appropriate span count according
            to the current screen width, the maximum span size requested
            by the user.
        */
        private void setTargetItemWidth(final int screenWidthInPx) {
            int totalWidth = getHorizontalLayoutSpace(screenWidthInPx);
            targetItemWidth = totalWidth / getSpanCount();
        }

        /*
            With the item width now set, scale the height according to the constant
            aspect ratio in order to maintain proper image size proportions.
        */
        private void setTargetItemHeight() {
            targetItemHeight = (int) (targetItemWidth * ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION);
        }

        private void setTargetItemHorizontalPadding(final int screenWidthInPx) {
            int screenSizeRemaining = screenWidthInPx - (getTargetItemWidth() * getSpanCount());
            if (screenSizeRemaining == 0) {
                targetItemHorizontalPadding = 0;
            } else {
                targetItemHorizontalPadding = 2 / (screenWidthInPx - (getTargetItemWidth() * getSpanCount()));
            }
        }

        private void setTargetSpanCount() {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setSpanCount(SPAN_COUNT_PORTRAIT);
            } else {
                setSpanCount(SPAN_COUNT_LANDSCAPE);
            }
        }

        public int getTargetItemWidth() {
            return targetItemWidth;
        }

        public int getTargetItemHeight() {
            return targetItemHeight;
        }

        public int getTargetItemHorizontalPadding() {
            return targetItemHorizontalPadding;
        }

        private int getHorizontalLayoutSpace(final int screenWidthInPx) {
            return screenWidthInPx - getPaddingRight() - getPaddingLeft();
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e(LOG_TAG, "onLayoutChildren error: " + e.getMessage());
            }
        }
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
