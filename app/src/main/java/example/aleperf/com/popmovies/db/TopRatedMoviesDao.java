package example.aleperf.com.popmovies.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import example.aleperf.com.popmovies.Movie;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopRatedMoviesDao {

    @Query("SELECT * FROM top_rated")
    public LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM top_rated WHERE id  = :id")
    public Movie loadMovieWithId(String id);

    @Insert(onConflict = REPLACE)
    void insertMovie(Movie movie);

    @Insert
    void insertAllMovies(List<Movie> movies);

    @Query("DELETE FROM top_rated")
    void deleteAll();
}
