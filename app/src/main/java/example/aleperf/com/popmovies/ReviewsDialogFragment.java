package example.aleperf.com.popmovies;



import android.graphics.Point;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

/**
 * Create a Dialog to Show the Reviews for a movie
 */

public class ReviewsDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<List<Review>> {

    private final static int REVIEW_LOADER_ID = 231;


    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mAdapter;
    private List<Review> mReviews;
    private String mMovieId;
    private TextView mEmptyView;

    public ReviewsDialogFragment() {
        //empty constructor
    }

    public static ReviewsDialogFragment newInstance(String movieId) {
        ReviewsDialogFragment fragment = new ReviewsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MovieDetailFragment.EXTRA_MOVIE_ID, movieId);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_reviews, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovieId = getArguments().getString(MovieDetailFragment.EXTRA_MOVIE_ID);
        mEmptyView = (TextView)view.findViewById(R.id.reviews_empty_view);
        mReviewsRecyclerView = (RecyclerView) view.findViewById(R.id.reviews_recycler_view);
        mAdapter = new ReviewAdapter(getContext());
        mReviewsRecyclerView.setAdapter(mAdapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_reviews);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }});
        getActivity().getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);


    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Review>>(getContext()) {
            @Override
            protected void onStartLoading() {
                if (mReviews != null && mReviews.size() > 0) {
                    deliverResult(mReviews);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Review> loadInBackground() {
                List<Review> results = new ArrayList<>();
                // build the query
                URL url = NetworkUtils.buildReviewsUrl(mMovieId, 1);

                try {
                    // query TheMovieDb
                    String jsonString = NetworkUtils.getResponseFromHttpsUrl(url);
                    // parse the JSON and return the list of reviews
                    results = JSONUtils.getMovieReviews(jsonString);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return results;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        if(data == null || data.size()== 0){
            showEmptyView();
        } else {
            hideEmptyView();
        }
        mReviews = data;
        mAdapter.setReviewsData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
        mAdapter.setReviewsData(null);
    }

    @Override
    public void onResume() {
        //get the screen width
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog to a percentage of the screen width,
        // with a value from resources depending on screen size (greater the screen, smaller the percentage)
        TypedValue value = new TypedValue();
        getResources().getValue(R.dimen.reviews_dialog_width, value, true);
        float width_percentage = value.getFloat();
        window.setLayout((int) (size.x * width_percentage ), WindowManager.LayoutParams.WRAP_CONTENT);
        //put the dialog window in the middle of the screen
        window.setGravity(Gravity.CENTER);
        super.onResume();
        //restart the loader
        getActivity().getSupportLoaderManager().restartLoader(REVIEW_LOADER_ID, null, this);
    }

    private void showEmptyView(){
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){
        mEmptyView.setVisibility(View.GONE);
    }


}
