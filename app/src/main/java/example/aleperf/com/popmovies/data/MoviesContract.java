package example.aleperf.com.popmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;



public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "example.aleperf.com.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_MOST_POPULAR = "most_popular";
    public static final String PATH_TOP_RATED = "top_rated";

    /**
     * Base class used to model the tables in in the database.
     * Defines the the columns present in every table.
     */
    public abstract static class MovieEntry implements BaseColumns {

        // BASIC COLUMNS

        /**
         * movie id returned by the API used to unique identify this movie and
         * build the path to reviews and trailer about the movie
         **/
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Title in the original language of the movie
         */
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        /**
         * Title in the default language of the app
         **/
        public static final String COLUMN_TITLE = "movie_title";

        /**
         * path to the  poster of the movie
         **/
        public static final String COLUMN_POSTER_PATH = "poster_path";
        /**
         * path to the backdrop poster for the movie detail.
         **/

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        /**
         * synopsis of the movie
         **/
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";

        /**
         * average vote of users
         */
        public static final String COLUMN_RATING = "movie_rating";

        /**
         * release date of the movie
         **/
        public static final String COLUMN_RELEASE_DATE = "release_date";




    }

    /**
     * Table representing the Movies marked as favorites by the user
     */
    public static final class FavoriteMoviesEntry extends MovieEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        /**
         * Favorites table name
         **/
        public static final String TABLE_NAME = "favorites";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of favorite movies.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

    }

    /**
     * Table representing the Most Popular Movies
     */

    public static final class MostPopuplarMoviesEntry extends MovieEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOST_POPULAR)
                .build();

        /**
         * Most Popular Table Name
         **/
        public static final String TABLE_NAME = "most_popular";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of most popular movies.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POPULAR;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POPULAR;
    }

    /**
     * Table representing the Top Rated Movies
     */

    public static final class TopRatedMoviesEntry extends MovieEntry {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();

        /**
         * Most Popular Table Name
         **/
        public static final String TABLE_NAME = "top_rated";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of favorite movies.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;


    }


}
