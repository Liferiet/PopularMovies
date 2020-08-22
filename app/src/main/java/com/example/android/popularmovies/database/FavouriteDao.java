package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.model.MovieModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourite_movie ORDER BY id")
    List<FavouriteEntry> loadAllFavourites();

    @Insert
    void insertFavourite(FavouriteEntry favouriteEntry);

    @Query("DELETE FROM favourite_movie WHERE external_id=:id")
    void deleteFavourite(int id);

    @Query("SELECT id FROM favourite_movie WHERE external_id = :id LIMIT 1")
    int isFavourite(int id);

}
