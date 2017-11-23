package example.aleperf.com.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import example.aleperf.com.popmovies.sync.PopMoviesSyncUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MoviePosterClickListener {

    private final static String EXTRA_CURRENT_TABLE = "current table uri";
    private final static String EXTRA_CURRENT_MOVIE_ID = "current movie id";
    private final static String EXTRA_CURRENT_MOVIE_TITLE ="current movie title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inflate toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PopMoviesSyncUtils.initialize(this);


        FragmentManager fm = getSupportFragmentManager();
        //if this is the first time the app is initialized, add the needed fragment
        MovieGridFragment movieGridFragment = MovieGridFragment.newInstance();
        fm.beginTransaction().replace(R.id.movie_grid_fragment_container, movieGridFragment, MovieGridFragment.TAG).commit();

    }

    public void onClickMoviePoster(String movieId,String movieTitle, Uri currentTable) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra(EXTRA_CURRENT_MOVIE_ID, movieId);
        detailIntent.putExtra(EXTRA_CURRENT_TABLE, currentTable);
        detailIntent.putExtra(EXTRA_CURRENT_MOVIE_TITLE, movieTitle);
        startActivity(detailIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
