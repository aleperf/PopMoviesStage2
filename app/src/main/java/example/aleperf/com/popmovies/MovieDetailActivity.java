package example.aleperf.com.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import example.aleperf.com.popmovies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity implements VideosFragment.ShareMovie {

    private final static String EXTRA_CURRENT_TABLE_URI = "current table uri";
    private final static String EXTRA_CURRENT_MOVIE_ID = "current movie id";
    private final static String EXTRA_CURRENT_MOVIE_TITLE = "current movie title";
    private final static String FIRST_VIDEO_URL_TAG = "first video url tag";

    private String mShareUrlString = VideosFragment.FAB_NO_VIDEO;
    private FloatingActionButton mFabShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        String movieId = getIntent().getStringExtra(EXTRA_CURRENT_MOVIE_ID);
        String movieTitle = getIntent().getStringExtra(EXTRA_CURRENT_MOVIE_TITLE);
        Uri currentTable = getIntent().getParcelableExtra(EXTRA_CURRENT_TABLE_URI);

        mFabShare = (FloatingActionButton) findViewById(R.id.fab_share_video);
        mFabShare.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mShareUrlString.equals(VideosFragment.FAB_NO_VIDEO)){
                    Toast.makeText(MovieDetailActivity.this, getString(R.string.video_trailer_no_video),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String shareString = getString(R.string.fab_button_share_text) + mShareUrlString;
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                    i.putExtra(Intent.EXTRA_TEXT, shareString);
                    startActivity(Intent.createChooser(i, getString(R.string.share_title)));
                }
            }
        });

        //inflate Toolbar, set As Action Bar and set the title  to that of current movie
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView movieTitleTextView = (TextView) findViewById(R.id.header_title_detail);
        movieTitleTextView.setText(movieTitle);

        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = fm.beginTransaction();
            MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movieId, currentTable);
            VideosFragment videosFragment = VideosFragment.newInstance(movieId);
            transaction.add(R.id.movie_detail_fragment_container,
                    movieDetailFragment, MovieDetailFragment.TAG);
            transaction.add(R.id.videos_fragment_container, videosFragment, VideosFragment.TAG);
            transaction.commit();
        } else {
            //reload mShareUrlStringValue
            mShareUrlString = savedInstanceState.getString(FIRST_VIDEO_URL_TAG, VideosFragment.FAB_NO_VIDEO);
            MovieDetailFragment fragmentMovieDetail = (MovieDetailFragment) fm.findFragmentByTag(MovieDetailFragment.TAG);
            if (fragmentMovieDetail != null) {
                fm.beginTransaction().replace(R.id.movie_detail_fragment_container,
                        fragmentMovieDetail, MovieDetailFragment.TAG).commit();
            } else {
                MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movieId, currentTable);
                fm.beginTransaction().replace(R.id.movie_detail_fragment_container, movieDetailFragment, MovieDetailFragment.TAG).commit();
            }

            VideosFragment videosFragment = (VideosFragment) fm.findFragmentByTag(VideosFragment.TAG);
            if (videosFragment != null) {
                fm.beginTransaction().replace(R.id.videos_fragment_container,
                        videosFragment, VideosFragment.TAG).commit();
            } else {
                videosFragment = VideosFragment.newInstance(movieId);
                fm.beginTransaction().replace(R.id.videos_fragment_container, videosFragment, videosFragment.TAG).commit();
            }
        }


    }


    @Override
    public void onShareMovie(String youtubeId) {
        if (!youtubeId.equals(VideosFragment.FAB_NO_VIDEO)) {
            mShareUrlString = NetworkUtils.buildYoutubeUri(youtubeId).toString();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FIRST_VIDEO_URL_TAG, mShareUrlString);

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
