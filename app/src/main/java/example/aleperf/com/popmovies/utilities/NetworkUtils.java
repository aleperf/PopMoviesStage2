package example.aleperf.com.popmovies.utilities;


import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import example.aleperf.com.popmovies.BuildConfig;


/**
 * Provides static CONSTANTS and  methods to build URLs for querying the TheMovieDb
 * <p>
 * This class requires a valid API KEY to query TheMovieDb, the API KEY is build from
 * the constant THE_MOVIE_DB_API_KEY in the gradle.properties file.
 * In order to make this class work, include this line of code in your
 * gradle.properties file in the gradle folder,
 * putting a valid API KEY between quotes:
 * THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
 * <p>
 * your can request a valid API KEY to query TheMovieDb here:
 * https://www.themoviedb.org/documentation/api
 * <p>
 * If your are sharing this code somewhere, please remember to hide your API KEY and put
 * the build.gradle file in the .gitignore file, or take whatever measures necessary to prevent
 * your API KEY to be shared publicly, as required by the TheMovieDb terms of use.
 */

public class NetworkUtils {

    //constants used to build the query to TheMovieDB

    private static final String MOVIE_DB_DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String REVIEWS_DIR = "reviews";
    private static final String VIDEO_DIR = "videos";
    //TODO: insert the following line of code, with a valid API KEY between the quotes,
    // in the gradle.properties file:
    //  THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
    private static final String MOVIE_DB_API_KEY = BuildConfig.MY_API_KEY;
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_PAGE = "page";
    //add a filter to limit high rated, but too little known movies, based on number of ratings
    // VOTE_COUNT_GTE should be greater then or equal to 200
    private static final String PARAM_VOTE_COUNT_GTE = "vote_count.gte";
    private static final String VOTE_COUNT_GTE = "200";

    private static final String SORT_POPULARITY_DESC = "popularity.desc";
    private static final String SORT_TOP_RATED_DESC = "vote_average.desc";

    //constants used to decide which url to build
    private static final String QUERY_MOST_POPULAR = "query most popular";
    private static final String QUERY_TOP_RATED = "query top rated";

    //constants used to build a URL for requesting an image
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_FORMAT_W185 = "w185";

    //Constants used to query Youtube
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAM = "v";


    /**
     * Build an URL based on the match of queryRequest to predefined constants
     *
     * @param queryRequest a String corresponding to a predefined constant in NetworkUtils
     * @return an URL of "theMovieDB" corresponding to the queryRequest.
     */

    @Nullable
    public static URL buildMoviesQueryURL(String queryRequest, int page) {
        Uri requestUri = buildMoviesQueryUri(queryRequest, page);
        return convertUriToURL(requestUri);

    }

    /**
     * Convert an Uri to the corresponding URL
     *
     * @param requestUri
     * @return a URL corresponding to the @param requestUri
     */

    private static URL convertUriToURL(Uri requestUri) {
        try {
            URL url = new URL(requestUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Build an Uri for "TheMovieDB" based on the the match of queryRequest to predefined constants,
     * if no match is found return the uri for the most popular movies
     *
     * @param queryRequest a string  defining the type of uri request
     * @return an uri corresponding to the type of request
     */
    private static Uri buildMoviesQueryUri(String queryRequest, int page) {

        Uri.Builder builder = Uri.parse(MOVIE_DB_DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_VOTE_COUNT_GTE, VOTE_COUNT_GTE);

        switch (queryRequest) {
            case QUERY_MOST_POPULAR:
                builder = builder.appendQueryParameter(PARAM_SORT_BY, SORT_POPULARITY_DESC);
                break;

            case QUERY_TOP_RATED:
                builder = builder.appendQueryParameter(PARAM_SORT_BY, SORT_TOP_RATED_DESC);
                break;

            default:
                builder = builder.appendQueryParameter(PARAM_SORT_BY, SORT_POPULARITY_DESC);


        }
        return builder.appendQueryParameter(PARAM_PAGE, String.valueOf(page)).build();
    }

    /**
     * Open a connection with the url specified as parameter and retrieve
     * a String representing a JSON query
     *
     * @param url the url to open the connection with
     * @return return a String representing a query in JSON
     * @throws IOException
     */

    public static String getResponseFromHttpsUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Build an return the uri of a movie poster in TheMovieDb
     *
     * @param imagePath the path of a specific image
     * @return the uri of the image
     */
    public static Uri buildImageUri(String imagePath) {
        Uri uri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W185).appendPath(imagePath).build();

        return uri;
    }

    /**
     * Download  and return a list of String representing JSON obiects,
     * retrieved by queries to TheMovieDb
     *
     * @param urls list of urls to query
     * @return a list of json responses from query to the @param urls
     */

    public static List<String> getJsonStringListFromHttpsUrls(List<URL> urls) {
        List<String> jsonResponse = new ArrayList<>();
        for (URL url : urls) {
            try {
                String page = getResponseFromHttpsUrl(url);
                if (page != null) {
                    jsonResponse.add(page);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }

    /**
     * Build a number of urls equals to numPages, representing different pages
     * in a query to TheMovieDb
     *
     * @param numPages    the number of pages to query
     * @param typeOfQuery a string between QUERY_MOST_POPULAR and QUERY_TOP_RATED
     * @return a list of URL objects
     */
    public static List<URL> getUrlsToLoad(int numPages, String typeOfQuery) {
        List<URL> urls = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            URL url = buildMoviesQueryURL(typeOfQuery, i);
            urls.add(url);
        }

        return urls;
    }

    /***
     * Build the url to query TheMovieDb for reviews of a movie with a certain id.
     *
     * @param id the movie id returned by a query to TheMovieDb API
     * @param page the number of page to query, must be greater than or equal to 1
     * @return the URL corresponding to the id and page provided.
     */
    public static URL buildReviewsUrl(String id, int page) {
        if (page < 1) {
            return null;
        }
        Uri requestUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEWS_DIR)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page)).build();
        return convertUriToURL(requestUri);
    }

    /**
     * Build the url to query TheMovieDb for video of a movie with a specific id
     *
     * @param id the movie id returned by a query to TheMovieDb API
     * @return the URL corresponding to the video request for the specified id
     */

    public static URL buildYoutubeUrl(String id) {
        Uri requestUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEO_DIR)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        return convertUriToURL(requestUri);
    }

    public static Uri buildYoutubeUri(String videoId) {
        Uri.Builder builder = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(VIDEO_PARAM, videoId);
        return builder.build();
    }


}
