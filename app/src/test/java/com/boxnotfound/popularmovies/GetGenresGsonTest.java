package com.boxnotfound.popularmovies;

import com.boxnotfound.popularmovies.model.Genre;
import com.boxnotfound.popularmovies.model.GenreJSONResult;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetGenresGsonTest {

    /*
    Test to ensure Gson deserialization of JSON responses for get genre list are properly handled
    */

    // Sample response from TMDB's API's call to get genres
    private final String SAMPLE_GET_GENRES_JSON_RESPONSE = "{\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"History\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV Movie\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]}";
    private GenreJSONResult genreJSONResult;
    private List<Genre> genreList;
    private Gson gson;

    @Before
    public void setup() {
        gson = new Gson();
        genreJSONResult = gson.fromJson(SAMPLE_GET_GENRES_JSON_RESPONSE, GenreJSONResult.class);
        genreList = genreJSONResult.getGenreList();
    }

    @After
    public void cleanup() {
        gson = null;
        genreJSONResult = null;
        genreList = null;
    }

    @Test
    public void checkGson() {
        assertThat(gson, notNullValue());
    }

    @Test
    public void checkGenreJSONResult() {
        assertThat(genreJSONResult, notNullValue());
    }

    @Test
    public void checkGenreList() {
        assertThat(genreList, notNullValue());
        assertThat(genreList.size(), is(19));
    }

    @Test
    public void checkFirstGenreFromGenreList() {
        Genre genre = genreList.get(0);
        assertThat(genre, notNullValue());
        assertThat(genre.getId(), is(28));
        assertThat(genre.getName(), is("Action"));
    }
}
