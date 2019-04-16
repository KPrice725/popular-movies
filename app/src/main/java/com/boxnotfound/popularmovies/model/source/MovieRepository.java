***REMOVED***

***REMOVED***

***REMOVED***
***REMOVED***

import java.util.LinkedHashMap;
***REMOVED***
import java.util.Map;

***REMOVED***

public class MovieRepository implements MovieDataSource {

    private static final String LOG_TAG = MovieRepository.class.getSimpleName();
    private static MovieRepository INSTANCE = null;

    private MovieDataSource remoteMovieDataSource;

    private Map<Long, Movie> cachedMovies;

    private MovieRepository(@NonNull MovieDataSource remoteMovieDataSource) {
        this.remoteMovieDataSource = remoteMovieDataSource;
***REMOVED***

    public static MovieRepository getInstance(@NonNull MovieDataSource remoteMovieDataSource) {
***REMOVED***
            INSTANCE = new MovieRepository(remoteMovieDataSource);
    ***REMOVED***
***REMOVED***
***REMOVED***

***REMOVED***
***REMOVED***
        remoteMovieDataSource.getMoreMovies(sortParameter, pageNumber, new LoadMoviesCallback() {
        ***REMOVED***
            public void onMoviesLoaded(@NonNull List<Movie> movies) {
***REMOVED***
                cacheMovies(movies);
        ***REMOVED***

        ***REMOVED***
            public void onMoviesNotAvailable() {
***REMOVED***
        ***REMOVED***
    ***REMOVED***);
***REMOVED***

    private void cacheMovies(@NonNull List<Movie> movies) {
        if (cachedMovies == null) {
            cachedMovies = new LinkedHashMap<>();
    ***REMOVED***
        cacheMovies(movies);
        for (Movie movie : movies) {
            cachedMovies.put(movie.getId(), movie);
    ***REMOVED***
***REMOVED***

    public Movie getMovieById(final long id) throws IllegalArgumentException {
        if (cachedMovies.containsKey(id)) {
            return cachedMovies.get(id);
    ***REMOVED*** else {
            Log.e(LOG_TAG, "Invalid movie id: " + id);
            throw new IllegalArgumentException("Invalid movie id: " + id);
    ***REMOVED***
***REMOVED***

    public void clearMovieCache() {
        if (cachedMovies != null) {
            cachedMovies.clear();
    ***REMOVED***
***REMOVED***
***REMOVED***
