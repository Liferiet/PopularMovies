package com.example.android.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavouriteEntry>> favourites;


    public MainViewModel(AppDatabase database) {

        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favourites = database.favouriteDao().loadAllFavourites();
    }

    public LiveData<List<FavouriteEntry>> getFavourites() {
        return favourites;
    }
}
