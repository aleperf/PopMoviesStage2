package example.aleperf.com.popmovies.db;


import android.arch.persistence.room.Entity;

import example.aleperf.com.popmovies.Movie;

@Entity(tableName = "favorites")
public class FavoriteMovies extends Movie {
    public FavoriteMovies(String id, String originalTitle, String title, String plotSynopsis, double rating,
                          String releaseDate, String posterPath, String backdropPath) {
        super(id, originalTitle, title, plotSynopsis, rating, releaseDate, posterPath, backdropPath);
    }
}
