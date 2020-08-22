package com.example.android.popularmovies.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.MainRepository;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;
import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MediatorLiveData<List<? extends MovieModel>> mMovies;
    private MutableLiveData<List<MovieModel>> mMoviesFromInternet;
    private MutableLiveData<List<FavouriteEntry>> favouritesFromDatabase;

    private MainRepository mRepository;

    private String mApiKey;

    public MainViewModel(AppDatabase database, String apiKey) {

        Log.d(TAG, "Preparing main viewModel");
        mRepository = MainRepository.getInstance(database);
        mApiKey = apiKey;

        mMoviesFromInternet = new MutableLiveData<>();
        favouritesFromDatabase = new MutableLiveData<>();
        mMovies = new MediatorLiveData<>();
        mMovies.addSource(mMoviesFromInternet,
                movieModels -> mMovies.setValue(movieModels));
        mMovies.addSource(favouritesFromDatabase,
                movieModels -> mMovies.setValue(movieModels));

        loadPopular();
    }

    public LiveData<List<? extends MovieModel>> getCurrentMovies() {
        return mMovies;
    }

    public void loadFavourites() {
        mRepository.loadAllFavourites(favouritesFromDatabase);
    }

    public void loadPopular() {
        mRepository.loadPopular(mMoviesFromInternet, mApiKey);
    }

    public void loadTopRated() {
        mRepository.loadTopRated(mMoviesFromInternet, mApiKey);
    }

}
