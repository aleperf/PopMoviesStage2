package example.aleperf.com.popmovies.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import example.aleperf.com.popmovies.Movie;

@Entity(tableName = "most_popular")
public class MostPopularMovies extends Movie{


  public MostPopularMovies  (String id, String originalTitle, String title, String plotSynopsis, double rating,
    String releaseDate, String posterPath, String backdropPath){
   super(id, originalTitle, title, plotSynopsis, rating, releaseDate, posterPath, backdropPath);
  }
}
