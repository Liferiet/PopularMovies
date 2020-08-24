package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourite_movie ORDER BY internal_id")
    LiveData<List<Movie>> loadAllFavourites();

    @Insert
    void insertFavourite(Movie movie);

    @Query("DELETE FROM favourite_movie WHERE id=:id")
    void deleteFavourite(int id);

    @Query("SELECT id FROM favourite_movie WHERE id = :id LIMIT 1")
    int isFavourite(int id);

}
