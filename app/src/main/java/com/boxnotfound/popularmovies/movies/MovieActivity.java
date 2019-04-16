package com.boxnotfound.popularmovies.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.MovieRepository;
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private MovieContract.Presenter moviePresenter;

    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        moviePresenter = new MoviePresenter(
                MovieRepository.getInstance(RemoteMovieDataSource.getInstance()),
                this);
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        adapter = new MovieAdapter(this);
        MovieGridLayoutManager layoutManager = new MovieGridLayoutManager(this, 150);
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
            final Movie movie = movieList.get(position);
            // TODO
            String posterUrl = MoviePosterUtils.getSmallMoviePosterUrlPath(movie.getPosterPath());
            Picasso.get().load(posterUrl).into(viewHolder.posterImage);
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
            runOnUiThread(() -> notifyItemRangeInserted(currentIndex, movies.size()));
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView posterImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                posterImage = itemView.findViewById(R.id.iv_movie_poster);
            }
        }
    }

    /*
        Solution to handling auto-setting the GridLayoutManager's spanCount
        https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
    */
    private class MovieGridLayoutManager extends GridLayoutManager {

        private int columnWidth;
        private boolean columnWidthChanged = true;

        public MovieGridLayoutManager(Context context, int colWidth) {
            super(context, 1);
            setColumnWidth(checkedColumnWidth(context, colWidth));
        }

        public MovieGridLayoutManager(Context context, int colWidth, int orientation, boolean reverseLayout) {
            super(context, 1, orientation, reverseLayout);
            setColumnWidth(checkedColumnWidth(context, colWidth));
        }

        private int checkedColumnWidth(Context context, int colWidth) {
            if (colWidth <= 0) {
                colWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        context.getResources().getDisplayMetrics());
            }
            return colWidth;
        }

        public void setColumnWidth(int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
                columnWidth = newColumnWidth;
                columnWidthChanged = true;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int width = getWidth();
            int height = getHeight();
            if (columnWidthChanged && columnWidth > 0 && width > 0 && height > 0) {
                int totalSpace;
                if (getOrientation() == RecyclerView.VERTICAL) {
                    totalSpace = width - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = height - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / columnWidth);
                setSpanCount(spanCount);
                columnWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }
    }
}
