package com.boxnotfound.popularmovies.model.source.video;

import androidx.annotation.NonNull;

import com.boxnotfound.popularmovies.model.Video;

import java.util.List;

public interface VideoDataSource {

    interface LoadVideosCallback {

        void onVideosLoaded(@NonNull final List<Video> videos);

        void onVideosNotAvailable();
    }

    void loadVideosByMovieId(final long movieId, @NonNull final LoadVideosCallback callback);
}
