package com.boxnotfound.popularmovies.model.source.video;

import androidx.annotation.NonNull;

public class VideoRepository implements VideoDataSource {

    private static VideoRepository INSTANCE = null;

    private final VideoDataSource remoteVideoDataSource;

    private VideoRepository(@NonNull final VideoDataSource remoteVideoDataSource) {
        this.remoteVideoDataSource = remoteVideoDataSource;
    }

    public static VideoRepository getInstance(@NonNull final VideoDataSource remoteVideoDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VideoRepository(remoteVideoDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadVideosByMovieId(long movieId, @NonNull LoadVideosCallback callback) {
        remoteVideoDataSource.loadVideosByMovieId(movieId, callback);
    }
}
