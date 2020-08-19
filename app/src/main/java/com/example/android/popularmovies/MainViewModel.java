package com.example.android.popularmovies;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;
import com.example.android.popularmovies.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MediatorLiveData<List<MovieModel>> mMovies;
    private MutableLiveData<List<MovieModel>> mMoviesFromInternet;
    private LiveData<List<FavouriteEntry>> favouritesFromDatabase;

    private DataRepository mRepository;

    private String mApiKey;


    public MainViewModel(AppDatabase database, String apiKey) {

        Log.d(TAG, "Preparing viewModel");
        mRepository = DataRepository.getInstance(database);
        mApiKey = apiKey;

        mMoviesFromInternet = new MutableLiveData<>();
        favouritesFromDatabase = new MutableLiveData<>();
        mMovies = new MediatorLiveData<>();
        mMovies.addSource(mMoviesFromInternet,
                movieModels -> mMovies.setValue(movieModels));
        mMovies.addSource(favouritesFromDatabase,
                movieModels -> mMovies.setValue(new ArrayList<>(movieModels)));

    }

    public LiveData<List<MovieModel>> getCurrentMovies() {
        return mMovies;
    }

    public void loadFavourites() {
        favouritesFromDatabase = mRepository.loadAllFavourites();
    }

    public void loadPopular() {
        mRepository.loadPopular(mMoviesFromInternet, mApiKey);
    }

    public void loadTopRated() {
        mRepository.loadTopRated(mMoviesFromInternet, mApiKey);
    }

}
