package example.aleperf.com.popmovies;


import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.aleperf.com.popmovies.data.FavoritesIntentService;
import example.aleperf.com.popmovies.data.MoviesContract;
import example.aleperf.com.popmovies.data.MoviesProvider;
import example.aleperf.com.popmovies.utilities.MovieUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;


public class MovieDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public final static String TAG = MovieDetailFragment.class.getSimpleName();

    //MovieDetailExtraContent Interface  constants
    public final static String EXTRA_MOVIE_ID = "extra movie id";
    public final static String REVIEWS_DIALOG_TAG = "reviews dialogfragment tag";
    //constants
    private final int MOVIE_DETAIL_LOADER_ID = 33;
    private final static String MOVIE_ID_ARGS_KEY = "movie detail id";
    private final static String CURRENT_MOVIE_TABLE = "current movie table";

    //projection
    private final String[] MOVIE_DETAIL_PROJECTION = {
            MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
            MoviesContract.MovieEntry.COLUMN_RATING,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    //column indexes
    private static final int INDEX_ORIGINAL_TITLE = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_POSTER_PATH = 2;
    private static final int INDEX_PLOT_SYNOPSIS = 3;
    private static final int INDEX_RATING = 4;
    private static final int INDEX_RELEASE_DATE = 5;

    //fields
    private String mMovieId;
    private Uri mCurrentTable;
    private boolean mIsFavorite;
    private Movie mMovie;



    //Views
    private ImageView mPosterImage;

    private TextView mOriginalTitle;
    private Button mFavoriteButton;
    private TextView mDate;
    private RatingBar mRatingBar;
    private TextView mRatingDetail;
    private TextView mSynopsis;





    public MovieDetailFragment() {
        //empty constructor;
    }

    public static MovieDetailFragment newInstance(String movieId, Uri currentTable) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE_ID_ARGS_KEY, movieId);
        args.putParcelable(CURRENT_MOVIE_TABLE, currentTable);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieId = getArguments().getString(MOVIE_ID_ARGS_KEY);
        mCurrentTable = getArguments().getParcelable(CURRENT_MOVIE_TABLE);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMovieId = getArguments().getString(MOVIE_ID_ARGS_KEY);
        mCurrentTable = getArguments().getParcelable(CURRENT_MOVIE_TABLE);

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mPosterImage = (ImageView) rootView.findViewById(R.id.movie_poster_detail);
        mFavoriteButton = (Button) rootView.findViewById(R.id.favorite_button);

        mOriginalTitle = (TextView) rootView.findViewById(R.id.original_title);
        mDate = (TextView) rootView.findViewById(R.id.year_detail);
        mRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        mRatingDetail = (TextView) rootView.findViewById(R.id.rating_detail);
        mSynopsis = (TextView) rootView.findViewById(R.id.synopsis_detail);

        mFavoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mIsFavorite){
                    FavoritesIntentService.startActionDeleteFav(getContext(),mMovieId);
                    mFavoriteButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.prize1_gray));
                    mFavoriteButton.setText(getString(R.string.button_not_fav));
                    mIsFavorite = false;
                } else {

                    FavoritesIntentService.startActionInsertFav(getContext(),mMovie);
                    mFavoriteButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.prize1));
                    mFavoriteButton.setText(getString(R.string.button_marked_as_fav));
                    mIsFavorite = true;
                }
            }
        });

        LinearLayout reviews = (LinearLayout) rootView.findViewById(R.id.reviews);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                ReviewsDialogFragment reviewsDialogFragment = ReviewsDialogFragment.newInstance(mMovieId);
                reviewsDialogFragment.show(manager,REVIEWS_DIALOG_TAG);
            }
        });

        getActivity().getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = getContext();
        String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=? ";
        String[] selectionArgs = new String[]{mMovieId};
        UriMatcher matcher = MoviesProvider.buildUriMatcher();
        int match = matcher.match(mCurrentTable);
        if (match == MoviesProvider.CODE_FAVORITES) {
            // if the mCurrentTable is the Favorite table, obviously this movie is in the user favorites
            //initialize mIsFavorite to true
            mIsFavorite = true;

        } else { // we are in a table different from Favorites
            //check if the movie is already in the Favorites table
            Cursor cursor = context.getContentResolver().query(MoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                    null, selection, selectionArgs, null);
            if (cursor.getCount() > 0) { // if the cursor isn't empty initialize the mIsFavorite to true;
                mIsFavorite = true;
            }

        }

        return new CursorLoader(context, mCurrentTable, MOVIE_DETAIL_PROJECTION, selection, selectionArgs, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //initialize all the UI fields with appropriate data from the cursor
        if (data.moveToFirst()) {
            String originalTitle = data.getString(INDEX_ORIGINAL_TITLE);
            String title = data.getString(INDEX_TITLE);
            String posterPath = data.getString(INDEX_POSTER_PATH);
            String plotSynopsis = data.getString(INDEX_PLOT_SYNOPSIS);
            double rating = data.getFloat(INDEX_RATING);
            Long timeInMilliseconds = data.getLong(INDEX_RELEASE_DATE);

            mOriginalTitle.setText(originalTitle);

            mSynopsis.setText(plotSynopsis);
            mRatingDetail.setText(MovieUtils.formatRating(rating));
            mRatingBar.setRating(((float)rating) / 2);
            mDate.setText(MovieUtils.formatTime(timeInMilliseconds));
            if (posterPath != null) {
                Uri imagePath = NetworkUtils.buildImageUri(posterPath);
                Picasso.with(getContext()).load(imagePath).fit().error(R.drawable.no_preview_pop).into(mPosterImage);
            } else {
                Picasso.with(getContext()).load(R.drawable.no_preview_pop).into(mPosterImage);
            }
            if (mIsFavorite) {
                mFavoriteButton.setText(getString(R.string.button_marked_as_fav));
                mFavoriteButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.prize1));
            } else {
                mFavoriteButton.setText(getString(R.string.button_not_fav));
                mFavoriteButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.prize1_gray));
            }

           mMovie = new Movie(mMovieId, originalTitle, title, posterPath,
                   plotSynopsis, timeInMilliseconds, rating);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //the cursor is going to be closed
        //reset field to default value
        mIsFavorite = false;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(MOVIE_DETAIL_LOADER_ID, null, this);
    }


}
