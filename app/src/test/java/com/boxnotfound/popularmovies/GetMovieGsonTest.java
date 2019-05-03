package com.boxnotfound.popularmovies;

import com.boxnotfound.popularmovies.model.Genre;
import com.boxnotfound.popularmovies.model.Movie;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import androidx.annotation.NonNull;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetMovieGsonTest {

    /*
    Test to ensure Gson deserialization of JSON responses for get most by ID are properly handled
    */

    // Sample response from TMDB's API's call to get movie by ID
    private final String SAMPLE_GET_MOVIE_JSON_RESPONSE = "{\"adult\":false,\"backdrop_path\":\"/4iJfYYoQzZcONB9hNzg0J0wWyPH.jpg\",\"belongs_to_collection\":{\"id\":10,\"name\":\"Star Wars Collection\",\"poster_path\":\"/iTQHKziZy9pAAY4hHEDCGPaOvFC.jpg\",\"backdrop_path\":\"/d8duYyyC9J5T825Hg7grmaabfxQ.jpg\"},\"budget\":11000000,\"genres\":[{\"id\":12,\"name\":\"Adventure\"},{\"id\":28,\"name\":\"Action\"},{\"id\":878,\"name\":\"Science Fiction\"}],\"homepage\":\"http://www.starwars.com/films/star-wars-episode-iv-a-new-hope\",\"id\":11,\"imdb_id\":\"tt0076759\",\"original_language\":\"en\",\"original_title\":\"Star Wars\",\"overview\":\"Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire.\",\"popularity\":38.71,\"poster_path\":\"/btTdmkgIvOi0FFip1sPuZI2oQG6.jpg\",\"production_companies\":[{\"id\":1,\"logo_path\":\"/o86DbpburjxrqAzEDhXZcyE8pDb.png\",\"name\":\"Lucasfilm\",\"origin_country\":\"US\"},{\"id\":25,\"logo_path\":\"/qZCc1lty5FzX30aOCVRBLzaVmcp.png\",\"name\":\"20th Century Fox\",\"origin_country\":\"US\"}],\"production_countries\":[{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"1977-05-25\",\"revenue\":775398007,\"runtime\":121,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"A long time ago in a galaxy far, far away...\",\"title\":\"Star Wars\",\"video\":false,\"vote_average\":8.2,\"vote_count\":11248}";
    private Movie movie;
    private Gson gson;

    @Before
    public void setup() {
        gson = new Gson();
        movie = gson.fromJson(SAMPLE_GET_MOVIE_JSON_RESPONSE, Movie.class);
    }

    @After
    public void cleanup() {
        gson = null;
        movie = null;
    }

    @Test
    public void checkGson() {
        assertThat(gson, notNullValue());
    }

    @Test
    public void checkMovie() {
        assertMovie(movie);
    }

    private void assertMovie(@NonNull final Movie movie) {
        assertThat(movie, notNullValue());
        assertThat(movie.getId(), is(11L));
        assertThat(movie.getUserRating(), is(8.2));
        assertThat(movie.getTitle(), is("Star Wars"));
        assertThat(movie.getOriginalTitle(), is("Star Wars"));
        assertThat(movie.getOverview(), is("Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire."));
        assertThat(movie.getReleaseDate(), is("1977-05-25"));
        assertThat(movie.getPosterPath(), is("/btTdmkgIvOi0FFip1sPuZI2oQG6.jpg"));
        assertThat(movie.getBackdropPosterPath(), is("/4iJfYYoQzZcONB9hNzg0J0wWyPH.jpg"));
        assertThat(movie.getGenreList(), notNullValue());
        Genre genre1 = movie.getGenreList().get(0);
        Genre genre2 = movie.getGenreList().get(1);
        Genre genre3 = movie.getGenreList().get(2);
        assertThat(genre1.getId(), is(12));
        assertThat(genre1.getName(), is("Adventure"));
        assertThat(genre2.getId(), is(28));
        assertThat(genre2.getName(), is("Action"));
        assertThat(genre3.getId(), is(878));
        assertThat(genre3.getName(), is("Science Fiction"));
    }
}
