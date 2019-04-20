package com.boxnotfound.popularmovies.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.boxnotfound.popularmovies.R;
import com.boxnotfound.popularmovies.movies.MovieActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            movieId = bundle.getLong(MovieActivity.EXTRA_MOVIE_ID);
        }

    }
}
