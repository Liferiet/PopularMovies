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

/**
 * Stores data for MainActivity.
 * Has reference to repository that is responsible for the entire data retrieval logic
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MediatorLiveData<List<Movie>> mMovieList;
    private MutableLiveData<List<Movie>> mMoviesFromInternet;
    private LiveData<List<Movie>> mFavourites;

    private MainRepository mRepository;

    private String mApiKey;

    /**
     * Constructor
     * Adds a list of movies from remote data source as observed data. If this list is updated
     * the list with current movies is also updated.
     * Initially list is populated with list of popular movies.
     * @param database instance created at application startup
     * @param apiKey application API_KEY
     */
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
