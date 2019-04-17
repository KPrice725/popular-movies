package com.boxnotfound.popularmovies.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    private MovieContract.Presenter moviePresenter;

    private MovieGridLayoutManager layoutManager;

    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        moviePresenter = new MoviePresenter(
                MovieRepository.getInstance(RemoteMovieDataSource.getInstance()),
                this);
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        int screenWidthInPx = Resources.getSystem().getDisplayMetrics().widthPixels;
        layoutManager = new MovieGridLayoutManager(this, 4, screenWidthInPx);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviePresenter.start();
    }

    //TODO is this necessary since I'm instantiating the moviePresenter
    //in onCreate?
    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        moviePresenter = presenter;
    }

    @Override
    public void displayLoadingIndicator(boolean isLoading) {

    }

    @Override
    public void displayNewMovies(@NonNull List<Movie> movies) {
        adapter.addMovies(movies);
    }

    @Override
    public void displayNoMovies() {

    }

    @Override
    public void displayLoadMoviesError() {

    }

    @Override
    public void displayMovieDetailActivity(final long movieId) {

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
                    .fit()
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

        private class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView posterImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                posterImage = itemView.findViewById(R.id.iv_movie_poster);
                posterImage.getLayoutParams().height = layoutManager.getTargetItemHeight();
                posterImage.getLayoutParams().width = layoutManager.getTargetItemWidth();
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
        private int itemWidthTargetWindow = 270;
        private static final double ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION = 1.5;
        private int maxSpanCount;

        public MovieGridLayoutManager(final Context context, final int maxSpanCount, final int screenWidthInPx) {
            super(context, maxSpanCount); // spanCount must be set here, but will be adjusted shortly...
            this.maxSpanCount = maxSpanCount;
            setTargetItemWidth(screenWidthInPx);
            setTargetItemHeight();
        }

        /*
            Set the grid item's width and appropriate span count according
            to the current screen width, the maximum span size requested
            by the user.
        */
        private void setTargetItemWidth(final int screenWidthInPx) {
            int totalWidth = getHorizontalLayoutSpace(screenWidthInPx);
            int newSpanCount = calculateAppropriateSpanCount(totalWidth);
            setSpanCount(newSpanCount);
            targetItemWidth = totalWidth / newSpanCount;
        }

        /*
            With the item width now set, scale the height according to the constant
            aspect ratio in order to maintain proper image size proportions.
        */
        private void setTargetItemHeight() {
            targetItemHeight = (int) (targetItemWidth * ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION);
        }

        /*
            Determine the appropriate span size for the given screen width and a general
            target width size.  If the calculated span exceeds the set max span size,
            gradually scale the target width size.
            TODO: This could likely be a little bit cleaner, look into improving this.
        */
        private int calculateAppropriateSpanCount(int totalWidth) {
            if (totalWidth <= itemWidthTargetWindow) {
                return 1;
            }
            int count = 0;
            int widthHolder = totalWidth;
            while (widthHolder - itemWidthTargetWindow > 0) {
                count++;
                widthHolder -= itemWidthTargetWindow;
                if (count > maxSpanCount) {
                    count = 0;
                    widthHolder = totalWidth;
                    itemWidthTargetWindow = (int) (itemWidthTargetWindow * 1.25);
                }

            }
            return count;
        }

        public int getTargetItemWidth() {
            return targetItemWidth;
        }

        public int getTargetItemHeight() {
            return targetItemHeight;
        }

        private int getHorizontalLayoutSpace(final int screenWidthInPx) {
            return screenWidthInPx - getPaddingRight() - getPaddingLeft();
        }
    }
}
