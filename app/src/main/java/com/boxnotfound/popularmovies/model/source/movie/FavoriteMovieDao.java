package com.boxnotfound.popularmovies.model.source.movie;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.boxnotfound.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(@NonNull final Movie movie);

    @Delete
    void deleteFavoriteMovie(@NonNull final Movie movie);

    @Query("SELECT * FROM favorite_movie_table")
    List<Movie> getAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movie_table WHERE id = :id")
    Movie getMovieById(final long id);
}
