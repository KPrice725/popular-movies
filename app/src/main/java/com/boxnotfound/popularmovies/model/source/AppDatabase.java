package com.boxnotfound.popularmovies.model.source;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.movie.FavoriteMovieDao;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoriteMovieDao favoriteMovieDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
