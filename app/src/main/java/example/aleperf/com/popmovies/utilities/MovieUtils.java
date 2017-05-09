package example.aleperf.com.popmovies.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.rating;
import static android.os.Build.VERSION_CODES.M;


/**
 * Provide static methods to format information about Movies in UI
 */

public class MovieUtils {

    /**
     * Format a date string in a more readable format in English.
     *
     * @param date a String date in the yyyy-dd--mm as provided by a query to a TheMovieDb
     * @return a formatted English date like Jan 1, 2017.
     */

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date newDate = inputFormat.parse(date);
            DateFormat outputFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
            return outputFormatter.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Format a rating as a fraction of 10.
     * input (double) : 8.1
     * output (String): 8.1/10
     *
     * @param rating a double representing a rating as returned from a query to TheMovieDb
     * @return a String representing the formatted rating as fraction of 10
     */

    public static String formatRating(double rating) {
        return String.format(Locale.ENGLISH, "%1$.1f/10", rating);
    }

    /**
     * Convert a date string from TheMovieDb Api in a time in milliseconds
     * (a format more convenient for storing in a database)
     *
     * @param dateString a String return from The MovieDbApi representing a date
     * @return a long representing a time in milliseconds
     */

    public static long getTimeInMilliseconds(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        long milliseconds = -1;
        try {
            Date newDate = inputFormat.parse(dateString);
            milliseconds = newDate.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    /**
     * Convert a time in milliseconds in a formatted date  string
     *
     * @param milliseconds, a long used to store a date in the database
     * @return a formatted string representing a date
     */

    public static String formatTime(long milliseconds) {
        DateFormat outputFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        Date date = new Date(milliseconds);
        return outputFormatter.format(date);
    }
}
