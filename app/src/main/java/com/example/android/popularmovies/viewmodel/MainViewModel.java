package com.example.android.popularmovies.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.MainRepository;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MediatorLiveData<List<Movie>> mMovieList;
    private MutableLiveData<List<Movie>> mMoviesFromInternet;
    private LiveData<List<Movie>> mFavourites;

    private MainRepository mRepository;

    private String mApiKey;

    public MainViewModel(AppDatabase database, String apiKey) {

        Log.d(TAG, "Preparing main viewModel");
        mRepository = MainRepository.getInstance(database);
        mApiKey = apiKey;

        mMovieList = new MediatorLiveData<>();
        mMoviesFromInternet = new MutableLiveData<>();
        mFavourites = getFavourites();

        mMovieList.addSource(mMoviesFromInternet, movies -> mMovieList.setValue(movies));

        loadPopular();
    }

    public LiveData<List<Movie>> getCurrentMovies() {
        return mMovieList;
    }

    private LiveData<List<Movie>> getFavourites() {
        return mRepository.loadAllFavourites();
    }

    public void loadFavourites() {
        mMovieList.addSource(mFavourites, movies ->
                mMovieList.setValue(mFavourites.getValue()));
    }

    public void loadPopular() {
        mMovieList.removeSource(mFavourites);
        mRepository.loadPopular(mMoviesFromInternet, mApiKey);
    }

    public void loadTopRated() {
        mMovieList.removeSource(mFavourites);
        mRepository.loadTopRated(mMoviesFromInternet, mApiKey);
    }

}
