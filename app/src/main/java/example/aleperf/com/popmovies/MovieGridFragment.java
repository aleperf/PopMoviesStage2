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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import example.aleperf.com.popmovies.data.MoviesContract;
import example.aleperf.com.popmovies.data.MoviesProvider;
import example.aleperf.com.popmovies.sync.PopMoviesSyncTask;


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

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private TextView emptyTextView;
    private NestedScrollView emptyScrollView;
    private ImageView emptyViewImage;
    private Uri actualTable;


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
        progressBar = rootView.findViewById(R.id.progress_bar_grid);
        //inflate empty view elements
        emptyScrollView = rootView.findViewById(R.id.empty_view_home_screen);
        emptyTextView = rootView.findViewById(R.id.empty_view_message);
        emptyViewImage = rootView.findViewById(R.id.empty_view_image);
        emptyViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMoviesSyncTask.syncPopMoviesDatabase(getActivity());
                Toast.makeText(getActivity(), getString(R.string.toast_msg_empty_view), Toast.LENGTH_SHORT).show();
                getActivity().getSupportLoaderManager().restartLoader(GRID_LOADER_ID, null, MovieGridFragment.this);
            }
        });
        recyclerView = rootView.findViewById(R.id.recycler_view_main);
        adapter = new MovieAdapter(getContext());
        recyclerView.setAdapter(adapter);
        int numOfColumns = getResources().getInteger(R.integer.grid_layout_num_columns);
        GridLayoutManager manager = new GridLayoutManager(getContext(), numOfColumns);
        recyclerView.setLayoutManager(manager);

        getActivity().getSupportLoaderManager().initLoader(GRID_LOADER_ID, null, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);


        return new CursorLoader(getContext(),
                actualTable,
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
            actualTable = MoviesContract.MostPopuplarMoviesEntry.CONTENT_URI;

        } else if (querySearch.equals(getString(R.string.pref_search_top_rated_value))) {
            actualTable = MoviesContract.TopRatedMoviesEntry.CONTENT_URI;

        } else {
            actualTable = MoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);
        if (data == null || data.getCount() == 0) {
            showEmptyView();
        } else {
            data.moveToFirst();
            hideEmptyView();
        }
        adapter.swapCursor(data, actualTable);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTable();
        getActivity().getSupportLoaderManager().restartLoader(GRID_LOADER_ID, null, this);
    }

    public void showEmptyView() {
        //show a different empty view  for different tables
        UriMatcher matcher = MoviesProvider.buildUriMatcher();
        int match = matcher.match(actualTable);
        switch (match) {
            case MoviesProvider.CODE_FAVORITES:
                emptyScrollView.setVisibility(View.VISIBLE);
                emptyViewImage.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getString(R.string.empty_view_favorites_message));
                break;
            default:
                emptyScrollView.setVisibility(View.VISIBLE);
                emptyViewImage.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getString(R.string.empty_view_message));

        }

    }

    public void hideEmptyView() {
        emptyScrollView.setVisibility(View.GONE);
    }

}
