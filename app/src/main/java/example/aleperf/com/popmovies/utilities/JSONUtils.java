package example.aleperf.com.popmovies.utilities;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.Movie;
import example.aleperf.com.popmovies.Review;
import example.aleperf.com.popmovies.VideoTrailer;
import example.aleperf.com.popmovies.data.MoviesContract;

/**
 * Provides static methods to extract data from JSON strings
 */

public class JSONUtils {
    // constants for querying most popular and top rated movies
    private static final String TAG = JSONUtils.class.getSimpleName();
    private static final String ARRAY_OF_MOVIES = "results";
    private static final String MOVIE_ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String RATING = "vote_average";

    //constants used in the movie reviews json
    private static final String ARRAY_OF_REVIEWS = "results";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";

    //constants used in the movie video jsons
    private static final String ARRAY_OF_VIDEOS = "results";
    private static final String VIDEO_SITE = "site";
    private static final String TYPE_OF_VIDEO = "type";
    private static final String VIDEO_TITLE = "name";
    private static final String VIDEO_ID = "key";
    private static final String YOUTUBE = "YouTube";

    private static final String STATUS_CODE = "status_code";
    private static final int INVALID_API_KEY = 7;
    private static final int NOT_FOUND = 34;

    /**
     * Build a list of movies from a JSON string obtained from a query to TheMovieDb
     *
     * @param jsonString a JSON string returned by a query to TheMovieDb
     * @return a list of Movies
     * @throws JSONException
     */



    public static ContentValues[] getContentValuesFromJson(String jsonString) throws JSONException {

        List<Movie> movies = getMovieList(jsonString);
        ContentValues[] movieValues = new ContentValues[movies.size()];

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);

            String id = movie.getMovieId();
            String originalTitle = movie.getOriginalTitle();
            String title = movie.getTitle();
            String posterPath = movie.getPosterPath();
            String plotSynopsis = movie.getPlotSynopsis();
            long releaseDate = MovieUtils.getTimeInMilliseconds(movie.getReleaseDate());
            double rating = movie.getRating();


            ContentValues value = new ContentValues();
            value.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, id);
            value.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
            value.put(MoviesContract.MovieEntry.COLUMN_TITLE, title);
            value.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
            value.put(MoviesContract.MovieEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);
            value.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            value.put(MoviesContract.MovieEntry.COLUMN_RATING, rating);
            movieValues[i] = value;

        }

        return movieValues;
    }

    public static List<Review> getMovieReviews(String jsonString) throws JSONException {

        List<Review> reviewsList = new ArrayList<>();
        JSONObject reviewsJson = new JSONObject(jsonString);
        JSONArray reviewsArray = reviewsJson.getJSONArray(ARRAY_OF_REVIEWS);

        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject jsonReview = reviewsArray.getJSONObject(i);
            String author = jsonReview.getString(REVIEW_AUTHOR);
            String reviewContent = jsonReview.getString(REVIEW_CONTENT);
            Review review = new Review(author, reviewContent);
            reviewsList.add(review);
        }
        return reviewsList;
    }

    public static List<VideoTrailer> getMovieVideos(String jsonString) throws JSONException{
        List<VideoTrailer> videoList = new ArrayList<>();
        JSONObject videoJson = new JSONObject(jsonString);
        JSONArray videoArray = videoJson.getJSONArray(ARRAY_OF_VIDEOS);

        for(int i = 0; i < videoArray.length(); i++){
            JSONObject video = videoArray.getJSONObject(i);
            String videoSite = video.getString(VIDEO_SITE);
            if(videoSite.equals(YOUTUBE)){
                String title = video.getString(VIDEO_TITLE);
                String videoId = video.getString(VIDEO_ID);
                String type = video.getString(TYPE_OF_VIDEO);
                VideoTrailer videoTrailer = new VideoTrailer(title, videoId, type);
                videoList.add(videoTrailer);
            }
        }

      return videoList;
    }
    /**
     * Build a list of movies from a JSON string obtained from a query to TheMovieDb
     * @param jsonString a JSON string returned by a query to TheMovieDb
     * @return a list of Movies

     */

    public static List<Movie> getMovieList(String jsonString) {

        Gson gson = new Gson();
        MovieQuery query = gson.fromJson(jsonString, MovieQuery.class);
        Integer statusCode = query.getStatusCode();
        //TheMovieDB sends a status code only in case of error
        //if there is a status code, the query is invalid
        if(statusCode != null){
            return null;
        }

        return query.getResults();
    }


    public class MovieQuery {
        @SerializedName("status_code")
        Integer statusCode;
        @SerializedName("results")
        List<Movie> movieResults;

        List<Movie> getResults() {
            return movieResults;
        }

        Integer getStatusCode(){
            return statusCode;
        }


    }

}
