package example.aleperf.com.popmovies.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import example.aleperf.com.popmovies.Movie;

@Entity(tableName = "top_rated")
public class TopRatedMovies extends Movie {

    public TopRatedMovies(String id, String originalTitle, String title, String plotSynopsis, double rating,
                          String releaseDate, String posterPath, String backdropPath) {
        super(id, originalTitle, title, plotSynopsis, rating, releaseDate, posterPath, backdropPath);
    }
}
