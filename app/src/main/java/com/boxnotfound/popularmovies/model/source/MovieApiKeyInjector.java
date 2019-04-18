package com.boxnotfound.popularmovies.model.source;

public class MovieApiKeyInjector {

    /*
       API_KEY deliberately blank to respect TMDB's API Key security
       and privacy requirements.  The end user should paste their
       own personal API key to this constant to utilize the TMDB functionality.
   */
    private static final String API_KEY = "";

    public static String inject() {
        return API_KEY;
    }
}
