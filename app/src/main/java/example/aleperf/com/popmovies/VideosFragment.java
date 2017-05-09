package example.aleperf.com.popmovies;




import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

public class VideosFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<VideoTrailer>> {

    public final  static String EXTRA_MOVIE_ID = "extra movie id";
    public final static String TAG = VideosFragment.class.getSimpleName();
    public final static String FAB_NO_VIDEO ="no movie to share";
    private String mMovieId;
    private List<VideoTrailer> mMovieVideoTrailer;
    private TextView mVideoTitleTextView;
    private VideoTrailerAdapter mAdapter;
    private final static int VIDEO_TRAILER_LOADER_ID = 421;

    public interface ShareMovie{
        void  onShareMovie(String youtubeId);
    }

    public VideosFragment(){
        //empty constructor
    }

    public static VideosFragment newInstance(String movieId){
        VideosFragment fragment = new VideosFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MOVIE_ID, movieId);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieId = getArguments().getString(EXTRA_MOVIE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_videos,container, false );
        mVideoTitleTextView = (TextView) rootView.findViewById(R.id.videos_title);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new VideoTrailerAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(VIDEO_TRAILER_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<List<VideoTrailer>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<VideoTrailer>>(getActivity()) {

            @Override
            protected void onStartLoading() {
                if (mMovieVideoTrailer != null && mMovieVideoTrailer.size() > 0) {
                    deliverResult(mMovieVideoTrailer);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<VideoTrailer> loadInBackground() {
                List<VideoTrailer> videoTrailers = new ArrayList<>();
                URL youtubeUrl = NetworkUtils.buildYoutubeUrl(mMovieId);
                try {
                    String jsonString = NetworkUtils.getResponseFromHttpsUrl(youtubeUrl);
                    videoTrailers = JSONUtils.getMovieVideos(jsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return videoTrailers;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<VideoTrailer>> loader, List<VideoTrailer> data) {
        if(data == null || data.size() == 0){
            setVideoTitleNoVideos();
            setFabData(data, false);
        } else {
            setFabData(data, true);

        }
        mMovieVideoTrailer = data;

        mAdapter.setVideoTrailerData(data);
    }

    private void setFabData(List<VideoTrailer> data, boolean hasVideo) {
        String firstTrailerId = FAB_NO_VIDEO;
        if (hasVideo) {
            VideoTrailer firstTrailer = data.get(0);
            firstTrailerId = firstTrailer.getTrailerId();
        }

        if (getActivity() instanceof ShareMovie) {
            ((ShareMovie) getActivity()).onShareMovie(firstTrailerId);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<VideoTrailer>> loader) {
        mAdapter.setVideoTrailerData(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(VIDEO_TRAILER_LOADER_ID, null, this);
    }

    private void setVideoTitleNoVideos(){
        mVideoTitleTextView.setText(getString(R.string.video_trailer_no_video));
    }

    private void setVideoTitleVideos(){
        mVideoTitleTextView.setText(getString(R.string.video_trailer_title));
    }
}
