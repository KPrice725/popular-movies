package com.boxnotfound.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
***REMOVED***

import android.os.Bundle;
import android.view.View;

***REMOVED***
import com.boxnotfound.popularmovies.model.source.RemoteMovieDataSource;

public class MovieActivity extends AppCompatActivity {

***REMOVED***
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
***REMOVED***

    public void testNetwork(View view) {
        Request request = RemoteMovieDataSource.buildRequest(SortParameters.POPULARITY);
        RemoteMovieDataSource.getJsonFromRequest(request);
***REMOVED***
***REMOVED***
