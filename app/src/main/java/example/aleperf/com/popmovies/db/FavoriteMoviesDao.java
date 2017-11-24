package example.aleperf.com.popmovies.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import example.aleperf.com.popmovies.Movie;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


public interface FavoriteMoviesDao {
    @Query("SELECT * FROM favorites")
    public LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM favorites WHERE id  = :id")
    public Movie loadMovieWithId(String id);

    @Insert(onConflict = REPLACE)
    void insertMovie(Movie movie);

    @Insert
    void insertAllMovies(List<Movie> movies);

    @Query("DELETE FROM most_popular")
    void deleteAll();
}
