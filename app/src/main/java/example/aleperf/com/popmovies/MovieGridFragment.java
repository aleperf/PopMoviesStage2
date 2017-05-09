package example.aleperf.com.popmovies;

import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import example.aleperf.com.popmovies.data.MoviesContract;
import example.aleperf.com.popmovies.data.MoviesProvider;
import example.aleperf.com.popmovies.sync.PopMoviesSyncTask;
import example.aleperf.com.popmovies.sync.PopMoviesSyncUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

import static android.os.Build.VERSION_CODES.M;


/**
 * This class host the UI interface used to show a list of Movies in a GridLayout,
 * accordingly to the user's preferences (Most Popular, Top Rated, Favorites)
 */

public class MovieGridFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public final static String TAG = MovieGridFragment.class.getSimpleName();
    public static final String[] MOVIE_GRID_PROJECTION = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH

    };

    public static int INDEX_MOVIE_ID = 0;
    public static int INDEX_MOVIE_TITLE = 1;
    public static int INDEX_POSTER_PATH = 2;


    private final static int GRID_LOADER_ID = 10;

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private TextView mEmptyTextView;
    private NestedScrollView  mEmptyScrollView;
    private ImageView mEmptyViewImage;
    private Uri mActualTable;



    public MovieGridFragment() {
        //empty constructor
    }


    public static MovieGridFragment newInstance() {

        MovieGridFragment fragment = new MovieGridFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        setCurrentTable();
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_grid);
        //inflate empty view elements
        mEmptyScrollView = (NestedScrollView) rootView.findViewById(R.id.empty_view_home_screen);
        mEmptyTextView = (TextView)rootView.findViewById(R.id.empty_view_message);
        mEmptyViewImage = (ImageView) rootView.findViewById(R.id.empty_view_image);
        mEmptyViewImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopMoviesSyncTask.syncPopMoviesDatabase(getActivity());
                Toast.makeText(getActivity(),getString(R.string.toast_msg_empty_view),  Toast.LENGTH_SHORT).show();
                getActivity().getSupportLoaderManager().restartLoader(GRID_LOADER_ID, null, MovieGridFragment.this);
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_main);
        mAdapter = new MovieAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        int numOfColumns = getResources().getInteger(R.integer.grid_layout_num_columns);
        GridLayoutManager manager = new GridLayoutManager(getContext(), numOfColumns);
        mRecyclerView.setLayoutManager(manager);

        getActivity().getSupportLoaderManager().initLoader(GRID_LOADER_ID, null, this);

        return rootView;
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);


        return new CursorLoader(getContext(),
                mActualTable,
                MOVIE_GRID_PROJECTION,
                null,
                null,
                null);


    }

    private void setCurrentTable() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String querySearch = preferences.getString(getString(R.string.pref_search_key),
                getString(R.string.pref_search_most_pop_value));
        if (querySearch.equals(getString(R.string.pref_search_most_pop_value))) {
            mActualTable = MoviesContract.MostPopuplarMoviesEntry.CONTENT_URI;

        } else if (querySearch.equals(getString(R.string.pref_search_top_rated_value))) {
            mActualTable = MoviesContract.TopRatedMoviesEntry.CONTENT_URI;

        } else {
            mActualTable = MoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.GONE);
        if(data == null || data.getCount()== 0){
            showEmptyView();
        } else {
            data.moveToFirst();
            hideEmptyView();
        }
        mAdapter.swapCursor(data, mActualTable);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTable();
        getActivity().getSupportLoaderManager().restartLoader(GRID_LOADER_ID, null, this);
    }

    public void showEmptyView(){
        //show a different empty view  for different tables
        UriMatcher matcher = MoviesProvider.buildUriMatcher();
        int match = matcher.match(mActualTable);
        switch(match){
            case MoviesProvider.CODE_FAVORITES:
                mEmptyScrollView.setVisibility(View.VISIBLE);
                mEmptyViewImage.setVisibility(View.GONE);
                mEmptyTextView.setVisibility(View.VISIBLE);
                mEmptyTextView.setText(getString(R.string.empty_view_favorites_message));
                break;
            default:
                mEmptyScrollView.setVisibility(View.VISIBLE);
                mEmptyViewImage.setVisibility(View.VISIBLE);
                mEmptyTextView.setVisibility(View.VISIBLE);
                mEmptyTextView.setText(getString(R.string.empty_view_message));

        }

    }

    public void hideEmptyView(){
       mEmptyScrollView.setVisibility(View.GONE);
    }

}
