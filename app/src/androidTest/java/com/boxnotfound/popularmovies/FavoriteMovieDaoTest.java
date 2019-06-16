package com.boxnotfound.popularmovies;


import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.boxnotfound.popularmovies.model.Movie;
import com.boxnotfound.popularmovies.model.source.AppDatabase;
import com.boxnotfound.popularmovies.model.source.movie.FavoriteMovieDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FavoriteMovieDaoTest {

    private AppDatabase db;
    private FavoriteMovieDao favoriteMovieDao;
    private Movie movie;
    private long id = 1337;
    private String posterPath = "thisIsMyPosterPath";

    @Before
    public void setup() {
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class).build();
        favoriteMovieDao = db.favoriteMovieDao();
        movie = new Movie(id, posterPath);
    }

    @After
    public void cleanup() {
        movie = null;
        favoriteMovieDao = null;
        db.close();
    }

    @Test
    public void checkDao() {
        assertThat(favoriteMovieDao, notNullValue());
    }

    @Test
    public void insertMovieAndGetList() {
        favoriteMovieDao.insertFavoriteMovie(movie);
        final List<Movie> loadedMovies = favoriteMovieDao.getAllFavoriteMovies();
        final Movie loadedMovie = loadedMovies.get(0);
        assertThat(loadedMovie.getId(), is(id));
        assertThat(loadedMovie.getPosterPath(), is(posterPath));
    }

    @Test
    public void insertMovie_DeleteMovie_GetEmptyList() {
        favoriteMovieDao.insertFavoriteMovie(movie);
        assertThat(favoriteMovieDao.getAllFavoriteMovies().size(), is(1));
        favoriteMovieDao.deleteFavoriteMovie(movie);
        assertThat(favoriteMovieDao.getAllFavoriteMovies().size(), is(0));
    }

    @Test
    public void getNullMovieById() {
        Movie nullMovie = favoriteMovieDao.getMovieById(-1);
        assert(nullMovie == null);
    }

    @Test
    public void getNonNullMovieById() {
        favoriteMovieDao.insertFavoriteMovie(movie);
        Movie nonNullMovie = favoriteMovieDao.getMovieById(id);
        assert(nonNullMovie != null);
    }

    @Test
    public void deleteMovieThatDoesNotExistInTable() {
        favoriteMovieDao.deleteFavoriteMovie(movie);
    }
}
