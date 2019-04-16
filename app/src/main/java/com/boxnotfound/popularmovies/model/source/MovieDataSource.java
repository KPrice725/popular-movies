***REMOVED***

***REMOVED***
***REMOVED***

***REMOVED***

***REMOVED***

public interface MovieDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(@NonNull List<Movie> movies);

        void onMoviesNotAvailable();
***REMOVED***

    void getMoreMovies(@NonNull final SortParameters sortParameter, final int pageNumber, @NonNull final LoadMoviesCallback callback);

***REMOVED***
