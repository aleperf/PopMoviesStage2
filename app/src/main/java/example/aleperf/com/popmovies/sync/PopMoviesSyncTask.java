package example.aleperf.com.popmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.data.MoviesContract;
import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;


/**
 * Sync the database with the online data
 * Load fresh data for the Top Rated and Most Popular Tables
 *
 *
 */

public class PopMoviesSyncTask {

    //max quantity of pages loaded in the initial sync of database
    public static final int MAX_NUM_PAGES = 3;
    //constants used to decide which url to build
    private static final String QUERY_MOST_POPULAR = "query most popular";
    private static final String QUERY_TOP_RATED = "query top rated";

    synchronized public static void syncPopMoviesDatabase(Context context) {

        try {
            List<URL> mostPopularUrls = NetworkUtils.getUrlsToLoad(MAX_NUM_PAGES, QUERY_MOST_POPULAR);
            List<URL> topRatedUrls = NetworkUtils.getUrlsToLoad(MAX_NUM_PAGES, QUERY_TOP_RATED);

            List<String> jsonResponseMostPopular = NetworkUtils.getJsonStringListFromHttpsUrls(mostPopularUrls);
            List<String> jsonResponseTopRated = NetworkUtils.getJsonStringListFromHttpsUrls(topRatedUrls);

            //clear previous data if there is a response
            ContentResolver contentResolver = context.getContentResolver();
            if (jsonResponseMostPopular.size() > 0 && jsonResponseMostPopular.get(0) != null) {
                contentResolver.delete(
                        MoviesContract.MostPopuplarMoviesEntry.CONTENT_URI, null, null);
            }


            for (int i = 0; i < MAX_NUM_PAGES; i++) {

                ContentValues[] mostPopularCV =
                        JSONUtils.getContentValuesFromJson(jsonResponseMostPopular.get(i));
                if (mostPopularCV != null && mostPopularCV.length > 0) {
                    contentResolver
                            .bulkInsert(MoviesContract.MostPopuplarMoviesEntry.CONTENT_URI, mostPopularCV);
                }


                ContentValues[] topRatedCV = JSONUtils.
                        getContentValuesFromJson(jsonResponseTopRated.get(i));
                if (topRatedCV != null && topRatedCV != null) {
                    contentResolver.bulkInsert(MoviesContract.TopRatedMoviesEntry.CONTENT_URI, topRatedCV);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
