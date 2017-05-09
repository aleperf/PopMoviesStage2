package example.aleperf.com.popmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class PopMoviesSyncIntentService extends IntentService {

    public PopMoviesSyncIntentService() {
        super("PopMoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PopMoviesSyncTask.syncPopMoviesDatabase(this);
    }
}
