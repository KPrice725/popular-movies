package com.boxnotfound.popularmovies.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.utilities.MoviePosterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> movieList;
    private MovieContract.Presenter presenter;
    private MovieGridLayoutManager layoutManager;

    MovieAdapter(@NonNull final MovieContract.Presenter presenter,
                 @NonNull final MovieGridLayoutManager layoutManager) {
        movieList = new ArrayList<>();
        this.presenter = presenter;
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View movieView = inflater.inflate(R.layout.movie_rv_item, parent, false);

        return new ViewHolder(movieView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.ViewHolder viewHolder, final int position) {
        final Movie movie = movieList.get(position);
        String posterUrl = MoviePosterUtils.getLargeMoviePosterUrlPath(movie.getPosterPath());
        Picasso.get().load(posterUrl)
                .resize(layoutManager.getTargetItemWidth(), layoutManager.getTargetItemHeight())
                .centerCrop()
                .into(viewHolder.posterImage);
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }

    void addMovies(@NonNull final List<Movie> movies) {
        int currentIndex = getItemCount();
        movieList.addAll(movies);
        notifyItemRangeInserted(currentIndex, movies.size());
    }

    void clearMovies() {
        if (movieList != null) {
            movieList.clear();
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImage;

        ViewHolder(@NonNull View itemView) {
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
            presenter.openMovieDetails(movieList.get(index));
        }
    }
}
