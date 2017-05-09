package example.aleperf.com.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import example.aleperf.com.popmovies.data.MoviesContract.FavoriteMoviesEntry;



public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String COLUMNS = " (" +
                FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                FavoriteMoviesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT, " +
                FavoriteMoviesEntry.COLUMN_RATING + " REAL NOT NULL DEFAULT 0, " +
                FavoriteMoviesEntry.COLUMN_RELEASE_DATE + " INTEGER, " +
                " UNIQUE (" + FavoriteMoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_MOST_POPULAR_TABLE = "CREATE TABLE " + MoviesContract.MostPopuplarMoviesEntry.TABLE_NAME + COLUMNS;
        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + MoviesContract.TopRatedMoviesEntry.TABLE_NAME + COLUMNS;
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoriteMoviesEntry.TABLE_NAME + COLUMNS;
        sqLiteDatabase.execSQL(SQL_CREATE_MOST_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MostPopuplarMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TopRatedMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
