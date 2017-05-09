package example.aleperf.com.popmovies.utils;

import org.junit.Test;


import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import example.aleperf.com.popmovies.utilities.MovieUtils;


public class TestDateUtilities {

    private static GregorianCalendar calendar1 = new GregorianCalendar(2017, 2, 4); //March 4th, 2017
    private static GregorianCalendar calendar2 = new GregorianCalendar(1977, 4, 25);// May 25th, 1977 - Star Wars release
    private static GregorianCalendar calendar3 = new GregorianCalendar(2016, 11, 16); // December 16th, 2016 - Rogue One release
    private static GregorianCalendar calendar4 = new GregorianCalendar(1970, 0, 1);// January 1st, 1970 - Epoch
    private final static long time1 = calendar1.getTimeInMillis(); // March 4th, 2017
    private final static long time2 = calendar2.getTimeInMillis(); // May 25th 1977
    private final static long time3 = calendar3.getTimeInMillis(); // December 16, 2016
    private final static long time4 = calendar4.getTimeInMillis(); // January 01, 1970 (Epoch)


    // date string in format yyyy-MM-dd
    private final static String stringDate1 = "2017-03-04";
    private final static String stringDate2 = "1977-05-25";
    private final static String stringDate3 = "2016-12-16";
    private final static String stringDate4 = "1970-01-01";

    // proper formatted string
    private final static String formattedDate1 = "Mar 4, 2017";
    private final static String formattedDate2 = "May 25, 1977";
    private final static String formattedDate3 = "Dec 16, 2016";
    private final static String formattedDate4 = "Jan 1, 1970";


    @Test
    public void getTimeInMillisecondsReturnsCorrectTime() {

        assertEquals(time1, MovieUtils.getTimeInMilliseconds(stringDate1));
        assertEquals(time2, MovieUtils.getTimeInMilliseconds(stringDate2));
        assertEquals(time3, MovieUtils.getTimeInMilliseconds(stringDate3));
        assertEquals(time4, MovieUtils.getTimeInMilliseconds(stringDate4));

    }

    @Test
    public void formatTimeReturnsProperString() {
        assertEquals(formattedDate1, MovieUtils.formatTime(time1));
        assertEquals(formattedDate2, MovieUtils.formatTime(time2));
        assertEquals(formattedDate3, MovieUtils.formatTime(time3));
        assertEquals(formattedDate4, MovieUtils.formatTime(time4));
    }
}
