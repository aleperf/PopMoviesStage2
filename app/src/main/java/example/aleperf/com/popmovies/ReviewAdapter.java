package example.aleperf.com.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context mContext;
    private List<Review> mReviewsList;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.bindReview(position);
    }

    @Override
    public int getItemCount() {
        if (mReviewsList == null) {
            return 0;
        }
        return mReviewsList.size();
    }

    public void setReviewsData(List<Review> reviewList) {
        mReviewsList = reviewList;
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthor;
        private TextView mReview;

        public ReviewHolder(View view) {
            super(view);
            mAuthor = (TextView) view.findViewById(R.id.review_author);
            mReview = (TextView) view.findViewById(R.id.review_content);
        }

        private void bindReview(int position) {
            Review review = mReviewsList.get(position);
            String authorString = mContext.getString(R.string.review_author) + review.getAuthor();
            mAuthor.setText(authorString);
            mReview.setText(review.getReview());
        }

    }
}
