package example.aleperf.com.popmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable {
    private String mAuthor;
    private String mReview;

    public Review(String author, String review) {
        mAuthor = author;
        mReview = review;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getReview() {
        return mReview;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mReview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mReview);
    }

    @SuppressWarnings("unused")
    public static final Creator CREATOR
            = new Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

}
