package example.aleperf.com.popmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import example.aleperf.com.popmovies.data.MoviesContract;

/**
 * Used to check if the database exists and if it doesn't exist, initialize the app database.
 */

public class PopMoviesSyncUtils {

    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_HOURS = 24; // update once a day
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    //interval of 2 hours
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 12;

    private static final String POP_MOVIES_SYNC_TAG = "popmovies_sync";


    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) {

            return;
        }

        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);

        // this thread query a table of the database only to check if it's empty
        //this code tests only if the database it's created by the MovieProvider
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {


                Uri movieQueryUri = MoviesContract.MostPopuplarMoviesEntry.CONTENT_URI;

                String[] projectionColumns = {MoviesContract.MostPopuplarMoviesEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        movieQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (null == cursor) {

                    startImmediateSync(context);
                } else if (cursor.getCount() == 0) {

                    startImmediateSync(context);
                    cursor.close();
                }


            }
        });


        checkForEmpty.start();
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, PopMoviesSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);


        Job popMoviesSyncJob = dispatcher.newJobBuilder()
                .setService(PopMoviesFirebaseJobService.class)
                .setTag(POP_MOVIES_SYNC_TAG)

                //TODO Note to the reviewer: this job starts only on unmetered networks (like wi-fi)
                // in order to avoid possible consumption of bandwidth on your device.
                // In case you need a different type of network, please change the line below
                //to --->  .setConstraints(Constraint.ON_ANY_NETWORK)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow( //update every 24-26 hours
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(popMoviesSyncJob);
    }


}
