package example.aleperf.com.popmovies.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import example.aleperf.com.popmovies.data.MoviesContract.FavoriteMoviesEntry;

import example.aleperf.com.popmovies.Movie;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * FavoritesIntenteService  insert or delete an entry in the Favorites Table
 * Called when the user favorite or unfavorites a movie in the MovieDetailFragment
 */
public class FavoritesIntentService extends IntentService {

    public static final String ACTION_INSERT_FAV = "example.aleperf.com.popmovies.data.action.INSERT_FAV";
    public static final String ACTION_DELETE_FAV = "example.aleperf.com.popmovies.data.action.DELETE_FAV";


    public static final String EXTRA_MOVIE_FAV = "example.aleperf.com.popmovies.data.extra.movieFav";
    public static final String EXTRA_MOVIE_FAV_ID = "example.aleperf.com.popmovies.data.extra.movieFavID";


    public FavoritesIntentService() {
        super("FavoritesIntentService");
    }

    /**
     * Start this service to insert a Movie in the Favorite Movie Table
     *
     * @param context the context from which this service has been started
     * @param movie   a Movie object  containing all the data to create a contentValue object
     *                to insert in the Favorites table
     */

    public static void startActionInsertFav(Context context, Movie movie) {
        Intent intent = new Intent(context, FavoritesIntentService.class);
        intent.setAction(ACTION_INSERT_FAV);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_FAV, movie);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    /**
     * Start this service to insert a Movie in the Favorite Movie Table
     *
     * @param context the context from which this service has been started
     * @param movieId a string uniquely identifying a movie to delete in the Favorites Movie Table
     */

    public static void startActionDeleteFav(Context context, String movieId) {
        Intent intent = new Intent(context, FavoritesIntentService.class);
        intent.setAction(ACTION_DELETE_FAV);
        intent.putExtra(EXTRA_MOVIE_FAV_ID, movieId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT_FAV.equals(action)) {
                Bundle bundle = intent.getExtras();
                Movie movie = bundle.getParcelable(EXTRA_MOVIE_FAV);
                handleActionInsert(movie);
            } else if (ACTION_DELETE_FAV.equals(action)) {
                String movieId = intent.getStringExtra(EXTRA_MOVIE_FAV_ID);
                handleActionDelete(movieId);
            }
        }
    }

    /**
     * Insert a movie record in the FavoriteMovieEntry table
     *
     * @param movie the movie object with the record to insert
     */
    private void handleActionInsert(Movie movie) {
        //extract data from movie
        String movieId = movie.getId();
        String originalTitle = movie.getOriginalTitle();
        String title = movie.getTitle();
        String posterPath = movie.getPosterPath();
        String plotSynopsis = movie.getPlotSynopsis();
        double rating = movie.getRating();
        long releaseDate = movie.getReleaseDate();
        //create a content values object and fill it with column fields
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_ID, movieId);
        values.put(FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        values.put(FavoriteMoviesEntry.COLUMN_TITLE, title);
        values.put(FavoriteMoviesEntry.COLUMN_POSTER_PATH, posterPath);
        values.put(FavoriteMoviesEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);
        values.put(FavoriteMoviesEntry.COLUMN_RATING, rating);
        values.put(FavoriteMoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
        //call the insert method of the  provider
        Uri uri = getApplicationContext().getContentResolver().insert(FavoriteMoviesEntry.CONTENT_URI, values);



    }

    /**
     * Handle action delete in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDelete(String movieId) {

        Uri uri = FavoriteMoviesEntry.CONTENT_URI.buildUpon().appendPath(movieId).build();
        int num = getApplicationContext().getContentResolver().delete(uri, null, null);


    }
}
