package example.aleperf.com.popmovies;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class Movie implements Parcelable {

    private final static String NO_IMAGE = "no image";


    @SuppressWarnings("unused")
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @PrimaryKey
    @SerializedName("id")
     public String movieId;

    @ColumnInfo(name="original_title")
    @SerializedName("original_title")
    public String originalTitle;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    public String title;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    public String plotSynopsis;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    public double rating;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    public String releaseDate;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    public String backdropPath;


    public Movie(String id, String originalTitle, String title, String plotSynopsis, double rating,
                 String releaseDate, String posterPath, String backdropPath) {

        this.movieId = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.plotSynopsis = plotSynopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;


    }


    //Constructor used by Parcelable to deserialize data
    private Movie(Parcel in) {
        movieId = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        plotSynopsis = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();

    }

    public String getMovieId() {
        return movieId;
    }

   public  String getOriginalTitle() {
        return originalTitle;
    }

   public  String getTitle() {
        return title;
    }

   public  String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getPosterPath() {
        if (posterPath != null) {
            return posterPath.replace("/", "");
        }
        return NO_IMAGE;
    }

    private String getBackdropPath() {
        if (backdropPath != null) {
            return backdropPath.replace("/", "");
        }
        return NO_IMAGE;
    }

   public String getReleaseDate() {
        return releaseDate;
    }

   public  double getRating() {
        return rating;
    }

    public boolean hasImage() {

        return !(getPosterPath().equals(NO_IMAGE));
    }

  public  boolean hasBackdropPath() {
        return !(getBackdropPath().equals(NO_IMAGE));
    }



    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieId);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);


    }
}