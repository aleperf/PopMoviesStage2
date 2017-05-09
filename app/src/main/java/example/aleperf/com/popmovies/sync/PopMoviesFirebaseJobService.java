package example.aleperf.com.popmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;


public class PopMoviesFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mUpdatePopMoviesDatabaseTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mUpdatePopMoviesDatabaseTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                PopMoviesSyncTask.syncPopMoviesDatabase(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                jobFinished(job, false);
            }
        };


        mUpdatePopMoviesDatabaseTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mUpdatePopMoviesDatabaseTask != null) {
            mUpdatePopMoviesDatabaseTask.cancel(true);
        }
        return true;
    }
}
