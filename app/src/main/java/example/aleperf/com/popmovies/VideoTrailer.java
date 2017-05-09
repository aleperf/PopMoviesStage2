package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class VideoTrailer implements Parcelable {
    private String title;
    private String trailerId;
    private String typeOfVideo;

    public VideoTrailer(String title, String trailerId, String typeOfVideo) {
        this.title = title;
        this.trailerId = trailerId;
        this.typeOfVideo = typeOfVideo;
    }

    public String getTitle() {
        return title;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getTypeOfVideo() {
        return typeOfVideo;
    }

    protected VideoTrailer(Parcel in) {
        title = in.readString();
        trailerId = in.readString();
        typeOfVideo = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(trailerId);
        parcel.writeString(typeOfVideo);


    }

    @SuppressWarnings("unused")
    public static final Creator CREATOR
            = new Creator() {
        public VideoTrailer createFromParcel(Parcel in) {
            return new VideoTrailer(in);
        }

        public VideoTrailer[] newArray(int size) {
            return new VideoTrailer[size];
        }
    };
}
