package example.aleperf.com.popmovies;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import example.aleperf.com.popmovies.utilities.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Context mContext;
    private Cursor mCursor;
    //specify which table to query
    private Uri mActualTable;

    /**
     * Interface used to manage the click on a Movie Poster
     */
    public interface MoviePosterClickListener {

        void onClickMoviePoster(String movieId,String movieTitle, Uri currentTable);
    }


    public MovieAdapter(Context context) {
        this.mContext = context;

    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_list_main, parent, false);
        return new MovieHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bindMovie(position);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor data, Uri actualTable) {
        mCursor = data;
        mActualTable = actualTable;
        notifyDataSetChanged();
    }


    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView movieTitle;

        public MovieHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.movie_poster);
            movieTitle = (TextView) view.findViewById(R.id.title_no_image_preview);
            poster.setOnClickListener(this);
        }

        public void bindMovie(int position) {
            mCursor.moveToPosition(position);
            String id = mCursor.getString(MovieGridFragment.INDEX_MOVIE_ID);
            String title = mCursor.getString(MovieGridFragment.INDEX_MOVIE_TITLE);
            String posterPath = mCursor.getString(MovieGridFragment.INDEX_POSTER_PATH);
            if (posterPath != null) {
                Uri imagePath = NetworkUtils.buildImageUri(posterPath);
                Picasso.with(mContext).load(imagePath).fit().error(R.drawable.no_preview_pop).into(poster);
                movieTitle.setVisibility(View.INVISIBLE);
            } else {// if the movie hasn't a poster load the default image and show the movie title
                Picasso.with(mContext).load(R.drawable.no_preview_pop).fit().into(poster);
                movieTitle.setText(title);
                movieTitle.setVisibility(View.VISIBLE);
            }


        }


        @Override
        public void onClick(View view) {
            if (mContext instanceof MoviePosterClickListener) {
                MoviePosterClickListener listener = (MoviePosterClickListener) mContext;
                int position = getAdapterPosition();
                mCursor.moveToPosition(position);
                String id = mCursor.getString(MovieGridFragment.INDEX_MOVIE_ID);
                String title = mCursor.getString(MovieGridFragment.INDEX_MOVIE_TITLE);
                listener.onClickMoviePoster(id, title, mActualTable);
            }
        }
    }
}
