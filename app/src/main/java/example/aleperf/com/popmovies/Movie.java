package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {

    final private String mId;
    final private String mOriginalTitle;
    final private String mTitle;
    final private String mPosterPath;
    final private String mPlotSynopsis;
    final private double mRating;
    final private long  mReleaseDate;


    //Constructor
    public Movie(String id, String originalTitle, String title, String path,
                 String plot, long date, double rating) {
        mId = id;
        mOriginalTitle = originalTitle;
        mTitle = title;
        mPosterPath = path;
        mPlotSynopsis = plot;
        mReleaseDate = date;
        this.mRating = rating;

    }

    //Constructor used by Parcelable to deserialize data
    protected Movie(Parcel in) {
        mId = in.readString();
        mOriginalTitle = in.readString();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mPlotSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readLong();

    }

    public String getId() {
        return mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public long  getReleaseDate() {
        return mReleaseDate;
    }

    public double getRating() {
        return mRating;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mPlotSynopsis);
        parcel.writeDouble(mRating);
        parcel.writeLong(mReleaseDate);


    }

    @SuppressWarnings("unused")
    public static final Creator CREATOR
            = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
