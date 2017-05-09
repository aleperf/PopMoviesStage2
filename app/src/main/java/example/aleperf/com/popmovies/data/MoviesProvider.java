package example.aleperf.com.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import example.aleperf.com.popmovies.data.MoviesContract.FavoriteMoviesEntry;
import example.aleperf.com.popmovies.data.MoviesContract.MostPopuplarMoviesEntry;
import example.aleperf.com.popmovies.data.MoviesContract.TopRatedMoviesEntry;


/**
 * Execute CRUD operations on the 3 tables : Most Popular, Top Rated and Favorites
 */

public class MoviesProvider extends ContentProvider {

    public static final int CODE_MOST_POPULAR = 100;
    public static final int CODE_TOP_RATED = 200;
    public static final int CODE_FAVORITES = 300;
    public static final int CODE_MOST_POPULAR_WITH_MOVIE_ID = 101;
    public static final int CODE_TOP_RATED_WITH_MOVIE_ID = 201;
    public static final int CODE_FAVORITES_WITH_MOVIE_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOST_POPULAR, CODE_MOST_POPULAR);
        matcher.addURI(authority, MoviesContract.PATH_MOST_POPULAR + "/#", CODE_MOST_POPULAR_WITH_MOVIE_ID);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED, CODE_TOP_RATED);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED + "/#", CODE_TOP_RATED_WITH_MOVIE_ID);
        matcher.addURI(authority, MoviesContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, MoviesContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_MOVIE_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tableName;
        String whereClause = selection;
        String[] selectionArguments = selectionArgs;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOST_POPULAR:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                break;

            case CODE_MOST_POPULAR_WITH_MOVIE_ID:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                whereClause = MostPopuplarMoviesEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;

            case CODE_TOP_RATED:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                break;

            case CODE_TOP_RATED_WITH_MOVIE_ID:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                whereClause = TopRatedMoviesEntry.COLUMN_MOVIE_ID + " =?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;

            case CODE_FAVORITES:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                break;

            case CODE_FAVORITES_WITH_MOVIE_ID:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                whereClause = FavoriteMoviesEntry.COLUMN_MOVIE_ID + " =?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        Cursor cursor = mOpenHelper.getReadableDatabase().query(
                tableName,
                projection,
                whereClause,
                selectionArguments,
                null,
                null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_MOST_POPULAR:
                return MostPopuplarMoviesEntry.CONTENT_LIST_TYPE;
            case CODE_MOST_POPULAR_WITH_MOVIE_ID:
                return MostPopuplarMoviesEntry.CONTENT_ITEM_TYPE;
            case CODE_TOP_RATED:
                return TopRatedMoviesEntry.CONTENT_LIST_TYPE;
            case CODE_TOP_RATED_WITH_MOVIE_ID:
                return TopRatedMoviesEntry.CONTENT_ITEM_TYPE;
            case CODE_FAVORITES:
                return FavoriteMoviesEntry.CONTENT_LIST_TYPE;
            case CODE_FAVORITES_WITH_MOVIE_ID:
                return FavoriteMoviesEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long id;
        switch (match) {
            case CODE_MOST_POPULAR:
                id = db.insert(MostPopuplarMoviesEntry.TABLE_NAME, null, values);
                break;
            case CODE_TOP_RATED:
                id = db.insert(TopRatedMoviesEntry.TABLE_NAME, null, values);
                break;
            case CODE_FAVORITES:
                id = db.insert(FavoriteMoviesEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
        if (id == -1) {
            return null;
        }
        // Notify all listeners that the data has changed for the content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String tableName;
        String whereClause = selection;
        String[] selectionArguments = selectionArgs;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOST_POPULAR:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                break;
            case CODE_MOST_POPULAR_WITH_MOVIE_ID:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                whereClause = MostPopuplarMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_TOP_RATED:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED_WITH_MOVIE_ID:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                whereClause = TopRatedMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_FAVORITES:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                break;
            case CODE_FAVORITES_WITH_MOVIE_ID:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                whereClause = FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        int rowsDeleted = db.delete(tableName, whereClause, selectionArguments);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String tableName;
        String whereClause = selection;
        String[] selectionArguments = selectionArgs;

        switch (match) {
            case CODE_MOST_POPULAR:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                break;
            case CODE_MOST_POPULAR_WITH_MOVIE_ID:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                whereClause = MostPopuplarMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_TOP_RATED:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED_WITH_MOVIE_ID:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                whereClause = TopRatedMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_FAVORITES:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                break;
            case CODE_FAVORITES_WITH_MOVIE_ID:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                whereClause = FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArguments = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

        int rowsUpdated = db.update(tableName, contentValues, whereClause, selectionArguments);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String tableName;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOST_POPULAR:
                tableName = MostPopuplarMoviesEntry.TABLE_NAME;
                break;
            case CODE_TOP_RATED:
                tableName = TopRatedMoviesEntry.TABLE_NAME;
                break;
            case CODE_FAVORITES:
                tableName = FavoriteMoviesEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("BulkInsert is not supported for " + uri);
        }

        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;

    }

}
