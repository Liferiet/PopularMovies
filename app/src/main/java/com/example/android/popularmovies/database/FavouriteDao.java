package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourite_movie ORDER BY id")
    LiveData<List<FavouriteEntry>> loadAllFavourites();

    @Insert
    void insertFavourite(FavouriteEntry favouriteEntry);

    @Delete
    void deleteFavourite(FavouriteEntry favouriteEntry);

    // TODO loadFavouriteById ?
}
